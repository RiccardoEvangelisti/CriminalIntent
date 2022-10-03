package com.bignerdranch.android.criminalintent

import android.text.format.DateFormat
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.databinding.ListItemCrimeBinding
import java.util.*

class CrimeHolder(
    private val binding: ListItemCrimeBinding
) : RecyclerView.ViewHolder(binding.root) {

    // Viene passata una lambda expression (onCrimeClicked) in modo tale che l'Holder e l'adapter non si devono occupare del comportamento all'OnClick di un elemento,
    // ma se ne occupa il Fragment stesso
    fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit) {
        binding.crimeTitle.text = crime.title
        binding.crimeDate.text = DateFormat.format("hh:mm a dd/MM/yyyy", crime.date)
        binding.root.setOnClickListener {
            onCrimeClicked(crime.id)
        }
        binding.crimeSolved.visibility = if (crime.isSolved) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
