package com.bignerdranch.android.criminalintent.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.criminalintent.database.CrimeRepository
import com.bignerdranch.android.criminalintent.model.Crime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

// ViewModel:
// 1) si occupa di prelevare il dato dalle Repositories (es. database) e metterlo a disposizione per la View
// 2) viene associato ("scoped") ad una activity o ad un fragment, ossia si lega al suo lifecycle

// *ATTENZIONE*: Costruttore possibile solo grazie alla Factory
class CrimeDetailViewModel(crimeId: UUID) : ViewModel() {

	private val crimeRepository = CrimeRepository.get()

	// Se l'utente modifica il dato, la UI rimane sempre aggiornata grazie allo State Flow
	// La UI riceve sempre una versione read-only del dato state flow
	private val _crime: MutableStateFlow<Crime?> = MutableStateFlow(null)
	val crime: StateFlow<Crime?> = _crime.asStateFlow()

	// Il ViewModel si occupa di prelevare i dati appena viene istanziato
	init {
		viewModelScope.launch {
			_crime.value = crimeRepository.getCrime(crimeId)
		}
	}

	fun updateCrime(onUpdate: (Crime) -> Crime) {
		_crime.update { oldCrime ->
			oldCrime?.let { onUpdate(it) }
		}
	}

	// Esegue la update quando ViewModel viene distrutto, ad esempio quando si naviga via dal Fragment annesso
	override fun onCleared() {
		super.onCleared()
		crime.value?.let { crimeRepository.updateCrime(it) }
	}
}

// Factory che permette di generare un ViewModel con argomenti di ingresso
class CrimeDetailViewModelFactory(
	private val crimeId: UUID
) : ViewModelProvider.Factory {

	override fun <T : ViewModel> create(modelClass: Class<T>): T = CrimeDetailViewModel(crimeId) as T
}