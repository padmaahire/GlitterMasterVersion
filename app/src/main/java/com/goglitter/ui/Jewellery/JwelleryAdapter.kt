package com.goglitter.ui.Jewellery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goglitter.databinding.LayoutJwellersBinding
import com.goglitter.domain.response.JewellerBanner

class JwelleryAdapter(
    private var clist: ArrayList<JewellerBanner>, val listener: onClickListener
) : RecyclerView.Adapter<JwelleryAdapter.CustomerVH>() {

    private lateinit var binding: LayoutJwellersBinding
    var flag: Boolean = true

    class CustomerVH(private val binding: LayoutJwellersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: JewellerBanner) {
            binding.list = list!!
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerVH {
        binding =
            LayoutJwellersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         return CustomerVH(binding)
    }

    override fun onBindViewHolder(holder: CustomerVH, position: Int) {
        val _clist = clist[position]
        holder.bind(_clist)
        Glide.with(holder.itemView).load(_clist.Image)
            .into(binding.imgJweller)


        holder.itemView.setOnClickListener(){
            listener.OnJwellerClick(_clist,position)
        }
    }
    override fun getItemCount(): Int {
        return clist.size
    }
    interface onClickListener{
        fun OnJwellerClick(_clist: JewellerBanner, position: Int)
    }
    fun updateJeweller(sponsoredList: java.util.ArrayList<JewellerBanner>) {
        clist = sponsoredList
        notifyDataSetChanged()
    }
}