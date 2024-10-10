package com.goglitter.ui.Account

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.goglitter.databinding.FragmentLoanEnquiryListBinding
import com.goglitter.domain.response.ApplicationLeads
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import com.goglitter.utils.TokenManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
@author-Padma A
date-08/08/2023
 **/
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class LoanEnquiryListFragment : Fragment() {
    private lateinit var binding: FragmentLoanEnquiryListBinding
    private var param1: String? = null
    private var param2: String? = null
    lateinit var enquiryAdapter: EnquiryListAdapter
    var Glitter_lead_lst = ArrayList<ApplicationLeads>()
    var id: String = ""
    val viewModel: AuthViewModel by viewModels()
    private val calendar = Calendar.getInstance()

    @Inject
    lateinit var tokenManager: TokenManager

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
        binding = FragmentLoanEnquiryListBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        id = tokenManager.getSalesId().toString()
        getLeadList(id)
        Glitter_lead_lst = ArrayList()
        /*for (i in 0..10) {lead_lst.add(enquiryListResponse("1","Jhon","28 July 2023","+91-9923279850","abc@gmail.com"))}*/
        return view
    }

    private fun getLeadList(id: String) {
        viewModel.getLeadResponse(id)
        viewModel.leadsResponse.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Loading -> {
               //     binding.progressCircular.isVisible = it.isLoading
                }
                is NetworkResult.Failure -> {
                    showSnackBar(it.errorMessage)
                }
                is NetworkResult.Success -> {
                  //  binding.progressCircular.isVisible = false
                    Glitter_lead_lst = it.data.result as ArrayList<ApplicationLeads>
                    Log.d("lst", Glitter_lead_lst.toString())

                    if (Glitter_lead_lst != null && Glitter_lead_lst.size > 0) {
                        val layoutManager = LinearLayoutManager(
                            requireActivity(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        binding.rvLeadList.setLayoutManager(layoutManager)
                        enquiryAdapter = EnquiryListAdapter(Glitter_lead_lst)
                        binding.rvLeadList.setAdapter(enquiryAdapter)
                    } else {
                        binding.rvLeadList.visibility = View.GONE
                        binding.tvMsg.visibility = View.VISIBLE
                        binding.progressCircular.isVisible = false
                    }
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        getLeadList(id)
    }
    open fun showSnackBar(message :String) {
        val rootView: View? = getRootView()
        if (rootView != null) Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
    }
    open fun getRootView(): View? {
        val contentViewGroup = activity?.findViewById<View>(android.R.id.content) as ViewGroup
        var rootView: View? = null
        if (contentViewGroup != null) rootView = contentViewGroup.getChildAt(0)
        if (rootView == null) rootView = activity?.window?.decorView?.rootView
        return rootView
    }
}