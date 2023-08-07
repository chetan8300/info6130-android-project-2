package com.chetan.info6130.info6130_android_group9_project2

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.Image
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
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
    private lateinit var wheelImage: ImageView
    private lateinit var seasonImage: ImageView
    private lateinit var timeTextView: TextView
    private lateinit var currentTime: String
    private var wheelAnimation: Animation? = null
    private var wheelAnimationStarted = false
    private lateinit var animRotate: Animation
    private var season = Season.SPRING
    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    private fun updateTextView() {
        val currentTime = ARG_SEASON
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

        animRotate = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
        wheelImage = view.findViewById(R.id.wheelImage)
        seasonImage= view.findViewById(R.id.seasonImageView)

        val currentTime = arguments?.getString(ARG_SEASON)
        timeTextView = view.findViewById(R.id.timeTextView)
        timeTextView.text = currentTime

        handler = Handler()
        startUpdatingTime()
        startWheelAnimation()
        startSeasonalChanges()

        return view
    }

    private fun startSeasonalChanges() {
        val timer = Timer(15000, object : Timer.TimerListener {
            override fun onTick() {
                // Change the season and update background and other elements
                when (season) {
                    Season.SPRING -> {
                        view.setBackgroundResource(R.color.orange_red)
                        seasonImage.setImageResource(R.drawable.spring)
                        playMusic(R.raw.spring_song)
                        season = Season.SUMMER
                    }
                    Season.SUMMER -> {
                        view.setBackgroundResource(R.color.dark_sea_green)
                        seasonImage.setImageResource(R.drawable.summer)
                        playMusic(R.raw.summer_song)
                        season = Season.AUTUMN
                    }
                    Season.AUTUMN -> {
                        view.setBackgroundResource(R.color.yellow)
                        seasonImage.setImageResource(R.drawable.autumn)
                        playMusic(R.raw.autumn_song)
                        season = Season.WINTER
                    }
                    Season.WINTER -> {
                        view.setBackgroundResource(R.color.white)
                        seasonImage.setImageResource(R.drawable.winter)
                        playMusic(R.raw.winter_song)
                        season = Season.SPRING
                    }
                }
            }
        })
        timer.start()
    }

    private fun playMusic(musicFile: Int) {

    }



    private enum class Season {
        SPRING, SUMMER, AUTUMN, WINTER
    }

    class Timer(private val intervalMillis: Long, private val listener: TimerListener) {

        private val handler = Handler()
        private var runnable: Runnable? = null

        fun start() {
            runnable = object : Runnable {
                override fun run() {
                    listener.onTick()
                    handler.postDelayed(this, intervalMillis)
                }
            }
            handler.post(runnable!!)
        }

        fun stop() {
            handler.removeCallbacks(runnable!!)
        }

        interface TimerListener {
            fun onTick()
        }
    }

    private fun getFormattedTime(): String {
        val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        return sdf.format(Date())
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

//    fun startWheelAnimation(){
//        if(!::wheelImage.isInitialized){
//            return
//        }
//        wheelAnimation = RotateAnimation(
//            0f, 360f,
//            Animation.RELATIVE_TO_SELF, 0.5f,
//            Animation.RELATIVE_TO_SELF, 0.5f
//        ).apply {
//            duration = 1000 // Set the animation duration (in milliseconds)
//            repeatCount = Animation.INFINITE // Repeat the animation infinitely
//            interpolator = LinearInterpolator() // Use a linear interpolator for smooth rotation
//        }
//        // Start the animation
////        wheelImage.startAnimation(wheelAnimation)
//        wheelAnimation?.start()
//    }

    fun startWheelAnimation(){
//        if (isAdded && ::wheelImage.isInitialized) {
//            wheelAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
//            wheelImage.startAnimation(wheelAnimation)
//            wheelAnimationStarted = true
//        }
//        if (::wheelImage.isInitialized) {
//            val rotateAnimation = RotateAnimation(
//                0f, 360f,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f
//            ).apply {
//                duration = 1000 // Set the animation duration (in milliseconds)
//                repeatCount = Animation.INFINITE // Repeat the animation infinitely
//                interpolator = LinearInterpolator() // Use a linear interpolator for smooth rotation
//            }
//            Log.d("milit", "Animation started")
//            wheelImage.startAnimation(rotateAnimation)
//        }

//        if (::wheelImage.isInitialized) {
//            wheelImage.animate()
//                .rotationBy(360f)
//                .setDuration(1000)
//                .setInterpolator(LinearInterpolator())
//                .withEndAction { startWheelAnimation() }
//            Log.d("milit", "Animation started")
//        }
        wheelImage.startAnimation(animRotate)
        Log.d("milit", "Animation started")
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
        fun newInstance(season: String) =
            TimeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SEASON, season)
                }
            }
        private const val ARG_SEASON = "arg_season"
    }
}

