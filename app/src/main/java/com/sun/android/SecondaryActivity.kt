package com.sun.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sun.android.databinding.ActivitySecondaryBinding


class SecondaryActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySecondaryBinding

    companion object {
        const val EXTRA_REPLY = "com.sun.android.extra.REPLY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE)

        binding.textMessage.text = message
    }

    // call back for reply button
    fun returnReply(view: View) {
        val mReply = binding.editTextSecond.text.toString()

        val intent = Intent()
        intent.putExtra(EXTRA_REPLY, mReply)
        intent.putExtra(MainActivity.REQUEST_CODE, MainActivity.TEXT_REQUEST)

        setResult(RESULT_OK, intent)
        // finish to start main activity
        finish()
    }
}
