package com.bignerdranch.android.criminalintent.database

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.criminalintent.model.Crime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

private const val DATABASE_NAME = "crime-database"

// REPOSITORY PATTERN:
// 1) una Repository incapsula la logica per accedere alle risorse (uno o più database ad esempio)
// 2) mette a disposizione della UI i metodi per effettuare le chiamate alle risorse
// *OBBLIGATORIO*: implementarlo come Singleton e inizializzarlo all'inizio dell'applicazione
class CrimeRepository private constructor(context: Context, private val coroutineScope: CoroutineScope = GlobalScope) {

	// Singleton
	companion object {

		private var INSTANCE: CrimeRepository? = null
		fun initialize(context: Context) {
			if (INSTANCE == null) {
				INSTANCE = CrimeRepository(context)
			}
		}

		fun get(): CrimeRepository {
			return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
		}
	}

	// Implementazione concreta del database
	private val database: CrimeDatabase =
		// crea una concreta implementazione del database
		Room.databaseBuilder(context.applicationContext, CrimeDatabase::class.java, DATABASE_NAME)/*.createFromAsset(DATABASE_NAME)*/.build()

	// Implementazione di tutte le funzioni
	fun getCrimes(): Flow<List<Crime>> {
		return database.crimeDao().getCrimes()
	}

	suspend fun getCrime(id: UUID): Crime {
		return database.crimeDao().getCrime(id)
	}

	fun updateCrime(crime: Crime) {
		// Viene utilizzato il GlobalScope
		coroutineScope.launch {
			database.crimeDao().updateCrime(crime)
		}
	}

	suspend fun addCrime(crime: Crime) {
		database.crimeDao().addCrime(crime)
	}
}