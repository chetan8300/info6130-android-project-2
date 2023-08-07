package com.chetan.info6130.info6130_android_group9_project2

import android.animation.AnimatorSet
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import java.text.DateFormat
import java.util.Date

class NameFragment : Fragment() {

    private lateinit var view: View
    private lateinit var cloudImage: ImageView
    private lateinit var sunImage: ImageView
    private lateinit var birdsImage: ImageView
    private lateinit var colorAnimator: ValueAnimator
    private lateinit var cloudAnimator: AnimatorSet
    private lateinit var sunAnimator: AnimatorSet
    private lateinit var birdsAnimator: AnimatorSet
    private lateinit var animRotate: Animation
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var timeFragment: TimeFragment

//    interface AnimationListener {
//        fun onStartButtonClicked()
//    }

    private lateinit var animationListener: AnimationListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AnimationListener) {
            animationListener = context
        } else {
            throw RuntimeException("$context must implement AnimationListener")
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        view = inflater.inflate(R.layout.fragment_name, container, false)


        cloudImage = view.findViewById(R.id.cloudImage)
        sunImage = view.findViewById(R.id.sunImage)
        birdsImage = view.findViewById(R.id.birdsImage)
//        wheelImage = view.findViewById(R.id.wheelImage)

        val startButton = view.findViewById<Button>(R.id.startButton)
        val stopButton = view.findViewById<Button>(R.id.stopButton)
        val currentTime = getCurrentTime()

        passCurrentTime(currentTime)

        // Apply the initial animation
        colorAnimator = animateBackgroundColor()

        startButton.setOnClickListener {
            startAnimations()
        }

        stopButton.setOnClickListener {
            stopAnimations()
        }

        val cloudWidth = resources.displayMetrics.density * 180
        val cloudSequential = true
        cloudAnimator = initTranslateAnimator(cloudImage, cloudWidth, 3000, cloudSequential)

        val sunWidth = resources.displayMetrics.density * 90
        val sunSequential = true
        sunAnimator = initTranslateAnimator(sunImage, sunWidth, 2700, sunSequential)

        val birdsWidth = resources.displayMetrics.density * 180
        val birdsSequential = false
        birdsAnimator = initTranslateAnimator(birdsImage, birdsWidth, 3500, birdsSequential)
        return view
    }

    private fun startAnimations() {
        colorAnimator.start()
        cloudAnimator.start()
        sunAnimator.start()
        birdsAnimator.start()
        animationListener.onStartButtonClicked()
    }

    private fun stopAnimations() {
        colorAnimator.cancel()
        cloudAnimator.cancel()
        sunAnimator.cancel()
        birdsAnimator.cancel()
        animationListener.onStopButtonClick()
    }

    @SuppressLint("ResourceType")
    private fun animateBackgroundColor(): ValueAnimator {
        val colorAnim: ValueAnimator = ObjectAnimator.ofInt(
            view, "backgroundColor",
            Color.parseColor("#4ba4d6"),
            Color.parseColor("#4C7CF5"),
        )
        colorAnim.duration = 2000
        colorAnim.repeatCount = ValueAnimator.INFINITE
        colorAnim.repeatMode = ValueAnimator.REVERSE
        colorAnim.setEvaluator(ArgbEvaluator())
        return colorAnim
    }

    private fun initTranslateAnimator(image: ImageView, imageWidth: Float, duration: Long, isSequential: Boolean): AnimatorSet {
        val screenWidth = resources.displayMetrics.widthPixels
        var maxTranslation = screenWidth - imageWidth

        var startFrom = 0F

        if (!isSequential) {
            maxTranslation = screenWidth.toFloat()
            startFrom = -imageWidth
        }

        val translateFromLeft = ObjectAnimator.ofFloat(image, "translationX", startFrom, maxTranslation)
        translateFromLeft.duration = duration
        if (isSequential) {
            translateFromLeft.repeatMode = ValueAnimator.REVERSE
        } else {
            translateFromLeft.repeatMode = ValueAnimator.RESTART
        }

        translateFromLeft.repeatCount = ValueAnimator.INFINITE

        // Create an AnimatorSet to play animations sequentially
        val translateAnimator = AnimatorSet()
        if (isSequential) {
            val translateFromRight = ObjectAnimator.ofFloat(image, "translationX", maxTranslation, 0f)
            translateFromRight.duration = duration
            translateFromRight.repeatMode = ValueAnimator.REVERSE
            translateFromRight.repeatCount = ValueAnimator.INFINITE

            translateAnimator.playSequentially(translateFromLeft, translateFromRight)
        } else {
            translateAnimator.playSequentially(translateFromLeft)
        }


        return translateAnimator
    }

    private fun getCurrentTime(): String {
        val currentTime = Date()
        val dateFormat: DateFormat = DateFormat.getTimeInstance(DateFormat.LONG)
        return dateFormat.format(currentTime)
    }

    private fun passCurrentTime(season: String) {
        val timeFragment = TimeFragment.newInstance(season)
        parentFragmentManager.beginTransaction().replace(R.id.timeFrameLayout, timeFragment).commit()
    }

}