package com.goglitter.ui.Jewellery

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.goglitter.R
import com.goglitter.databinding.FragmentJwelleryDialogSheetBinding
import com.goglitter.domain.response.JewellerBanner
import com.goglitter.utils.CustomerCallBackListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class JwelleryDialogSheetFragment(private val listner: CustomerCallBackListener) :
    DialogFragment() {
    private lateinit var binding: FragmentJwelleryDialogSheetBinding
    private var param1: String? = null
    private var param2: String? = null
    lateinit var _name: String

    fun newInstance(list: JewellerBanner): JwelleryDialogSheetFragment {
        val args = Bundle()
        args.putParcelable("cList", list)
        val fragment = JwelleryDialogSheetFragment(listner)
        fragment.arguments = args
        return fragment
    }

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
        binding = FragmentJwelleryDialogSheetBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        arguments.let {
            val data: JewellerBanner? = requireArguments().getParcelable("cList")
            Log.d("list", data.toString())
            if (data != null) {
                binding.progressCircular.isVisible = false
                Glide.with(view).load(data.BannerImage).fitCenter()
                    .into(binding.imageView)
                bind(data)
            } else {
                binding.progressCircular.isVisible = true
            }
        }
        return view
    }

    @NonNull
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity()!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.attributes!!.windowAnimations = R.style.DialogAnimation
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnApply.setOnClickListener() {
            dismiss()
            listner.onClick()
        }
    }

    fun bind(list: JewellerBanner) {
        binding.list = list!!
    }
}