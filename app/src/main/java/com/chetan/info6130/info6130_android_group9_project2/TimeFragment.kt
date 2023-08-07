package com.chetan.info6130.info6130_android_group9_project2

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
    private lateinit var animRotate: Animation
    private var season = Season.SPRING
    private var mediaPlayer: MediaPlayer? = null
    private val musicDuration = 15000L // 15 seconds
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    var timer:Timer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_time, container, false)

        wheelImage = view.findViewById(R.id.wheelImage)
        seasonImage= view.findViewById(R.id.seasonImageView)

        val currentTime = arguments?.getString(ARG_SEASON)
        timeTextView = view.findViewById(R.id.timeTextView)
        timeTextView.text = currentTime

        handler = Handler()
        startUpdatingTime()


        return view
    }

    private fun startSeasonalChanges() {
        timer = Timer(15000, object : Timer.TimerListener {
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
        timer!!.start()
    }

    private fun playMusic(musicFile: Int) {
        stopMusic()

        try {
            mediaPlayer = MediaPlayer.create(requireContext(), musicFile)
            mediaPlayer?.setOnCompletionListener {
                stopMusic()
            }

            // Adjust the volume settings (0.0f to 1.0f)
            val leftVolume = 2.0f
            val rightVolume = 2.0f
            mediaPlayer?.setVolume(leftVolume, rightVolume)

            mediaPlayer?.start()

            // Schedule stopping the music after the desired duration
            handler?.postDelayed({
                stopMusic()
            }, musicDuration)
        } catch (e: Exception) {
            // Handle any exceptions that might occur during media player operations
            e.printStackTrace()
        }
    }

    private fun stopMusic() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            } catch (e: Exception) {
                // Handle any exceptions that might occur during media player operations
                e.printStackTrace()
            }
            mediaPlayer = null
        }
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

    private fun startWheelAnimation(context: Context) {
        wheelImage = view.findViewById(R.id.wheelImage)
        if (isAdded && ::wheelImage.isInitialized) {
            animRotate = AnimationUtils.loadAnimation(context, R.anim.rotate)
            wheelImage.startAnimation(animRotate)
            Log.d("wheelImage", "Animation started")
        } else {
            Log.d("wheelImage", "Fragment not attached or wheelImage not initialized")
            Log.d("isAdded", isAdded.toString())
            Log.d("wheelImage.isInitialized", ::wheelImage.isInitialized.toString())
        }
    }

    fun startAnimations(context: Context) {
        startWheelAnimation(context)
        startSeasonalChanges()
    }

    fun stopAnimations() {
        if (::animRotate.isInitialized) {
            wheelImage.clearAnimation()
            stopMusic()
            timer?.stop()
            Log.d("wheelImage", "Wheel animation stopped")
        } else {
            Log.d("wheelImage", "Wheel animation not initialized")
        }
    }

    fun resetSeasonToSpring(context: Context) {
        season = Season.SPRING
        view?.setBackgroundResource(R.color.orange_red)
        seasonImage?.setImageResource(R.drawable.spring)
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

