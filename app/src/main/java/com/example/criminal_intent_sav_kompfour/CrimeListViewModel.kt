package com.example.criminal_intent_sav_kompfour

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {
    fun addCrime(crime: Crime) {

    }

    private val crimeRepository = CrimeRepository.get()
    val crimeListLiveData = crimeRepository.getCrimes()
}