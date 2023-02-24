package com.sun.android.ex13

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.sun.android.R
import com.sun.android.databinding.ActivitySimpleApiUsingBinding

class SimpleApiUsingActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySimpleApiUsingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener(::doTask)
    }

    private fun doTask(view: View) {
        val searchString = binding.bookInput.text.toString()

        FetchBook(lifecycleScope)
            .fetch(
                searchString,
                _onPreExecute = {},
                _onPostExecute = { title, authors ->
                    binding.titleText.text = title ?: getString(R.string.no_results)
                    binding.authorText.text = authors ?: ""
                })
    }
}
