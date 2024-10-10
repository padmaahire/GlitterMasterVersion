package com.goglitter.ui.DrawerMenu

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.goglitter.R
import com.goglitter.databinding.FragmentTandCBottomSheetBinding
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

/**
@author-Padma A
date-05/07/2023
 **/
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class TandCBottomSheetFragment : BottomSheetDialogFragment() {
  lateinit var binding: FragmentTandCBottomSheetBinding
    private var param1: String? = null
    private var param2: String? = null
    val viewModel: AuthViewModel by viewModels()
    private var title:String?=null
    private var description:String?=null

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
        binding= FragmentTandCBottomSheetBinding.inflate(layoutInflater,container,false)
        val view=binding.root
        binding.btnback.setOnClickListener(){
            dismiss()
        }

        getTermsandConditionPage()
        return view

    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    @NonNull
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity()!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window!!.attributes!!.windowAnimations =R.style.DialogAnimation
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.getWindow()?.setLayout(width, height)
        }

    }


    private fun getTermsandConditionPage() {

        viewModel.getPageResponse("terms-and-condition")

        viewModel.glitterPageResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }

                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }

                is NetworkResult.Success -> {
                    title=it.data.result!!.pageTitle
                    description=it.data.result!!.pageDescription
                    if(description!=null){
                        binding.webview.visibility=View.VISIBLE
                        binding.tvLabel.visibility=View.GONE
                        binding.webview!!.getSettings().setJavaScriptEnabled(true)
                        binding.webview!!.loadData(description!!, "text/html; charset=utf-8", "UTF-8")
                        binding.progressCircular.visibility = View.GONE
                    }else{

                        binding.tvLabel.visibility=View.VISIBLE
                        binding.webview.visibility=View.GONE
                    }

                }
            }
        }

    }


}