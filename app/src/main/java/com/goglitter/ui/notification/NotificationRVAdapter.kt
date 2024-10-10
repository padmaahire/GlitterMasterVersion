package com.goglitter.ui.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goglitter.databinding.NotificationLayoutBinding
import com.goglitter.domain.response.GlitterNotificationList
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
@author-Padma Ahire
date-10/08/2023
 **/
class NotificationsRVAdapter(
    private var notificationList: ArrayList<GlitterNotificationList>, private var listener: OnItemClickListener
) : RecyclerView.Adapter<NotificationsRVAdapter.NotificationVH>() {

    private lateinit var binding: NotificationLayoutBinding
    var flag: Boolean = true
    private val dd_MMM_yyyy = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    class NotificationVH(private val binding: NotificationLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: GlitterNotificationList, listener: OnItemClickListener) {
            binding.list = list!!

            binding.layNoti.setSingleClickListener{
                listener.onNotificationDetailClick(list)
            }

            if (list.is_read!!.equals(1)) {
                binding.tvDot.setVisibility(View.INVISIBLE)
            } else {
                binding.tvDot.setVisibility(View.VISIBLE)
            }

        }

        fun View.setSingleClickListener(onClickAction: (View) -> Unit) {
            this.setOnClickListener(object : View.OnClickListener {
                private var lastClickTime: Long = 0

                override fun onClick(v: View) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime >= 1000) {
                        lastClickTime = currentTime
                        onClickAction(v)
                    }
                }
            })
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationVH {
        binding =
            NotificationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationVH(binding)
    }
    override fun onBindViewHolder(holder: NotificationVH, position: Int) {
        val _clist = notificationList[position]
        holder.bind(_clist,listener)
        val time: String = _clist.dateAdded.toString()
        val date = stringToDate(time)
        val newDate: String = date2DayTime(date)!!
        binding.tvDate.setText(newDate)
    }
    override fun getItemCount(): Int {
        return notificationList.size
    }
    fun updateNotification(glitterNotificationList: ArrayList<GlitterNotificationList>?) {
        notificationList = glitterNotificationList!!
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
    interface OnItemClickListener {
        fun onNotificationDetailClick(
            notificationList: GlitterNotificationList
        )
    }

}
