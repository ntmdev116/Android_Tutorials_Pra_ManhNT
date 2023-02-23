package com.sun.android.ex8

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.sun.android.R
import com.sun.android.databinding.ActivityScoreKeeperBinding

const val STATE_SCORE_1 = "Team 1 Score"
const val STATE_SCORE_2 = "Team 2 Score"

class ScoreKeeperActivity : AppCompatActivity() {
    private val binding by lazy { ActivityScoreKeeperBinding.inflate(layoutInflater) }

    private var mScore1 = 0
    private var mScore2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListener()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_SCORE_1, mScore1)
        outState.putInt(STATE_SCORE_2, mScore2)

        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.night_mode_menu, menu)

        val nightMode = AppCompatDelegate.getDefaultNightMode()

        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            menu?.findItem(R.id.night_mode)?.setTitle(R.string.day_mode)
        } else {
            menu?.findItem(R.id.night_mode)?.setTitle(R.string.night_mode)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.night_mode) {
            val nightMode = AppCompatDelegate.getDefaultNightMode()

            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        return true
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.let {
            mScore1 = savedInstanceState.getInt(STATE_SCORE_1)
            mScore2 = savedInstanceState.getInt(STATE_SCORE_2)

            binding.score1.text = mScore1.toString()
            binding.score2.text = mScore2.toString()
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun setListener() {
        binding.decreaseTeam1.setOnClickListener(::decreaseScore)
        binding.decreaseTeam2.setOnClickListener(::decreaseScore)
        binding.increaseTeam1.setOnClickListener(::increaseScore)
        binding.increaseTeam2.setOnClickListener(::increaseScore)
    }

    private fun decreaseScore(view: View) {
        when (view.id) {
            binding.decreaseTeam1.id -> {
                mScore1--
                binding.score1.text = mScore1.toString()
            }
            binding.decreaseTeam2.id -> {
                mScore2--
                binding.score2.text = mScore2.toString()
            }
        }
    }

    private fun increaseScore(view: View) {
        when (view.id) {
            binding.increaseTeam1.id -> {
                mScore1++
                binding.score1.text = mScore1.toString()
            }
            binding.increaseTeam2.id -> {
                mScore2++
                binding.score2.text = mScore2.toString()
            }
        }
    }
}
