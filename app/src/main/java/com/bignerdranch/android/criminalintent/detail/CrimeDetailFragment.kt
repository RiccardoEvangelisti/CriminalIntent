package com.bignerdranch.android.criminalintent.detail

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeDetailBinding
import com.bignerdranch.android.criminalintent.model.Crime
import kotlinx.coroutines.launch
import java.util.*

// Un Fragment è un componente simile ad Activity, ma più flessibile e manutenibile. Deve essere legato ad una View e hostato da una Activity
// *ATTENZIONE* usare androidx.fragment.app.Fragment
// *ATTENZIONE*: non sono un buon posto dove salvare lo stato dell'app
class CrimeDetailFragment : Fragment() {

	// VIEW BINDING:
	// 1) si occupa di collegare il layout al Fragment
	// 2) fornisce i componenti grafici della view al Fragment
	// *ATTENZIONE*: Se il layout che si vuole associare è chiamato "fragment_riccardo_eva", il binding si chiama "FragmentRiccardoEvaBinding"
	// *OBBLIGATORIO* aggiungere la dipendenza in app/build.gradle
	// *OBBLIGATORIO*: per evitare memory leak quando si cambia View, si usano 2 variabili e rese null nella onDestroyView()
	private var _binding: FragmentCrimeDetailBinding? = null
	private val binding
		get() = checkNotNull(_binding) {
			"Cannot access binding because it is null. Is the view visible?"
		}

	// SAFE ARGS:
	// 1) "nomeFragment" + "Args"
	// 2) si usa quando attraverso il navigator si passa da una View all'altra
	// 3) gli argomenti sono definiti nel nav_graph.xml
	private val args: CrimeDetailFragmentArgs by navArgs()

	// VIEW MODEL:
	// Per collegare il ViewModel al Fragment su utilizza una "property delegate" (stesso funzionamento di lazy)
	// *ATTENZIONE*: per passare al ViewModel degli argomenti bisogna utilizzare una factory
	private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
		CrimeDetailViewModelFactory(args.crimeId)
	}

	// OnCreateView()
	// 1) viene chiamata dal FragmentManager della hosting activity
	// 2) viene fatto l'inflate e bind del layout
	// 3) viene ritornata la View
	// *CONVENZIONE*: Non inserire nulla in più in questa funzione ma usare le funzioni chiamate successivamente
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		_binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	// OnViewCreated()
	// 1) viene chiamata immediatamente dopo la onCreateView
	// 2) qui si impostano i listeners
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// Custom back navigation
		activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, true) {
			if (binding.crimeTitle.text.isNotBlank()) {
				findNavController().navigateUp() // non si può terminare l'activity con requireActivity().finish() perché questo è un fragment
			} else {
				Toast.makeText(context, "ERRORE: Inserire un titolo", Toast.LENGTH_SHORT).show()
			}
		}

		// Listeners che al cambiamento del dato sulla UI applicano la modifica al ViewModel (UI->ViewModel)
		binding.apply {
			crimeTitle.doOnTextChanged { text, _, _, _ ->
				crimeDetailViewModel.updateCrime { oldCrime ->
					oldCrime.copy(title = text.toString()) // viene passata una copia del valore con un attribito cambiato
				}
			}

			crimeSolved.setOnCheckedChangeListener { _, isChecked ->
				crimeDetailViewModel.updateCrime { oldCrime ->
					oldCrime.copy(isSolved = isChecked)
				}
			}
		}

		// Colleziono lo StateFlow del ViewModel e con esso aggiorno la UI (ViewModel->UI)
		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
				crimeDetailViewModel.crime.collect { crime ->
					crime?.let { updateUi(it) }
				}
			}
		}

		// Setta un listener che ascolta il momento in cui un altro componente ha chiamato "setFragmentResult" con la stessa chiave (REQUEST_KEY_DATE)
		setFragmentResultListener(DatePickerFragment.REQUEST_KEY_DATE) { _, bundle ->
			@Suppress("DEPRECATION")
			// dal bundle (la mappatura key-value) si preleva il valore usando la giusta chiave
			val newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
			// Update del dato
			crimeDetailViewModel.updateCrime { it.copy(date = newDate) }
		}
	}

	// Funzione che applica le modifiche alla UI quando il dato viene modificato
	private fun updateUi(crime: Crime) {
		binding.apply {
			if (crimeTitle.text.toString() != crime.title) { // previene un infinite-loop con il listener
				crimeTitle.setText(crime.title)
			}

			crimeSolved.isChecked = crime.isSolved

			crimeDate.text = crime.date.toString()
			crimeDate.setOnClickListener { // viene inserito qui il listener perché qui è l'unico posto dove ho accesso al crime più aggiornato
				findNavController().navigate(CrimeDetailFragmentDirections.selectDate(crime.date))
			}

			crimeReport.setOnClickListener {
				val reportIntent = Intent(/*tipo di Intent*/Intent.ACTION_SEND).apply {
					type = "text/plain" // Obbligatorio
					putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime)) // Messaggio principale
					putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject)) // Il soggetto del messaggio
				}
				val chooserIntent = Intent.createChooser(reportIntent, getString(R.string.send_report)) // Apre il chooser (con funzionalità di copia)
				startActivity(chooserIntent)

			}
		}
	}

	// VIEW BINDING: è importante liberare binding per evitare memory leaking quando si cambia view
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	// GETSTRING()
	// Funzione che genera il messaggio del crimine andando a prelevare dal file strings delle risorse
	private fun getCrimeReport(crime: Crime): String {
		val solvedString = if (crime.isSolved) {
			getString(R.string.crime_report_solved)
		} else {
			getString(R.string.crime_report_unsolved)
		}
		val dateString = DateFormat.getDateFormat(context).format(crime.date).toString()
		val suspectText = if (crime.suspect.isBlank()) {
			getString(R.string.crime_report_no_suspect)
		} else {
			getString(R.string.crime_report_suspect, crime.suspect)
		}
		return getString(R.string.crime_report, crime.title, dateString, solvedString, suspectText)
	}
}