package com.bignerdranch.android.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.criminalintent.model.Crime

// ROOM:
// 1) l'annotazione Database indica che questa classe rappresenta il database
// 2) viene specificata una lista di entita'
// 3) viene speficiata la versione del database
// *ATTENZIONE*: modificare la versione del database quando si modificano le entità o ne si aggiungono/tolgono
// *OBBLIGATORIO* definire un converter se le entità possiedono dati non primitivi
@Database(entities = [Crime::class], version = 1)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {

	// Definire un DAO astratto
	abstract fun crimeDao(): CrimeDao
}
