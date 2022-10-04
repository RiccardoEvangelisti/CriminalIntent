package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.launch
import java.util.*

class CrimeDetailFragment : Fragment() {

	private val args: CrimeDetailFragmentArgs by navArgs()

	// Utilizzo la factory per generare un ViewModel con argomenti
	private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
		CrimeDetailViewModelFactory(args.crimeId)
	}

	// Questa doppia variabile serve per poter nullificare binding durante la fase di onDestroyView()
	private var _binding: FragmentCrimeDetailBinding? = null
	private val binding
		get() = checkNotNull(_binding) {
			"Cannot access binding because it is null. Is the view visible?"
		}

	// Qui viene fatto l'inflate e bind del layout (serve LayoutInflater e ViewGroup). Ritorna la view alla hosting activity
	// Non inserire nulla in più in questa funzione ma usare le funzioni chiamate successivamente
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		_binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	// Viene chiamata immediatamente dopo la onCreateView
	// Qui si impostano i listeners
	@RequiresApi(33)
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// Custom back navigation
		activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, true) {
			if (binding.crimeTitle.text.isNotBlank()) {
				findNavController().navigateUp()
			} else {
				Toast.makeText(context, "ERRORE: Inserire un titolo", Toast.LENGTH_SHORT).show()
			}
		}

		binding.apply {
			crimeTitle.doOnTextChanged { text, _, _, _ ->
				crimeDetailViewModel.updateCrime { oldCrime ->
					oldCrime.copy(title = text.toString())
				}
			}

			crimeSolved.setOnCheckedChangeListener { _, isChecked ->
				crimeDetailViewModel.updateCrime { oldCrime ->
					oldCrime.copy(isSolved = isChecked)
				}
			}
		}

		// Colleziono lo StateFlow e con esso aggiorno la UI
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

	// è importante liberare binding per evitare memory leaking nel momento in cui si cambia view
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	private fun updateUi(crime: Crime) {
		binding.apply {
			if (crimeTitle.text.toString() != crime.title) { //previene un infinite-loop con il listener
				crimeTitle.setText(crime.title)
			}

			crimeSolved.isChecked = crime.isSolved

			crimeDate.text = crime.date.toString()
			crimeDate.setOnClickListener { // viene inserito qui il listener perché qui è l'unico posto dove ho accesso al crime più aggiornato
				findNavController().navigate(CrimeDetailFragmentDirections.selectDate(crime.date))
			}
		}
	}
}