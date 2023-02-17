package com.sun.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val LOG_TAG = MainActivity::class.java.simpleName

    // using registerForActivityResult because startActivityForResult is deprecated
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {

            // Handle the result here
            val data: Intent? = result.data
            val requestCode = data?.getIntExtra(REQUEST_CODE, 0)
            Log.d("DDDD", requestCode.toString())

            if (requestCode == TEXT_REQUEST)
            {
                val reply = data.getStringExtra(SecondaryActivity.EXTRA_REPLY)
                Log.d("DDDD", reply.toString())
                mReplyHeadTextView.apply { visibility = View.VISIBLE }
                mReplyTextView.apply {
                    visibility = View.VISIBLE
                    text = reply
                }
            }
        }
    }

    companion object {
        const val TEXT_REQUEST = 1
        const val EXTRA_MESSAGE = "com.sun.android.extra.MESSAGE"
        const val REQUEST_CODE = "RequestCode"
    }

    private val editText : EditText by lazy { findViewById(R.id.editText_main) }
    private val mReplyHeadTextView: TextView by lazy { findViewById(R.id.text_header_reply) }
    private val mReplyTextView: TextView by lazy { findViewById(R.id.text_message_reply) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // call back for send button
    fun launchSecondaryActivity(view: View) {
        val message : String = editText.text.toString()
        Log.d(LOG_TAG, "Button clicked!");
        val intent = Intent(this, SecondaryActivity::class.java)

        intent.putExtra(EXTRA_MESSAGE, message)

        startForResult.launch(intent)

    }
}


















