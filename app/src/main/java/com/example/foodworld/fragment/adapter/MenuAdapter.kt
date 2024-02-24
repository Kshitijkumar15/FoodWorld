package com.example.foodworld.fragment.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodworld.DetailsActivity
import com.example.foodworld.databinding.MenuBinding

@Suppress("DEPRECATION")
class MenuAdapter(private val MenuItemsName:MutableList<String>,
                  private val MenuItemPrice:MutableList<String>,
                  private val MenuImage:MutableList<Int>, private val requireContext: Context
): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>()
{
    private  val itemClickListner:OnClickListener ?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding=MenuBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
      holder.bind(position)
    }
    override fun getItemCount(): Int =MenuItemsName.size
    inner  class MenuViewHolder(private val binding: MenuBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position=adapterPosition
                if(position !=RecyclerView.NO_POSITION){
                    itemClickListner?.onItemClick(position)
                }
                val intent=Intent(requireContext,DetailsActivity::class.java)
                intent.putExtra("MenuItemName",MenuItemsName.get(position))
                intent.putExtra("MenuItemImage",MenuImage.get(position))
                requireContext.startActivity(intent)

            }
        }

        fun bind(position: Int) {
            binding.apply {
                menuFoodName.text=MenuItemsName[position]
                menuPrice.text=MenuItemPrice[position]
                menuImage.setImageResource(MenuImage[position])
            }
        }

    }

    interface OnClickListener{
        fun onItemClick(position: Int)
    }
}


