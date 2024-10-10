package com.goglitter.ui.adapter

import android.R
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class LocationArrayAdapter(
    context: Context?,
    textViewResourceId: Int,
    objects: Array<CharSequence?>?
) :
    ArrayAdapter<CharSequence?>(context!!, textViewResourceId, objects!!) {
    private var _location: String? = null

    //FIXES IT
    var location: String?
        get() = _location
        set(location) {
            _location = location
            notifyDataSetChanged() //FIXES IT
        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = super.getView(position, convertView, parent!!)
        val t = view.findViewById(R.id.text1) as TextView
        t.setTextColor(Color.WHITE)
        t.text = _location
        return view
    }
}