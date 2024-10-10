package com.goglitter.ui.DrawerMenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.goglitter.databinding.FragmentPrivacyPolicyBinding
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
class PrivacyPolicyFragment : Fragment() {
    val viewModel: AuthViewModel by viewModels()
    lateinit var binding: FragmentPrivacyPolicyBinding
    private var param1: String? = null
    private var param2: String? = null
    private var title: String? = null
    private var description: String? = null

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
        // Inflate the layout for this fragment
        binding = FragmentPrivacyPolicyBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        getPrivacyPage()
        return view
    }


    private fun getPrivacyPage() {

        viewModel.getPageResponse("privacy-policy")

        viewModel.glitterPageResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }

                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false

                }
                is NetworkResult.Success -> {
                    title = it.data.result!!.pageTitle
                    description = it.data.result!!.pageDescription
                    binding.webview!!.getSettings().setJavaScriptEnabled(true);
                    binding.webview!!.loadData(description!!, "text/html; charset=utf-8", "UTF-8");
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }

    }

}