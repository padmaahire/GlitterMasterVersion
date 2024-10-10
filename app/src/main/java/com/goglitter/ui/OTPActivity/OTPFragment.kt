package com.goglitter.ui.OTPActivity

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.goglitter.R
import com.goglitter.databinding.FragmentOTPBinding
import com.goglitter.ui.Listener.DialogCallbackListener
import com.goglitter.utils.Constants

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class OTPFragment(private val listner: DialogCallbackListener) : DialogFragment() {

    lateinit var binding: FragmentOTPBinding
    private var param1: String? = null
    private var param2: String? = null

    fun newInstance(title: String, subtitle: String, positive : String): OTPFragment {
        val args = Bundle()
        args.putString(Constants.KEY_TITLE, title)
        args.putString(Constants.KEY_SUBTITLE, subtitle)
        args.putString(Constants.KEY_BUTTON_POSITIVE, positive)
        val fragment = OTPFragment(listner)
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
        isCancelable = false
        binding = FragmentOTPBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        getDialog()?.getWindow()?.setBackgroundDrawableResource(R.drawable.dialog_bg)
        return view
    }
    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            arguments.let {
                val title   = requireArguments().getString(Constants.KEY_TITLE, "")
                val subtitle   = requireArguments()!!.getString(Constants.KEY_SUBTITLE, "")
                val positve   = requireArguments()!!.getString(Constants.KEY_BUTTON_POSITIVE, "")
                val negitive  = requireArguments()!!.getString(Constants.KEY_BUTTON_NEGETIVE, "")

                binding.tvTitle.setText(title)
                binding.tvSubtitle.setText(subtitle)
                binding.btnOk.setText(positve)
            }

            binding.btnOk.setOnClickListener(View.OnClickListener {
                listner.onDialogOk()
                dismiss()
            })

        }catch (e : Exception){
            Log.d(ContentValues.TAG,e.toString())
        }

    }

}