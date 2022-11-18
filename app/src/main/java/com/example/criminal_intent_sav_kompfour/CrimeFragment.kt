package com.example.criminal_intent_sav_kompfour

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.util.*
import java.lang.String.format
import java.text.DateFormat
import java.text.MessageFormat.format
import android.icu.text.MessageFormat.format


private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
private const val DATE_FORMAT = "EEE, MMM, dd"

class CrimeFragment : Fragment()
    //DatePickerFragment.Callbacks
{
    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var twoDateButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var sendReport: Button
    private lateinit var chooseSuspect: Button
    private lateinit var call: Button

    private val crimeDetailViewModel:
            CrimeDetailViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime(UUID.randomUUID(), "", Date(), "", false);
        val crimeId: UUID =
            arguments?.getSerializable(ARG_CRIME_ID) as
                    UUID
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        sendReport = view.findViewById(R.id.send_report) as Button
        chooseSuspect = view.findViewById(R.id.choose_suspect) as Button
        call = view.findViewById(R.id.call) as Button


        fun onViewCreated(view: View,
                                   savedInstanceState: Bundle?) {
            super.onViewCreated(view,
                savedInstanceState)
            val crimeDetailViewModel = CrimeDetailViewModel()
            crimeDetailViewModel.crimeLiveData.observe(
                viewLifecycleOwner, androidx.lifecycle.Observer {  })
        }


        return view
    }

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()
        chooseSuspect.text = crime.date.toString()
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    
    override fun onActivityResult(requestCode:
                                  Int, resultCode: Int, data: Intent?) {
        when{
            resultCode != Activity.RESULT_OK -> {
                return (requestCode == REQUEST_DATE && data != null) {

                    val contactUri: Uri? = data?.data
                    // Указать, для каких полей ваш запрос должен возвращать значения.
                    val queryFields =
                        arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                    // Выполняемый здесь запрос — contactUri похож на предложение "where"
                    val cursor =
                        contactUri?.let {
                            requireActivity().contentResolver.query(
                                it,
                                queryFields, null, null, null
                            )
                        }
                    cursor?.use {
                        // Verify cursor contains at least one result
                        if (it.count == 0) return@use

                        // Первый столбец первой строки данных —
                        // это имя вашего подозреваемого.
                        it.moveToFirst()
                        val suspect = it.getString(0)
                        crime.suspect = suspect
                    }
                }
            }
        }

    }
    


    private fun getCrimeReport(): String {
        val solvedString = if (crime.isSolved)
        {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved) }

        val dateString = (crime.date).toString()
        var suspect = if(crime.suspect as Boolean) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }

        return getString(R.string.crime_report,
            crime.title, dateString,
            solvedString, suspect)
    }

    override fun onStart() {
        super.onStart()
        val titleWathcer = object : TextWatcher
        {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        //titleField.addTextChangedListener(titleWatcher)
        solvedCheckBox.apply {
            setOnCheckedChangeListener()
            {_, isChecked -> crime.isSolved = isChecked
            }
        }
        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(crime.date).apply()
            {

            }
        }
        sendReport.setOnClickListener{
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT,
                    getCrimeReport())
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    getString(R.string.crime_report_subject))
            }.also { intent ->
                val chooserIntent =
                    Intent.createChooser(intent,
                        getString(R.string.send_report))
                startActivity(chooserIntent)
            }
        }
        chooseSuspect.apply {
            val pickContactIntent =
                Intent(Intent.ACTION_PICK,
                    ContactsContract.Contacts.CONTENT_URI)

            setOnClickListener {
                startActivityForResult(pickContactIntent, REQUEST_DATE)
            }
            val packageManager: PackageManager =
                requireActivity().packageManager
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(pickContactIntent,
                    PackageManager.MATCH_DEFAULT_ONLY)
        }

        call.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:5551234"))
            startActivity(intent)
        }


        twoDateButton.setOnClickListener {
            DatePickerFragment.newInstance(crime.date).apply()
            {

            }
        }
    }

    private fun startActivity(intent: String) {

    }


    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    fun onDateSelected(date: Date) {
        crime.date = date
        updateUI()
    }

    companion object {

        fun newInstance(crimeId: Crime):
                CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }
}

private operator fun Boolean.invoke(value: Any) {

}


