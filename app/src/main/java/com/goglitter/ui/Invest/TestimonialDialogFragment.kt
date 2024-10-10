package com.goglitter.ui.Invest


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
import androidx.fragment.app.DialogFragment
import com.goglitter.R
import com.goglitter.databinding.FragmentTestimonialDialogBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TestimonialDialogFragment(str: String, name: String?) : DialogFragment() {
   lateinit var binding:FragmentTestimonialDialogBinding
    private var param1: String? = null
    private var param2: String? = null
    private var strMsg:String?=null
    private var title:String?=null

    init {
        this.strMsg=str
        this.title=name
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

        binding= FragmentTestimonialDialogBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        binding.btnClose.setOnClickListener(){
            dismiss()
        }

        if(title!=null && strMsg!=null){
            binding.progressCircular.visibility=View.GONE
            binding.tvTitle.setText(title)
            binding.tvDescription.setText(strMsg)
        }
        return view
    }


    @NonNull
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity()!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
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
}
