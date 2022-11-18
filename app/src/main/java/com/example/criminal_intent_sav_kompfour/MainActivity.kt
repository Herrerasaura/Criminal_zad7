package com.example.criminal_intent_sav_kompfour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

//private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(),
    CrimeListFragment.Callbacks
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null)
        {
            val fragment = CrimeListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onCrimeSelected(crimeId: Crime)
    {
        val fragment =
            CrimeFragment.newInstance(crimeId)
        val commit = supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                fragment
            )
            .commit()
    }

    override fun onDateSelected(resultDate: Date) {
        TODO("Not yet implemented")
    }
}
