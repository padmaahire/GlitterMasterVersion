package com.goglitter.ui.notification

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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.goglitter.R
import com.goglitter.databinding.FragmentNotificationDetailBinding
import com.goglitter.domain.request.GlitterNotificationRequest
import com.goglitter.domain.response.GlitterNotificationList
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

/**
@author-Padma Ahire
date-31/08/2023
 **/
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class NotificationDetailFragment(val notificationList: GlitterNotificationList, var listener: OnDialogDismissListener) : DialogFragment() {
    private lateinit var binding: FragmentNotificationDetailBinding
    private var param1: String? = null
    private var param2: String? = null
    val viewModel: AuthViewModel by viewModels()
    var title:String?=null
    var subTitle:String?=null
    lateinit var notificationAdapter: NotificationsRVAdapter
    var glitter_notification_lst = ArrayList<GlitterNotificationList>()
    var notificationID:String?=null

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
        binding=FragmentNotificationDetailBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        binding.list=notificationList

        notificationID=notificationList.notificationID
        getNotificationReaderStatus(notificationID)

        binding.backArrow.setOnClickListener(){
            dismiss()
            listener.onDialogDismissed(glitter_notification_lst)
        }
        return  view
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

    private fun getNotificationReaderStatus(notificationID: String?) {
        var request= GlitterNotificationRequest(notificationID)
        viewModel.GlitterNotificationResult(request)
        viewModel.glitterNotificationReaderResponse.observe(requireActivity()){
            when(it){
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    binding.progressCircular.isVisible = false
                }
            }
        }

    }

}