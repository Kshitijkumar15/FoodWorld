package com.example.foodworld.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodworld.R
import com.example.foodworld.databinding.FragmentMenuBinding
import com.example.foodworld.fragment.adapter.MenuAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMenuBinding.inflate(inflater,container,false)
        val menuFoodName= listOf("Burger","Ramen","South India Thali","Biryani","North Indian Thali","Dosa","Kadai Paneer")
        val menuItemPrice= listOf("49","299","70","249","100","60","135")
        val menuImage= listOf(
            R.drawable.burger,
            R.drawable.ramen,
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4,
            R.drawable.menu5

        )
        val adapter= MenuAdapter(ArrayList(menuFoodName),ArrayList(menuItemPrice),ArrayList(menuImage),requireContext())
        binding.menuRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter=adapter
        return binding.root
    }

    companion object {

    }
}