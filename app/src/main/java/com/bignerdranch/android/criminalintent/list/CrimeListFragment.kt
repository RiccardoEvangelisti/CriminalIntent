package com.bignerdranch.android.criminalintent.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.launch

class CrimeListFragment : Fragment() {

	// VIEW BINDING
	private var _binding: FragmentCrimeListBinding? = null
	private val binding
		get() = checkNotNull(_binding) {
			"Cannot access binding because it is null. Is the view visible?"
		}

	// VIEW MODEL
	private val crimeListViewModel: CrimeListViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		_binding = FragmentCrimeListBinding.inflate(inflater, container, false)

		// RECYCLER VIEW:
		// Un "LayoutManager" è un componente *necessario* che ha il compito di posizionare gli elementi del RecyclerView
		// "LinearLayoutManager" dispone gli elementi uno sotto l'altro come il LinearLayout
		binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		// Coroutine per il prelievo dei dati. Lo scope è quello del ViewModel
		viewLifecycleOwner.lifecycleScope.launch {

			// Esegue il blocco solo quando la view entra nello stato Started, perché non ha senso caricare i dati quando la UI non è visibile
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

				// Il collector osserva il Flow dei dati e la lambda viene chiamata ogni volta che vi è un nuovo dato nel Flow
				// In questo modo, quando la UI efettua una modifica sul db, il dato mostrato è il più aggiornato
				crimeListViewModel.crimes.collect { crimes ->

					// RECYCLE VIEW: setting dell'adapter con il dataset
					binding.crimeRecyclerView.adapter = CrimeListAdapter(crimes) { crimeId ->
						// NAVIGATOR CONTROLLER: si può chiamare la destinazione con l'ID della View o usando SafeArgs.
						// Le azioni sono definite nel nav_graph
						findNavController().navigate(
							// SAFE ARGS: "nomeFragment" + "Directions". Gli argomenti sono definiti nel nav_graph.xml
							CrimeListFragmentDirections.showCrimeDetail(crimeId))
					}
				}
			}
		}
	}

	// VIEW BINDING
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}