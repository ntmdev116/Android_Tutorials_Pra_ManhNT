package com.sun.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val LOG_TAG = MainActivity::class.java.simpleName

    companion object {
        const val EXTRA_MESSAGE = "com.sun.android.extra.MESSAGE"
    }

    private val editText : EditText by lazy { findViewById(R.id.editText_main) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun launchSecondaryActivity(view: View) {
        val message : String = editText.text.toString()
        Log.d(LOG_TAG, "Button clicked!");
        val intent = Intent(this, SecondaryActivity::class.java)

        intent.putExtra(EXTRA_MESSAGE, message)
        startActivity(intent)
    }
}
