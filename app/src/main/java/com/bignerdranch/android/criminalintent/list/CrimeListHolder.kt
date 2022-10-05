package com.bignerdranch.android.criminalintent.list

import android.text.format.DateFormat
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.databinding.ListItemCrimeBinding
import com.bignerdranch.android.criminalintent.model.Crime
import java.util.*

// RECYCLER VIEW
// 1) il ViewHolder possiede una singola istanza di una View
// 2) ci sono tanti viewHolder quante view da mostrare nel RecycleView
// 3) i viewHolder vengono generati dall'Adapter
// 4) si occupa dei listener e della formattazione dei dati
class CrimeListHolder(
	private val binding: ListItemCrimeBinding
) : RecyclerView.ViewHolder(binding.root) {

	fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit) {

		// Si usa una lambda expression per lasciare al Fragment la gestione della navigazione verso altri Fragment
		binding.root.setOnClickListener {
			onCrimeClicked(crime.id)
		}

		// Riempimento della View con i dati
		binding.crimeTitle.text = crime.title
		binding.crimeDate.text = DateFormat.format("hh:mm a dd/MM/yyyy", crime.date)
		binding.crimeSolved.visibility = if (crime.isSolved) {
			View.VISIBLE
		} else {
			View.GONE
		}
	}
}
