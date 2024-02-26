package com.example.foodworld

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodworld.databinding.FragmentNotiBinding
import com.example.foodworld.fragment.adapter.NotiAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class notiFragment : BottomSheetDialogFragment() {
private lateinit var binding: FragmentNotiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentNotiBinding.inflate(layoutInflater,container,false)
        val notifications= listOf("Canceled","Placed","In Transit")
        val notificationImages= listOf(R.drawable.cancel,R.drawable.successful,R.drawable.delivery)
        val adapter=NotiAdapter(
            ArrayList(notifications),
            ArrayList(notificationImages)
        )
        binding.notiRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.notiRecyclerView.adapter=adapter
        return binding.root
    }

    companion object
}