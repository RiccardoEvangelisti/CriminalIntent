package com.bignerdranch.android.criminalintent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// An activity is an entry point into your application and is responsible for managing user interaction with a screen of information
// *OBBLIGATORIO* definirle nel manifest
// *CONVENZIONE*: nomeActivity + "Activity"
// *ATTENZIONE*: non sono un buon posto dove salvare lo stato dell'app
class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		// La chiamata al superclass è obbligatoria ed è convenzione essere la prima riga della funzione
		super.onCreate(savedInstanceState)

		// "Inflate" della View nella activity
		setContentView(R.layout.activity_main)
	}
}