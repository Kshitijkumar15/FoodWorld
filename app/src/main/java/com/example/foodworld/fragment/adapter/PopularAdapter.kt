//package com.example.foodworld.fragment.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.foodworld.databinding.PopularItemBinding
//
//class PopularAdapter (private val items:List<String>,private val priceList:List<String>, private val image:List<Int>):RecyclerView.Adapter<PopularAdapter.PopularViewHolder>(){
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
//        return PopularViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
//    }
//
//    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
//        val item=items[position]
//        val images=image[position]
//        val price=priceList[position]
//        holder.bind(item,images,price)
//    }
//    override fun getItemCount(): Int {
//return items.size
//    }
//
//    class PopularViewHolder(private val binding:PopularItemBinding):RecyclerView.ViewHolder(binding.root) {
//       private val imageView=binding.imageView3
//        fun bind(item: String, images: Int, price: String) {
//            binding.foodname.text=item
//            binding.pricePopular.text=price
//            imageView.setImageResource(images)
//        }
//
//    }
//
//}
package com.example.foodworld.fragment.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodworld.DetailsActivity
import com.example.foodworld.databinding.PopularItemBinding

class PopularAdapter(private val items: List<String>, private val priceList: List<String>,
                     private val image: List<Int>,
                     private val requireContext: Context) : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    init {
        require(items.size == priceList.size && priceList.size == image.size) { "Lists must have the same size" }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = items[position]
        val images=image[position]
        val price = priceList[position]
//        val imageRes = if (position < image.size) image[position] else /* handle the out-of-bounds case */ 0
        holder.bind(item, price, images)
        holder.itemView.setOnClickListener {
            val intent= Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("MenuItemName",item)
            intent.putExtra("MenuItemImage",images)
            requireContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PopularViewHolder(private val binding: PopularItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.menuImage

        fun bind(item: String, price: String, imageRes: Int) {
            binding.menuFoodName.text = item
            binding.menuPrice.text = price
            imageView.setImageResource(imageRes)
        }
    }
}

