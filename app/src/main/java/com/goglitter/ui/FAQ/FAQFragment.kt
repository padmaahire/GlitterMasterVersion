package com.goglitter.ui.FAQ

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.goglitter.databinding.FragmentFAQBinding
import com.goglitter.domain.response.FAQsLIST
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

/*
@uthor-Padma Ahire
date-20/7/2023
* */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class FAQFragment : Fragment() {
    val viewModel: AuthViewModel by viewModels()
    lateinit var binding: FragmentFAQBinding
    lateinit var faqAdapter: FAQAdapter
    var Glitter_faq_lst= ArrayList<FAQsLIST>()
    private var title:String?=null
    private var description:String?=null
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
        binding= FragmentFAQBinding.inflate(layoutInflater,container,false)
        val view=binding.root

       getFAQs()

        Glitter_faq_lst= ArrayList()

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvFaqs.setLayoutManager(layoutManager)
        faqAdapter = FAQAdapter(requireContext())
        binding.rvFaqs.setAdapter(faqAdapter)
        return view
    }


    private fun getFAQs() {

        viewModel.getFAQSResponse()

        viewModel.faqResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }

                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }

                is NetworkResult.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    Glitter_faq_lst = it.data.result as ArrayList<FAQsLIST>
                     faqAdapter.updateFaqs(Glitter_faq_lst)

                }
            }
        }

    }
}