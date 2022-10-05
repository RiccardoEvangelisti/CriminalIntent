package com.bignerdranch.android.criminalintent.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.criminalintent.database.CrimeRepository
import com.bignerdranch.android.criminalintent.model.Crime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CrimeListViewModel : ViewModel() {

	private val crimeRepository = CrimeRepository.get()

	// Viene fornita all'esterno una versione read only di crimes
	private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())
	val crimes: StateFlow<List<Crime>>
		get() = _crimes.asStateFlow()

	init {
		// lancio una coroutine
		viewModelScope.launch {
			crimeRepository.getCrimes().collect { crimes -> _crimes.value = crimes }
		}
	}
}