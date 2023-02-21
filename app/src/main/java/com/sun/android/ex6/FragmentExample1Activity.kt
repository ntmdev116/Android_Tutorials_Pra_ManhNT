package com.sun.android.ex6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.android.databinding.ActivityFragmentExample1Binding

class FragmentExample1Activity : AppCompatActivity() {
    private val binding by lazy { ActivityFragmentExample1Binding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
