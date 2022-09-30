package com.bignerdranch.android.criminalintent

import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.databinding.ListItemCrimeBinding

class CrimeHolder(
    private val binding: ListItemCrimeBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(crime: Crime) {
        binding.crimeTitle.text = crime.title
        binding.crimeDate.text = DateFormat.format("hh:mm a dd/MM/yyyy", crime.date)
        binding.root.setOnClickListener {
            Toast.makeText(
                binding.root.context, "${crime.title} clicked!", Toast.LENGTH_SHORT
            ).show()
        }
        binding.crimeSolved.visibility = if (crime.isSolved) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
