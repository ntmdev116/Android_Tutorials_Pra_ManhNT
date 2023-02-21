package com.sun.android.ex6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sun.android.R
import com.sun.android.databinding.FragmentSimpleBinding


/**
 * A simple [Fragment] subclass.
 * Use the [SimpleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SimpleFragment : Fragment() {
    private val YES = 0
    private val NO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSimpleBinding.inflate(inflater, container, false)

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioButtonYes.id -> binding.fragmentHeader.setText(R.string.yes_message)
                binding.radioButtonNo.id -> binding.fragmentHeader.setText(R.string.no_message)
                else -> { }
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SimpleFragment()
    }
}
