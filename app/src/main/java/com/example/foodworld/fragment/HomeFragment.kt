package com.example.foodworld.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodworld.R
import com.example.foodworld.databinding.FragmentHomeBinding
import com.example.foodworld.fragment.adapter.PopularAdapter
import java.util.ArrayList

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewMenu.setOnClickListener{
            val bottomSheetDialog=MenuFragment()
            bottomSheetDialog.show(parentFragmentManager,"Text")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imagelist = ArrayList<SlideModel>()
        imagelist.add(SlideModel(R.drawable.offer1, ScaleTypes.CENTER_INSIDE))
        imagelist.add(SlideModel(R.drawable.offer2, ScaleTypes.CENTER_INSIDE))
        imagelist.add(SlideModel(R.drawable.offer3, ScaleTypes.CENTER_INSIDE))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imagelist)
        imageSlider.setImageList(imagelist, ScaleTypes.FIT)
        imageSlider.setItemChangeListener(object : ItemChangeListener {
            override fun onItemChanged(position: Int) {
                val itemPosition = imagelist[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })

        val foodName = listOf("Burger", "Ramen","Salad")
        val price = listOf("100", "300","150")
        val foodImages = listOf(R.drawable.burger, R.drawable.ramen,R.drawable.salad)

        val adapter = PopularAdapter(foodName, price, foodImages,requireContext())
        binding.popularrecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.popularrecyclerView.adapter = adapter
    }
}
