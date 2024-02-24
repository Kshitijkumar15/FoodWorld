package com.example.foodworld.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodworld.PayOutActivity
import com.example.foodworld.R
import com.example.foodworld.databinding.CartItemBinding
import com.example.foodworld.databinding.FragmentCartBinding
import com.example.foodworld.fragment.adapter.CartAdapter

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCartBinding.inflate(inflater,container,false)
        val cartFoodName= listOf("Burger","Ramen")
        val cartItemPrice= listOf("100","300")
        val cartImage= listOf(R.drawable.burger,R.drawable.ramen)
        val adapter=CartAdapter(ArrayList(cartFoodName),ArrayList(cartItemPrice),ArrayList(cartImage))
        binding.cartRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter=adapter

        binding.proceedButton.setOnClickListener {
            val intent=Intent(requireContext(),PayOutActivity::class.java)
            startActivity(intent)
        }



        return binding.root

    }

    companion object {

    }
}