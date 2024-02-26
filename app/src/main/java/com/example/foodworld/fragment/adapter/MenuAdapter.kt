package com.example.foodworld.fragment.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodworld.AllItemActivity
import com.example.foodworld.DetailsActivity
import com.example.foodworld.databinding.MenuBinding
import com.example.foodworld.model.MenuItem

@Suppress("DEPRECATION")
class MenuAdapter(
    private val MenuItems: List<MenuItem>,
    private val requireContext: Context,

    ) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = MenuItems.size
    inner class MenuViewHolder(private val binding: MenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailAcctivity(position)
                }
            }
        }

        private fun openDetailAcctivity(position: Int) {
            val menuItem = MenuItems[position]
            val intent = Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("MenuItemName", menuItem.foodName)
                putExtra("MenuItemImage", menuItem.foodImage)
                putExtra("MenuItemPrice", menuItem.foodPrice)
                putExtra("MenuItemDescription", menuItem.foodDescription)

            }
            requireContext.startActivity(intent)
        }
//set data into recycler view items

        fun bind(position: Int) {
            binding.apply {
                val menuItem = MenuItems[position]
                menuFoodName.text = menuItem.foodName
                menuPrice.text = menuItem.foodPrice
                val uri = Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(menuImage)
            }
        }
    }
}


