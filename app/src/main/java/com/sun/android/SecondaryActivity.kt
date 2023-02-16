package com.sun.android

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SecondaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)
        val intent = intent

        val message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE)
        Log.d("DDD", message.toString())
        val textView : TextView = findViewById(R.id.text_message)
        textView.text = message
    }
}
