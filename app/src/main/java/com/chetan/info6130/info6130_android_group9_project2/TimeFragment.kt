package com.chetan.info6130.info6130_android_group9_project2

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [TimeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimeFragment : Fragment() {

    private lateinit var view: View
    private lateinit var timeTextView: TextView
    private lateinit var currentTime: String

    private var handler: Handler? = null
    private var runnable: Runnable? = null

    private fun updateTextView() {
        val currentTime = ARG_CURRENT_TIME
        timeTextView.text = currentTime
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_time, container, false)

        val currentTime = arguments?.getString(ARG_CURRENT_TIME)
        timeTextView = view.findViewById(R.id.timeTextView)
        timeTextView.text = currentTime

        handler = Handler()
        startUpdatingTime()

        return view
    }

    private fun startUpdatingTime() {
        runnable = object : Runnable {
            override fun run() {
                updateTime()
                handler?.postDelayed(this, 1000) // Update every 1 second
            }
        }
        handler?.post(runnable as Runnable)
    }

    private fun updateTime() {
        val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        val currentTime = sdf.format(Date())
        timeTextView.text = currentTime
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TimeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(currentTime: String) =
            TimeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CURRENT_TIME, currentTime)
                }
            }
        private const val ARG_CURRENT_TIME = "arg_current_time"
    }
}

