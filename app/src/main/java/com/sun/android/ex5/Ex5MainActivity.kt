package com.sun.android.ex5

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.sun.android.databinding.ActivityEx4MainBinding


class Ex5MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEx4MainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.buttonWebsite.setOnClickListener(::openWebsite)
        binding.buttonLocation.setOnClickListener(::openLocation)
        binding.buttonShare.setOnClickListener(::openShare)
    }

    private fun openWebsite(view : View) {
        val url = binding.editTextWebsite.text.toString()
        val webpage = Uri.parse(url)

        openUri(webpage)
    }

    private fun openLocation(view : View) {
        val loc = binding.editTextLocation.text.toString()
        val uri = Uri.parse("geo:0,0?q=$loc");

        openUri(uri)
    }

    private fun openShare(view : View) {
        val txt = binding.editTextShare.text.toString()
        val mimeType = "text/plain"
        ShareCompat.IntentBuilder
            .from(this)
            .setType(mimeType)
            .setChooserTitle("Share text with: ")
            .setText(txt)
            .startChooser()
    }

    private fun openUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Can't handle this Intent", Toast.LENGTH_LONG).show()
        }
    }
}
