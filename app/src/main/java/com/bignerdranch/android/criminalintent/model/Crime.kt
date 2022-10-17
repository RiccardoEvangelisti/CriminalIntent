package com.bignerdranch.android.criminalintent.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

// "data class" indica che la classe serve a contenere il "model data"
// ROOM: ogni Entity definisce la struttura di una tabella, identificata da una PrimaryKey
@Entity
data class Crime(
	@PrimaryKey val id: UUID, val title: String, val date: Date, val isSolved: Boolean, val suspect: String = ""
)