package com.bignerdranch.android.criminalintent.database

import androidx.room.TypeConverter
import java.util.*

// ROOM:
// Room salva dati solo primitivi quindi è obbligatorio definire un converter per ogni dato non primitivo
class CrimeTypeConverters {

	// *ATTENZIONE* nel importare il tipo giusto. In questo caso java.util.Date diventa "fromDate" e "toDate"
	@TypeConverter
	fun fromDate(date: Date): Long {
		return date.time
	}

	@TypeConverter
	fun toDate(millisSinceEpoch: Long): Date {
		return Date(millisSinceEpoch)
	}
}