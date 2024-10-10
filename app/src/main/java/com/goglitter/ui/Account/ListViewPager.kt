package com.goglitter.ui.Account

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

/**
@author-Padma A
date-08/08/2023
 **/
class ListViewPager(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {
    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val listTitle: MutableList<String> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }
    override fun getCount(): Int {
        return listTitle.size
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }
    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        listTitle.add(title)
    }
}

/*
class ListViewPager(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val listTitle: MutableList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return listTitle.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position].uppercase()
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        listTitle.add(title.capitalizeFirstLetter())
    }

    private fun String.capitalizeFirstLetter(): String {
        if (isEmpty()) {
            return this
        }
        return this.substring(0, 1).uppercase() + this.substring(1)
    }
}*/
