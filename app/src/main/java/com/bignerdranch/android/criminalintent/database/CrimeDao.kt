package com.bignerdranch.android.criminalintent.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.bignerdranch.android.criminalintent.model.Crime
import kotlinx.coroutines.flow.Flow
import java.util.*

// ROOM:
// 1) il DAO contiene una funzione per ciascuna operazione del database
// 2) il DAO si occupa di scrivere l'effettivo codice SQL se necessario (ad es Query)
// 2) Room si occupa di implementare le funzioni
@Dao
interface CrimeDao {

	// Query si usa per prelevare i dati dal database
	@Query("SELECT * FROM crime")
	fun getCrimes(): Flow<List<Crime>>

	@Query("SELECT * FROM crime WHERE id=(:id)")
	suspend fun getCrime(id: UUID): Crime

	// Update
	@Update
	fun updateCrime(crime: Crime)
}