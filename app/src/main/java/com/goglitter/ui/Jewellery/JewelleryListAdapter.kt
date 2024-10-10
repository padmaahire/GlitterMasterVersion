package com.goglitter.ui.Jewellery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goglitter.R
import com.goglitter.data.JewelleryApplicationLeads
import com.google.android.material.textview.MaterialTextView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class JewelleryListAdapter(private var mList: ArrayList<JewelleryApplicationLeads>) : RecyclerView.Adapter<JewelleryListAdapter.ViewHolder>() {
    lateinit var context: Context
    private val dd_MMM_yyyy = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context=parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.layout_enquiry
                , parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // gList= ArrayList<GasTypeList>()

        val ItemsViewModel = mList[position]
        holder.name.setText(ItemsViewModel.jewName+" "+ItemsViewModel.jewLname)
        holder.phone.setText(ItemsViewModel.jewMobile)
        holder.email.setText(ItemsViewModel.jewEmail)

        val time: String = ItemsViewModel.dateAdded.toString()
        val date = stringToDate(time)
        val newDate: String = date2DayTime(date)!!
        holder.date.setText(newDate)
    }
    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val name: MaterialTextView =itemView.findViewById(R.id.name)
        val phone: MaterialTextView =itemView.findViewById(R.id.phone)
        val email: MaterialTextView =itemView.findViewById(R.id.email)
        val date: MaterialTextView =itemView.findViewById(R.id.date)
    }

    fun updateLeadList(LeadList: ArrayList<JewelleryApplicationLeads>) {
        mList = LeadList
        notifyDataSetChanged()
    }

    private fun stringToDate(aDate: String): Date? {
        var date = Date()
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val formatterOut = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy")
            date = formatter.parse(aDate)
            println(date)
            println(formatterOut.format(date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    fun date2DayTime(oldTime: Date?): String? {
        val newTime = Date()
        try {
            val cal = Calendar.getInstance()
            cal.time = newTime
            val oldCal = Calendar.getInstance()
            oldCal.time = oldTime
            val oldYear = oldCal[Calendar.YEAR]
            val year = cal[Calendar.YEAR]
            val oldDay = oldCal[Calendar.DAY_OF_YEAR]
            val day = cal[Calendar.DAY_OF_YEAR]
            if (oldYear == year) {
                val value = oldDay - day
                return if (value == -1) {
                    "Yesterday " // + HHmm.format(oldTime);
                } else if (value == 0) {
                    // "Today " //+ HHmm.format(oldTime);
                    return dd_MMM_yyyy.format(oldTime)
                } else if (value == 1) {
                    //return oldTime.toString();
                    return dd_MMM_yyyy.format(oldTime)
                } else {
                    //return oldTime.toString();
                    return  dd_MMM_yyyy.format(oldTime)
                }
            }
        } catch (e: Exception) {
        }
        //return oldTime.toString();
        return dd_MMM_yyyy.format(oldTime)
    }
}
