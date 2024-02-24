package com.example.foodworld.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodworld.R
import com.example.foodworld.databinding.BuyagainBinding
import com.example.foodworld.databinding.FragmentHistoryBinding
import com.example.foodworld.fragment.adapter.BuyAdapter


class HistoryFragment : Fragment() {

private lateinit var binding: FragmentHistoryBinding
private lateinit var buyAgainAdapter:BuyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHistoryBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        setupRecyclerView()
        return binding.root
    }
    private fun setupRecyclerView(){
        val buyAgainFoodName= arrayListOf("food 1","food 2")
        val buyAgainFoodPrice= arrayListOf("49","299")
        val buyAgainFoodImage= arrayListOf(R.drawable.burger,R.drawable.ramen)
        buyAgainAdapter= BuyAdapter(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodImage)
        binding.buyAgainRecyclerView.adapter=buyAgainAdapter
        binding.buyAgainRecyclerView.layoutManager=LinearLayoutManager(requireContext())

    }

    companion object {

    }
}