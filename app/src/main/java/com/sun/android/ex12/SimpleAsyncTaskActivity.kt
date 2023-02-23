package com.sun.android.ex12

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sun.android.R
import com.sun.android.databinding.ActivitySimpleAsyncTaskBinding
import kotlinx.coroutines.*

class SimpleAsyncTaskActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySimpleAsyncTaskBinding.inflate(layoutInflater) }

    private var isTaskRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonStart.setOnClickListener { if (!isTaskRunning) doAsyncTask() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TEXT_STATE, binding.textMessage.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        binding.textMessage.text = savedInstanceState.getString(TEXT_STATE)

        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun doAsyncTask() {
        val startTime = System.currentTimeMillis()
        var remainingTime = WAIT_TIME - (System.currentTimeMillis() - startTime)

        lifecycleScope.launch {
            onPreExecute()

            val result = withContext(Dispatchers.IO) {
                while (isActive && remainingTime > 0) {
                    remainingTime -= PROGRESS_UPDATE_INTERVAL

                    withContext(Dispatchers.Main) {
                        binding.progressbar.progress = (WAIT_TIME - remainingTime).toInt()
                    }
                    delay(PROGRESS_UPDATE_INTERVAL)
                }
                getString(R.string.text_result_simple_async_task, WAIT_TIME.toString())
            }

            binding.textMessage.text = result
            isTaskRunning = false
            binding.buttonStart.isEnabled = true
        }
    }

    private fun onPreExecute() {
        isTaskRunning = true
        binding.buttonStart.isEnabled = false
        binding.progressbar.max = WAIT_TIME.toInt()
        binding.progressbar.progress = 0
        binding.textMessage.text = getString(R.string.napping)
    }

    companion object {
        private val WAIT_TIME = 5000L
        private val PROGRESS_UPDATE_INTERVAL = 100L

        private val TEXT_STATE = "text_state"
    }
}
