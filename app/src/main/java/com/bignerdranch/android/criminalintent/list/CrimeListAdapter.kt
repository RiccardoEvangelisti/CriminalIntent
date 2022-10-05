package com.bignerdranch.android.criminalintent.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.databinding.ListItemCrimeBinding
import com.bignerdranch.android.criminalintent.model.Crime
import java.util.*

// RECYCLER VIEW
// 1) l'Adapter e' responsabile della creazione dei ViewHolder
// 2) l'Adapter effettua il bind dei dati (riempimento della grafica) solo quando richiesto
// 3) il RecyclerView si occupa del chiedere all'Adapter di fare il bind tra l'older e l'item ad una certa posizione
// *BEST PRACTICE*: l'Adapter deve conoscere il meno possibile di cosa fa l'Holder e di come è composta la singola View
class CrimeListAdapter(
	private val crimes: List<Crime>, private val onCrimeClicked: (crimeId: UUID) -> Unit
) : RecyclerView.Adapter<CrimeListHolder>() {

	// OnCreateViewHolder()
	// 1) crea un VIEW BINDING della singola view
	// 2) crea un View Holder
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeListHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
		return CrimeListHolder(binding)
	}

	// onBindViewHolder()
	// 1) popola l'Holder con i dati di un item ad una certa posizione
	override fun onBindViewHolder(holder: CrimeListHolder, position: Int) {
		val crime = crimes[position]
		holder.bind(crime, onCrimeClicked)
	}

	// Il RecyclerView deve conoscere quanti elementi ci sono nel data set
	override fun getItemCount() = crimes.size
}