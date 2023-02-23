package com.sun.android.ex10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.sun.android.adapters.SimpleViewPagerAdapter
import com.sun.android.databinding.ActivityTabExperimentBinding
import com.sun.android.utils.Constants

class TabExperimentActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTabExperimentBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        createTabLayout()
    }

    private fun createTabLayout() {
        val adapter = SimpleViewPagerAdapter(supportFragmentManager, lifecycle).apply {
            setTabList(mutableListOf(TabFragment1(), TabFragment2(), TabFragment3()))
        }
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> Constants.FIRST_TAB_NAME
                1 -> Constants.SECOND_TAB_NAME
                2 -> Constants.THRIRD_TAB_NAME
                else -> throw IllegalArgumentException("Invalid position $position")
            }
        }.attach()
    }
}
