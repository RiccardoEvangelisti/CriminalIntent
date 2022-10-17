package com.bignerdranch.android.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.criminalintent.model.Crime

// ROOM:
// 1) l'annotazione Database indica che questa classe rappresenta il database
// 2) viene specificata una lista di entita'
// 3) viene speficiata la versione del database
// *ATTENZIONE*: modificare la versione del database quando si modificano le entità o ne si aggiungono/tolgono
// *OBBLIGATORIO* definire un converter se le entità possiedono dati non primitivi
@Database(entities = [Crime::class], version = 2)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {

	// Definire un DAO astratto
	abstract fun crimeDao(): CrimeDao
}

// MIGRAZIONE dalla versione 1 alla versione 2 del database. Deve essere chiamata nel Repository quando il database viene allocato
val migration_1_2 = object : Migration(1, 2) {
	override fun migrate(database: SupportSQLiteDatabase) {
		database.execSQL("ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''")
	}
}
