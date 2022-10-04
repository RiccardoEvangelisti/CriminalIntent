package com.bignerdranch.android.criminalintent

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.criminalintent.database.CrimeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

private const val DATABASE_NAME = "crime-database"

// Singleton
class CrimeRepository private constructor(context: Context, private val coroutineScope: CoroutineScope = GlobalScope) {

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

	// Crea una implementazione concreta del database
	private val database: CrimeDatabase =
		Room.databaseBuilder(context.applicationContext, CrimeDatabase::class.java, DATABASE_NAME).createFromAsset(DATABASE_NAME).build()

	fun getCrimes(): Flow<List<Crime>> = database.crimeDao().getCrimes()
	suspend fun getCrime(id: UUID): Crime = database.crimeDao().getCrime(id)
	fun updateCrime(crime: Crime) {
		// Viene utilizzato il GlobalScope
		coroutineScope.launch {
			database.crimeDao().updateCrime(crime)
		}
	}
}