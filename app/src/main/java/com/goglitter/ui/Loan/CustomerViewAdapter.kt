package com.goglitter.ui.Loan

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.goglitter.R
import com.goglitter.databinding.CutomerViewLayoutBinding
import com.goglitter.domain.response.GlitterTestimonial
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.cutomer_view_layout.view.*
import java.util.ArrayList

class CustomerViewAdapter(private var clist: ArrayList<GlitterTestimonial>,var listener:onClickListener) : RecyclerView.Adapter<CustomerViewAdapter.CustomerVH>() {

    private lateinit var binding: CutomerViewLayoutBinding
    var flag: Boolean = true

    class CustomerVH(private val binding: CutomerViewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: GlitterTestimonial) {
            binding.list = list!!
        }
//        var imageView: ImageView = itemView!!.findViewById(R.id.ivProfileImage)
        var description: MaterialTextView = itemView!!.findViewById(R.id.tvDescription)
        var readMore: MaterialTextView = itemView!!.findViewById(R.id.readMore)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerVH {
        binding = CutomerViewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerVH(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CustomerVH, position: Int) {
        val _clist = clist[position]
        holder.bind(_clist)

        val str = Html.fromHtml(_clist.testDescription ?: "").toString()
       // val strLength = str.length
        val strLength = str.lines()
        holder.description.text = str
      /*  if(strLength.equals(5)){
            holder.readMore.visibility=View.GONE
        }else{
            holder.readMore.visibility=View.VISIBLE
        }*/



        /*if (strLength < 250) {
            holder.description.text = str
            holder.readMore.visibility=View.GONE
        } else {
            val truncatedStr = str.substring(0, 250) // Truncate to 250 characters

            //  holder.description.text = "$truncatedStr... Read More"
            val color = ContextCompat.getColor(holder.itemView.context, R.color.primary_color)
            val readMoreText = SpannableString("Read More")
            readMoreText.setSpan(
                ForegroundColorSpan(color), // Change Color.RED to the desired color
                0,
                readMoreText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Concatenate truncated string and colored "Read More"
            val finalText = SpannableStringBuilder()
                .append(truncatedStr)
                .append("... ")
                .append(readMoreText)

           holder.description.text = truncatedStr
        }*/

        holder.readMore.setOnClickListener(){
            listener.onTestimonialViewClick(_clist,position)
        }

       // holder.description.text=Html.fromHtml(_clist.testDescription)
    }

    override fun getItemCount(): Int {
        return clist.size
    }

    fun updateTestimonial(testimonialList: ArrayList<GlitterTestimonial>) {
        clist = testimonialList
        notifyDataSetChanged()
    }

    interface onClickListener{
        fun onTestimonialViewClick(_clist: GlitterTestimonial, position: Int)
    }

}