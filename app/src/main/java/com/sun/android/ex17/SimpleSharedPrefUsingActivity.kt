package com.sun.android.ex17

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sun.android.R
import com.sun.android.databinding.ActivitySimpleSharedPrefUsingBinding


class SimpleSharedPrefUsingActivity : AppCompatActivity() {
    private val getDefaultColor = { ContextCompat.getColor(this, R.color.default_background) }

    private val mPreferences by lazy { getSharedPreferences(mPreferenceString, MODE_PRIVATE) }

    private var mCount = DEFAULT_COUNT
    private var mColor = 0

    private val binding by lazy { ActivitySimpleSharedPrefUsingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListeners()
        restoreValue()
    }

    override fun onPause() {
        super.onPause()
        mPreferences.edit().apply {
            putInt(COUNT_KEY, mCount)
            putInt(COLOR_KEY, mColor)
            apply()
        }
    }

    private fun setListeners() {
        binding.countButton.setOnClickListener { countUp() }
        binding.resetButton.setOnClickListener { reset() }
        binding.blackBackgroundButton.setOnClickListener(::changeBackgroundColor)
        binding.redBackgroundButton.setOnClickListener(::changeBackgroundColor)
        binding.blueBackgroundButton.setOnClickListener(::changeBackgroundColor)
        binding.greenBackgroundButton.setOnClickListener(::changeBackgroundColor)
    }

    private fun reset() {
        mCount = DEFAULT_COUNT
        binding.countTextview.text = mCount.toString()

        mColor = getDefaultColor()
        binding.countTextview.setBackgroundColor(mColor)

        mPreferences.edit().apply {
            clear()
            apply()
        }
    }

    private fun countUp() {
        mCount++
        binding.countTextview.text = mCount.toString()
    }

    private fun changeBackgroundColor(view: View) {
        mColor = (view.background as? ColorDrawable)?.color ?: R.color.default_background
        binding.countTextview.setBackgroundColor(mColor)
    }

    private fun restoreValue() {
        mCount = mPreferences.getInt(COUNT_KEY, DEFAULT_COUNT)
        binding.countTextview.text = mCount.toString()

        mColor = mPreferences.getInt(COLOR_KEY, getDefaultColor())
        binding.countTextview.setBackgroundColor(mColor)
    }

    companion object {
        private val COUNT_KEY = "count"
        private val COLOR_KEY = "color"

        private val DEFAULT_COUNT = 0
        private val mPreferenceString = "com.sun.android.sharedprefs"
    }
}
