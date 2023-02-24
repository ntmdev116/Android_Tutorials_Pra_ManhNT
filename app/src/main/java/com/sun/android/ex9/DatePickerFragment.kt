package com.sun.android.ex9

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment


class DatePickerFragment : DialogFragment() {
    private var onDateSet: DatePickerDialog.OnDateSetListener? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Calendar.getInstance().let {
            val year = it.get(Calendar.YEAR)
            val month = it.get(Calendar.MONTH)
            val day = it.get(Calendar.DAY_OF_MONTH)

            return DatePickerDialog(requireActivity(), onDateSet, year, month, day)
        }
    }

    internal fun setOnDateSet(onDateSet : DatePickerDialog.OnDateSetListener) {
        this.onDateSet = onDateSet
    }
}
