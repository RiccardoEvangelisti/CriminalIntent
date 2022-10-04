package com.bignerdranch.android.criminalintent

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

	private var _binding: FragmentCrimeListBinding? = null
	private val binding
		get() = checkNotNull(_binding) {
			"Cannot access binding because it is null. Is the view visible?"
		}

	private val crimeListViewModel: CrimeListViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		_binding = FragmentCrimeListBinding.inflate(inflater, container, false)

		binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		// Eseguo il prelievo dei dati in un viewLifecycleScope, il quale "cancella il lavoro" se la view viene distrutta
		// (ossia blocca ed elimina l'esecuzione del blocco di codice se questo è ancora in corso
		viewLifecycleOwner.lifecycleScope.launch {
			// Ripete il prelievo dei dati (=esegue una coroutine) ogni volta che la view entra nello stato Started,
			// quindi rimane in esecuzione se è nello stato Resumed, ma viene "cancellata" se torna nello stato Created
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				// Osserva il Flow dei dati. La lambda viene chiamata ogni volta che il dato cambia
				crimeListViewModel.crimes.collect { crimes ->
					// All'Adapter vengono passati i dati del ViewModel e una lambda che performa una azione di navigazione dato un ID
					binding.crimeRecyclerView.adapter = CrimeListAdapter(crimes) { crimeId ->
						// import fragment version
						findNavController().navigate(
							// Uso di Safe Args
							CrimeListFragmentDirections.showCrimeDetail(crimeId))
					}
				}
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}