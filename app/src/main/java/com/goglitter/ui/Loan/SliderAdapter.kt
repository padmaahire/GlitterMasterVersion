package com.goglitter.ui.Loan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.goglitter.R
import com.goglitter.domain.response.LoanBanner
import com.smarteist.autoimageslider.SliderViewAdapter

/**
@author-Padma A
date-31/5/2023
 **/

class SliderAdapter(imageUrl: java.util.ArrayList<LoanBanner>) :
    SliderViewAdapter<SliderAdapter.SliderViewHolder>() {
    var sliderList: ArrayList<LoanBanner> = imageUrl

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

            Glide.with(viewHolder.itemView).load(sliderList.get(position).bannerImage)
                .into(viewHolder.imageView)
        }
    }

    class SliderViewHolder(itemView: View?) : SliderViewAdapter.ViewHolder(itemView) {

        var imageView: ImageView = itemView!!.findViewById(R.id.myimage)
    }
    fun updateBanner(customerList: ArrayList<LoanBanner>) {
        sliderList = customerList
        notifyDataSetChanged()
    }


}