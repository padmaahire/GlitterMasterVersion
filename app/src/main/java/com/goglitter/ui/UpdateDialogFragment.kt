package com.goglitter.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.goglitter.R
import com.goglitter.databinding.FragmentDialogBaseBinding
import com.goglitter.databinding.FragmentUpdateDialogBinding
import com.goglitter.ui.Listener.GlitterDialogCallbackListener
import com.goglitter.utils.Constants


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/*
@author-Padma Ahire
@date-26/04/24
* */
class UpdateDialogFragment(private val listner: GlitterUpdateCallBackListener) : DialogFragment() {

    lateinit var binding: FragmentUpdateDialogBinding
    private var param1: String? = null
    private var param2: String? = null

    fun newInstance(
        title: String,
        subtitle: String,
        positive: String
    ): UpdateDialogFragment {

        val args = Bundle()
        args.putString(Constants.KEY_TITLE, title)
        args.putString(Constants.KEY_SUBTITLE, subtitle)
        args.putString(Constants.KEY_BUTTON_POSITIVE, positive)
        val fragment = UpdateDialogFragment(listner)
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
        // Inflate the layout for this fragment
        binding = FragmentUpdateDialogBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        getDialog()?.getWindow()?.setBackgroundDrawableResource(R.drawable.dialog_bg)
        dialog!!.setCancelable(false)
        return view
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            arguments.let {
                val title = requireArguments().getString(Constants.KEY_TITLE, "")
                val subtitle = requireArguments()!!.getString(Constants.KEY_SUBTITLE, "")
                val positve = requireArguments()!!.getString(Constants.KEY_BUTTON_POSITIVE, "")

                binding.tvTitle.setText(title)
                binding.tvSubtitle.setText(subtitle)
                binding.btnUpdate.setText(positve)

            }

            binding.btnUpdate.setOnClickListener(View.OnClickListener {
                listner.DialogUpdate()
                dismiss()
            })


        } catch (e: Exception) {
            Log.d(ContentValues.TAG, e.toString())
        }
    }

}