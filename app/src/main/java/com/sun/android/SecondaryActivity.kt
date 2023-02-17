package com.sun.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SecondaryActivity : AppCompatActivity() {
    private val editText : EditText by lazy { findViewById(R.id.editText_second) }
    companion object {
        const val EXTRA_REPLY = "com.sun.android.extra.REPLY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)
        val intent = intent

        val message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE)
        val textView : TextView = findViewById(R.id.text_message)
        textView.text = message
    }

    // call back for reply button
    fun returnReply(view: View) {
        // Get EditText text
        val mReply = editText.text.toString()

        // put extra in intent
        val intent = Intent()
        intent.putExtra(EXTRA_REPLY, mReply)
        intent.putExtra(MainActivity.REQUEST_CODE, MainActivity.TEXT_REQUEST)

        // set result
        setResult(RESULT_OK, intent)
        // finish to start main activity
        finish()
    }
}
