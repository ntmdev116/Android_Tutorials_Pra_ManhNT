package com.sun.android.ex9

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sun.android.R
import com.sun.android.databinding.ActivityMenuExampleBinding


class MenuExampleActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMenuExampleBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.example_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_order -> {
                displayToast(getString(R.string.action_order_message))
                return true
            }
            R.id.action_status -> {
                displayToast(getString(R.string.action_status_message))
                return true
            }
            R.id.action_favorites -> {
                displayToast(getString(R.string.action_favorites_message))
                return true
            }
            R.id.action_contact -> {
                displayToast(getString(R.string.action_contact_message))
                return true
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setListeners() {
        binding.buttonAlert.setOnClickListener {
            AlertDialog.Builder(this).run {
                setTitle(R.string.alert)
                setMessage(R.string.alert_message)
                setPositiveButton(R.string.ok) { _, _ ->
                    displayToast("Pressed OK")
                }
                setNegativeButton(R.string.cancel) { _, _ ->
                    displayToast("Pressed Cancel")
                }

                create()
                show()
            }
        }

        binding.buttonDate.setOnClickListener(::createDatePicker)
    }

    private fun createDatePicker(view: View) {
        DatePickerFragment().run {
            setOnDateSet { _, year, month, day ->
                val monthString = (month + 1).toString()
                val dayString = day.toString()
                val yearString = year.toString()
                val dateMessage = "$monthString/$dayString/$yearString"

                displayToast(getString(R.string.date).plus(dateMessage))
            }

            show(supportFragmentManager, "datePicker")

        }
    }

    private fun displayToast(message: String?) {
        Toast.makeText(
            applicationContext, message,
            Toast.LENGTH_SHORT
        ).show()
    }
}
