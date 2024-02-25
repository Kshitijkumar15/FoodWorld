package com.example.foodworld.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodworld.R
import com.example.foodworld.databinding.FragmentSearchBinding
import com.example.foodworld.fragment.adapter.MenuAdapter

class SearchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private val originalMenuFoodName= listOf("Burger","Ramen","South India Thali","Biryani","North Indian Thali","Dosa","Kadai Paneer")
    private val originalMenuItemPrice= listOf("49","299","70","249","100","60","135")
    val originalMenuImage= listOf(
        R.drawable.burger,
        R.drawable.ramen,
        R.drawable.menu1,
        R.drawable.menu2,
        R.drawable.menu3,
        R.drawable.menu4,
        R.drawable.menu5

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private val filterMenuFood= mutableListOf<String>()
    private val filterMenuPrice= mutableListOf<String>()
    private val filterMenuImage= mutableListOf<Int>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSearchBinding.inflate(inflater,container,false)
//        adapter= MenuAdapter(filterMenuFood,filterMenuPrice,filterMenuImage,requireContext())
        binding.menuRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter=adapter


        setupSearchView()

        showAllMennu()
        return binding.root
    }

    private fun showAllMennu() {
         filterMenuFood.clear()
        filterMenuPrice.clear()
        filterMenuImage.clear()

        filterMenuFood.addAll(originalMenuFoodName)
        filterMenuPrice.addAll(originalMenuItemPrice)
        filterMenuImage.addAll(originalMenuImage)
        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView() {
      binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
          android.widget.SearchView.OnQueryTextListener {
          override fun onQueryTextSubmit(query: String?): Boolean {
              filterMenuItem(query.orEmpty())
              return true
          }

          override fun onQueryTextChange(newText: String?): Boolean {
              filterMenuItem(newText.orEmpty())
              return true
          }
      })
    }

    private fun filterMenuItem(query: String) {
       filterMenuFood.clear()
        filterMenuPrice.clear()
        filterMenuImage.clear()

        originalMenuFoodName.forEachIndexed { index, foodName ->
            if (foodName.contains(query.toString(), ignoreCase = true)) {
                filterMenuFood.add(foodName)
                filterMenuPrice.add(originalMenuItemPrice[index])
                filterMenuImage.add(originalMenuImage[index])
            }
        }
        adapter.notifyDataSetChanged()
    }

    companion object {

    }
}