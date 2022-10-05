package com.bignerdranch.android.criminalintent

import android.app.Application
import com.bignerdranch.android.criminalintent.database.CrimeRepository

// *OBBLIGATORIO* inserire 'android:name=".CriminalIntentApplication"' come attributo di <application> nel manifest
class CriminalIntentApplication : Application() {

	override fun onCreate() {
		super.onCreate()
		CrimeRepository.initialize(this)
	}
}