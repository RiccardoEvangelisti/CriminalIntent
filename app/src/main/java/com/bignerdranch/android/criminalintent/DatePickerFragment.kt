package com.bignerdranch.android.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs

class DatePickerFragment : DialogFragment() {

    // Il NavController passa il dato da visualizzare
    private val args: DatePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Imposto un listener che, quando viene settato un nuovo dato, chiama la "setFragmentResult" con il nuovo dato
        val dateListener = DatePickerDialog.OnDateSetListener {_: DatePicker, year: Int, month: Int, day: Int ->
            val resultDate = GregorianCalendar(year, month, day).time
            setFragmentResult(REQUEST_KEY_DATE, bundleOf(BUNDLE_KEY_DATE to resultDate))
        }

        val calendar = Calendar.getInstance()
        calendar.time = args.crimeDate
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    companion object {
        // Chiave che serve per associare "setFragmentResultListener" a "setFragmentResult"
        const val REQUEST_KEY_DATE = "REQUEST_KEY_DATE"
        // Chiave che serve per prelevare il valore del dato passato
        const val BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE"
    }
}