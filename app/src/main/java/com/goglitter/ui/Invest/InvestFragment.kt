package com.goglitter.ui.Invest

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.goglitter.R
import com.goglitter.databinding.FragmentInvestBinding
import com.goglitter.domain.InvestCategory
import com.goglitter.domain.response.GlitterTestimonial
import com.goglitter.ui.Jewellery.JwellaryFragment
import com.goglitter.ui.Loan.CustomerViewAdapter
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.CustomerCallBackListener
import com.goglitter.utils.NetworkResult
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

/**
@author-Padma A
date-09/02/2024
 **/

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class InvestFragment : Fragment(), CustomerCallBackListener, InvestorAdapter.onClickListener,CustomerViewAdapter.onClickListener {
    lateinit var binding: FragmentInvestBinding
    private var param1: String? = null
    private var param2: String? = null
    lateinit var cutomerAdapter: CustomerViewAdapter
    lateinit var adapter: InvestorAdapter
    val viewModel: AuthViewModel by viewModels()
    var testimonialList: ArrayList<GlitterTestimonial> = arrayListOf()
    //var list = arrayOf("Mutual Fund","ETF","Digital Gold","Sovereign Bonds","RD")
    var investList: ArrayList<InvestCategory> = arrayListOf()
    lateinit var dialog:TestimonialDialogFragment

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
        binding = FragmentInvestBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        setupAdapter()
        getInvestorCategoryList()
        setupTestimonial()
        getTestimonials()
        return view
    }

    private fun setupTestimonial() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPeopleView.setLayoutManager(layoutManager)
        cutomerAdapter = CustomerViewAdapter(testimonialList,this)
        binding.rvPeopleView.setAdapter(cutomerAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupAdapter() {
        //val layoutManager = GridLayoutManager(requireContext(), 2)
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvJweller.setLayoutManager(layoutManager)
        //adapter = InvestorAdapter(list,this)
        adapter = InvestorAdapter(investList, this)
        binding.rvJweller.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            JwellaryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onAddCustomer(msg: String) {}
    override fun onClick() {
    }

    override fun OnInvestorClick(clist: ArrayList<InvestCategory>, position: Int) {
        val selectedValue = clist[position].categoryID
        val bundle = Bundle()
        bundle.putString("selectedValue", selectedValue)
        // Navigate to the destination fragment with the data
        Navigation.findNavController(requireView())
            .navigate(R.id.action_nav_invest_to_investApplicationFragment, bundle)
    }

    private fun getInvestorCategoryList() {
        viewModel.getInvestorCategoryList()
        viewModel.investCategoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }
                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                    showSnackBar(it.errorMessage)
                }
                is NetworkResult.Success -> {
                    binding.progressCircular.isVisible = false
                    investList = it.data.result as ArrayList<InvestCategory>
                    if (investList.size > 0) {
                        adapter.updateInvestList(investList)
                        binding.progressCircular.visibility = View.GONE
                    } else {

                    }
                }
                else -> {}
            }
        }
    }
    private fun getTestimonials() {
        viewModel.getInvestorTestimonialResponse()
        viewModel.investorTestimonialResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    testimonialList = it.data.result as ArrayList<GlitterTestimonial>
                    Log.d("lst", testimonialList.toString())
                    cutomerAdapter.updateTestimonial(testimonialList)
                    cutomerAdapter.notifyDataSetChanged()
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getInvestorCategoryList()
    }

    open fun showSnackBar(message: String) {
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

    override fun onTestimonialViewClick(_clist: GlitterTestimonial, position: Int) {
        val str = Html.fromHtml(_clist.testDescription ?: "").toString()
        val strLength = str.length
        var name=_clist.testName
            dialog=TestimonialDialogFragment(str,name)
            activity?.supportFragmentManager?.let { dialog.show(it, "testimonial") }

       /* if(strLength>250){
            dialog=TestimonialDialogFragment(str,name)
            activity?.supportFragmentManager?.let { dialog.show(it, "testimonial") }
        }*/
    }
}