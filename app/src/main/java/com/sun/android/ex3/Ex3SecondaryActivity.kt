package com.sun.android.ex3

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sun.android.databinding.ActivitySecondaryBinding


class Ex3SecondaryActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySecondaryBinding.inflate(layoutInflater) }

    companion object {
        const val EXTRA_REPLY = "com.sun.android.extra.REPLY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent = intent
        val message = intent.getStringExtra(Ex3MainActivity.EXTRA_MESSAGE)

        binding.textMessage.text = message
    }

    // call back for reply button
    fun returnReply(view: View) {
        val mReply = binding.editTextSecond.text.toString()

        val intent = Intent()
        intent.putExtra(EXTRA_REPLY, mReply)
        intent.putExtra(Ex3MainActivity.REQUEST_CODE, Ex3MainActivity.TEXT_REQUEST)

        setResult(RESULT_OK, intent)
        // finish to start main activity
        finish()
    }
}
