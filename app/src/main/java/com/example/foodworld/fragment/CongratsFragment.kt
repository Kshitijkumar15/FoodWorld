package com.example.foodworld.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodworld.MainActivity
import com.example.foodworld.R
import com.example.foodworld.databinding.FragmentCongratsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CongratsFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentCongratsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCongratsBinding.inflate(layoutInflater,container,false)
        binding.goHomeButton.setOnClickListener {
            val intent=Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    companion object {

    }
}