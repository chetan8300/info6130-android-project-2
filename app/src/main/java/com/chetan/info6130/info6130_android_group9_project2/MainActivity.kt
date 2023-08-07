package com.chetan.info6130.info6130_android_group9_project2

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

interface AnimationListener {
    fun onStartButtonClicked()
    fun onStopButtonClick()
}
class MainActivity : AppCompatActivity(), AnimationListener {

    private lateinit var nameFragment: NameFragment
    private lateinit var timeFragment: TimeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameFragment = NameFragment()
        supportFragmentManager.beginTransaction().replace(R.id.nameFrameLayout, nameFragment).commit()

        timeFragment = TimeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.timeFrameLayout, timeFragment).commit()
    }

    override fun onStartButtonClicked() {
        val timeFragment = supportFragmentManager.findFragmentById(R.id.timeFrameLayout) as? TimeFragment
        timeFragment?.startAnimations(this)
        timeFragment?.resetSeasonToSpring(this)
    }

    override fun onStopButtonClick() {
        val timeFragment = supportFragmentManager.findFragmentById(R.id.timeFrameLayout) as? TimeFragment
        timeFragment?.stopAnimations()
    }
}