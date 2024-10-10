package com.goglitter.ui.OTPActivity

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.goglitter.R
import com.goglitter.databinding.FragmentDialogBaseBinding
import com.goglitter.ui.Listener.GlitterDialogCallbackListener
import com.goglitter.utils.Constants

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DialogBaseFragment(private val listner: GlitterDialogCallbackListener) : DialogFragment() {

    lateinit var binding: FragmentDialogBaseBinding
    private var param1: String? = null
    private var param2: String? = null

    fun newInstance(
        img: Int,
        title: String,
        subtitle: String,
        positive: String,
        negative: String,
        isShow: Boolean
    ): DialogBaseFragment {

        val args = Bundle()
        args.putInt(Constants.KEY_IMG, img)
        args.putString(Constants.KEY_TITLE, title)
        args.putString(Constants.KEY_SUBTITLE, subtitle)
        args.putString(Constants.KEY_BUTTON_POSITIVE, positive)
        args.putString(Constants.KEY_BUTTON_NEGETIVE, negative)
        args.putBoolean("KEY_SHOW", isShow)
        val fragment = DialogBaseFragment(listner)
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
        binding = FragmentDialogBaseBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        getDialog()?.getWindow()?.setBackgroundDrawableResource(R.drawable.dialog_bg)

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
                val negitive = requireArguments()!!.getString(Constants.KEY_BUTTON_NEGETIVE, "")
                val img = requireArguments().getInt(Constants.KEY_IMG)
                val isVisible = requireArguments().getBoolean("KEY_SHOW")
                if (isVisible) {
                    binding.layButton.visibility = View.VISIBLE
                    binding.btnDone.visibility = View.GONE
                } else {
                    binding.layButton.visibility = View.GONE
                    binding.btnDone.visibility = View.VISIBLE
                }

                binding.img.setImageResource(img)
                binding.tvTitle.setText(title)
                binding.tvSubtitle.setText(subtitle)
                binding.btnDone.setText(positve)
                binding.btnOkay.setText(positve)
                binding.btnCancel.setText(negitive)
            }

            binding.btnDone.setOnClickListener(View.OnClickListener {
                listner.DialogDone()
                dismiss()
            })

            binding.btnOkay.setOnClickListener(View.OnClickListener {
                listner.onCustomerDialogOk()
                dismiss()
            })
            binding.btnCancel.setOnClickListener(View.OnClickListener {
                listner.onCustomerDialogCancel()
                dismiss()
            })

            binding.btnDone.setOnClickListener() {
                listner.DialogDone()
                dismiss()
            }
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, e.toString())
        }
    }

}