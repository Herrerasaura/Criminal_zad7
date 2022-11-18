package com.example.criminal_intent_sav_kompfour

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/*
class CrimeListFragment : androidx.fragment.app.Fragment() {
    private val TAG = "CrimeListFragment"
*/
    class CrimeListFragment : Fragment() {

    interface Callbacks {
        fun onCrimeSelected(crimeId: Crime)
        abstract fun onDateSelected(resultDate: Date)
    }

    private var callbacks: Callbacks? = null

        private lateinit var companion: Unit
        private lateinit var crimeRecyclerView: RecyclerView
        private val crimeListViewModel = CrimeListViewModel()
        private var adapter: CrimeAdapter? =
        CrimeAdapter(emptyList())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState:
                          Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

        private inner class CrimeHolder(view: View)
            : RecyclerView.ViewHolder(view),
            View.OnClickListener {
            private lateinit var crime: Crime

            private val titleTextView: TextView =
                itemView.findViewById(R.id.crime_title)
            private val dateTextView: TextView =
                itemView.findViewById(R.id.crime_date)

            init {
                itemView.setOnClickListener(this)
            }

            fun bind(crime: Crime) {
                this.crime = crime
                titleTextView.text = this.crime.title
                dateTextView.text =
                    this.crime.date.toString()
            }

            override fun onClick(v: View) {
                callbacks?.onCrimeSelected(crime)
            }
        }

        private inner class CrimeAdapter(var crimes: List<Crime>)
        : RecyclerView.Adapter<CrimeHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CrimeHolder {
                val view =
                    layoutInflater.inflate(R.layout.list_item_crime
                        , parent, false)
                return CrimeHolder(view)
            }

            override fun getItemCount() =
                crimes.size
            override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
                val crime = crimes[position]
                holder.bind(crime)
            }
        }

        private fun updateUI(crimes: List<Crime>) {
            adapter = CrimeAdapter(crimes)
            crimeRecyclerView.adapter = adapter
        }

            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {
                val view =
                    inflater.inflate(R.layout.fragment_crime_list,
                        container, false)

                val crimeRecyclerView =
                    view.findViewById(R.id.crime_recycler_view) as RecyclerView
                crimeRecyclerView.layoutManager =
                    LinearLayoutManager(context)

                crimeRecyclerView.adapter = adapter

                return view

            }

         override fun onViewCreated(view: View,
                               savedInstanceState: Bundle?) {
        super.onViewCreated(view,
            savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes${crimes.size}")
                    updateUI(crimes)
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item:
                                       MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                val crime = Crime(UUID.randomUUID(), "", Date(), "", false)
                crimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crime)
                true
            }
            else -> return  super.onOptionsItemSelected(item)
        }
    }

        }
        /*companion object {
            fun newInstance(): CrimeListFragment {
                return CrimeListFragment()
            }
        }*/

//}
