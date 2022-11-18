package com.example.criminal_intent_sav_kompfour

import android.app.Application

class CriminalIntentApplication : Application()
{

    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}