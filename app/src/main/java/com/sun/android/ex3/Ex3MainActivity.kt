package com.sun.android.ex3

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.sun.android.databinding.ActivityMainBinding


class Ex3MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    companion object {
        const val TEXT_REQUEST = 1
        const val EXTRA_MESSAGE = "com.sun.android.extra.MESSAGE"
        const val REQUEST_CODE = "RequestCode"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {

            // Handle the result here
            val data: Intent? = result.data
            val requestCode = data?.getIntExtra(REQUEST_CODE, 0)

            if (requestCode == TEXT_REQUEST)
            {
                val reply = data.getStringExtra(Ex3SecondaryActivity.EXTRA_REPLY)
                binding.textHeaderReply.apply { visibility = View.VISIBLE }
                binding.textMessageReply.apply {
                    visibility = View.VISIBLE
                    text = reply
                }
            }
        }
    }

    // call back for send button
    fun launchSecondaryActivity(view: View) {
        val message : String = binding.editTextMain.text.toString()

        val intent = Intent(this, Ex3SecondaryActivity::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)

        startForResult.launch(intent)

    }
}


















