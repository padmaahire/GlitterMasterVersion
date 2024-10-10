package com.goglitter.ui.DrawerMenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.goglitter.R
import com.goglitter.domain.response.JewelleryBanner
import com.smarteist.autoimageslider.SliderViewAdapter

/**
@author-Padma A
date-31/5/2023
 **/

class BannerAdapter(imageUrl: java.util.ArrayList<JewelleryBanner>) :
    SliderViewAdapter<BannerAdapter.SliderViewHolder>() {
    var sliderList: ArrayList<JewelleryBanner> = imageUrl
    override fun getCount(): Int {
        return sliderList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        val inflate: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.slider_layout, null)
        return SliderViewHolder(inflate)
    }
    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {
        if (viewHolder != null) {

            Glide.with(viewHolder.itemView).load(sliderList.get(position).homeImage).fitCenter()
                .into(viewHolder.imageView)
        }
    }

    class SliderViewHolder(itemView: View?) : SliderViewAdapter.ViewHolder(itemView) {
        var imageView: ImageView = itemView!!.findViewById(R.id.myimage)
    }
    fun updateBanner(customerList: ArrayList<JewelleryBanner>) {
        sliderList = customerList
        notifyDataSetChanged()
    }
}