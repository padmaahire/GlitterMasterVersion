package com.goglitter.ui.Loan

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.goglitter.R
import com.goglitter.databinding.SponsoredLayoutBinding
import com.goglitter.domain.response.GlitterSponsors
import java.util.ArrayList


class SupportAdapter(
    private var clist: ArrayList<GlitterSponsors>
) : RecyclerView.Adapter<SupportAdapter.CustomerVH>() {

    private lateinit var binding: SponsoredLayoutBinding
    var flag: Boolean = true

    class CustomerVH(private val binding: SponsoredLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: GlitterSponsors) {
            binding.slist = list!!
        }
        var imageView: ImageView = itemView!!.findViewById(R.id.img_sponsored)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerVH {
        binding =
            SponsoredLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerVH(binding)
    }

    override fun onBindViewHolder(holder: CustomerVH, position: Int) {
        val _clist = clist[position]
        holder.bind(_clist)

        if (holder != null) {

            Glide.with(holder.itemView).load(clist.get(position).logoImage).fitCenter()
                .into(holder.imageView)
        }

    }

    override fun getItemCount(): Int {
        return clist.size
    }
    fun updateSponsors(sponsoredList: ArrayList<GlitterSponsors>) {
        clist = sponsoredList
        notifyDataSetChanged()
    }

}