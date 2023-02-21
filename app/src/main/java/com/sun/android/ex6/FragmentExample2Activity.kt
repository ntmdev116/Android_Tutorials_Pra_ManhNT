package com.sun.android.ex6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sun.android.R
import com.sun.android.databinding.ActivityFragmentExample2Binding

const val STATE_FRAGMENT = "state_of_fragment"

class FragmentExample2Activity : AppCompatActivity() {
    private val binding by lazy { ActivityFragmentExample2Binding.inflate(layoutInflater) }
    private var isFragmentDisplayed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        isFragmentDisplayed = savedInstanceState?.getBoolean(STATE_FRAGMENT) ?: false

        binding.buttonOpenFragment.setOnClickListener {
            if (!isFragmentDisplayed) {
                displayFragment()
            } else {
                closeFragment()
            }
        }

        binding.buttonOpenFragment.setText(if (isFragmentDisplayed) R.string.close else R.string.open)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_FRAGMENT, isFragmentDisplayed)
        super.onSaveInstanceState(outState)
    }

    private fun displayFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(binding.fragmentContainerView.id, SimpleFragment.newInstance())
            .commit()

        binding.buttonOpenFragment.setText(R.string.close)
        isFragmentDisplayed = true
    }

    private fun closeFragment() {
        val simpleFragment = supportFragmentManager
            .findFragmentById(binding.fragmentContainerView.id) as? SimpleFragment

        simpleFragment?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }

        binding.buttonOpenFragment.setText(R.string.open)
        isFragmentDisplayed = false
    }

}
