package com.goglitter.ui.Invest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goglitter.R
import com.goglitter.databinding.InvestorLayoutBinding
import com.goglitter.domain.InvestCategory
import com.google.android.material.textview.MaterialTextView

/**
@author-Padma A
date-09/02/2024
 @updated 15/04/2024
 **/

/*
class InvestorAdapter(private var clist: ArrayList<InvestCategory>, val listener: onClickListener) : RecyclerView.Adapter<InvestorAdapter.CustomerVH>() {

   private lateinit var binding:InvestorLayoutBinding
    var flag: Boolean = true

    class CustomerVH(private val binding: InvestorLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var title: MaterialTextView = itemView!!.findViewById(R.id.tv_investor)
       var subtitle: MaterialTextView = itemView!!.findViewById(R.id.tv_investor_sub)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerVH {
        binding = InvestorLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerVH(binding)
    }

    override fun onBindViewHolder(holder: CustomerVH, position: Int) {
        val _clist = clist[position]
        val name=_clist.categoryName
        if (name!!.contains("(")) {
            val parts = name!!.split("(")
            val titleText = parts[0].trim() // Trim to remove extra spaces
            val subtitleText = parts[1].replace(")", "").trim() // Trim and remove ")" from the subtitle

            holder.title.text = "$titleText"
            holder.subtitle.text = "($subtitleText)"
        } else {
            holder.title.text = name
            // If there is no "(", you might want to set subtitle to an empty string or handle it accordingly.
            holder.subtitle.text = ""
        }
        holder.itemView.setOnClickListener(){
            listener.OnInvestorClick(clist,position)
        }

    }
    override fun getItemCount(): Int {
        return clist.size
    }
    fun updateInvestList(investList: ArrayList<InvestCategory>) {
        clist = investList
        notifyDataSetChanged()
    }


    interface onClickListener{
        fun OnInvestorClick(clist: ArrayList<InvestCategory>, position: Int)
    }

}
*/
class InvestorAdapter(private var clist: ArrayList<InvestCategory>, val listener: onClickListener) : RecyclerView.Adapter<InvestorAdapter.CustomerVH>() {

    private lateinit var binding:InvestorLayoutBinding
    var flag: Boolean = true

    class CustomerVH(private val binding: InvestorLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var title: MaterialTextView = itemView!!.findViewById(R.id.tv_investor)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerVH {
        binding = InvestorLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerVH(binding)
    }

    override fun onBindViewHolder(holder: CustomerVH, position: Int) {
        val _clist = clist[position]
        val name=_clist.categoryName
        if (name!!.contains("(")) {
            val parts = name!!.split("(")
            val titleText = parts[0].trim() // Trim to remove extra spaces
            val subtitleText = parts[1].replace(")", "").trim() // Trim and remove ")" from the subtitle
            //holder.title.text = "$titleText"
            holder.title.text = "$titleText"+" "+"|"+" "+"$subtitleText"
          //  holder.subtitle.text = "($subtitleText)"
        } else {
            holder.title.text = name
            // If there is no "(", you might want to set subtitle to an empty string or handle it accordingly.
           // holder.subtitle.text = ""
        }
        holder.itemView.setOnClickListener(){
            listener.OnInvestorClick(clist,position)
        }

    }
    override fun getItemCount(): Int {
        return clist.size
    }
    fun updateInvestList(investList: ArrayList<InvestCategory>) {
        clist = investList
        notifyDataSetChanged()
    }


    interface onClickListener{
        fun OnInvestorClick(clist: ArrayList<InvestCategory>, position: Int)
    }

}