package com.example.foodworld.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodworld.databinding.NotiItemBinding

class NotiAdapter(private val notification:ArrayList<String>,private val notificationImage:ArrayList<Int>): RecyclerView.Adapter<NotiAdapter.NotificationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding=NotiItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding)
    }



    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
       holder.bind(position)
    }
    override fun getItemCount(): Int =notification.size
    inner class NotificationViewHolder(private val binding:NotiItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply{
                notiTextView.text=notification[position]
                notiImageView.setImageResource(notificationImage[position])
            }
        }

    }
}