package com.goglitter.ui.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goglitter.MainActivity
import com.goglitter.R
import com.goglitter.databinding.FragmentHomeBinding
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

/**
@author-Padma A
date-30/5/2023
 updated-23/4/24
 Add Firebase Analytics SDK
 **/
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
@AndroidEntryPoint
class HomeFragment : Fragment(),View.OnClickListener {
    lateinit var binding: FragmentHomeBinding
    private lateinit var toolbarTitle : MaterialTextView
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(layoutInflater,container,false)
        val view=binding.root
        binding.goldLoan.setOnClickListener(this)
        binding.goldJwellery.setOnClickListener(this)
        binding.goldInvestment.setOnClickListener(this)
        binding.layCalculator.setOnClickListener(this)
        binding.goldRate.setOnClickListener(this)
        binding.cibilScore.setOnClickListener(this)
        binding.contactUs.setOnClickListener(this)

        return view
    }
    override fun onClick(v: View?) {
      when(v?.id){

          R.id.goldLoan->{
              (activity as MainActivity).navigate(R.id.nav_loan!!)
          }

          R.id.goldJwellery->{
              (activity as MainActivity).navigate(R.id.nav_jwellery!!)
          }

          R.id.goldInvestment->{
              (activity as MainActivity).navigate(R.id.nav_invest!!)
          }

          R.id.layCalculator->{
              Navigation.findNavController(v)
                  .navigate(R.id.action_nav_home_to_goldLoanCalculatorFragment)
          }

          R.id.goldRate->{
              Navigation.findNavController(v).navigate(R.id.action_nav_home_to_glitterRateFragment)

          }

          R.id.contactUs->{
              Navigation.findNavController(v).navigate(R.id.action_nav_home_to_contactUsFragment)
          }

      }
    }

}