package com.goglitter.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.goglitter.MainActivity
import com.goglitter.databinding.FragmentNotificationBinding
import com.goglitter.domain.request.GlitterNotificationRequest
import com.goglitter.domain.response.GlitterNotificationList
import com.goglitter.ui.Jewellery.JwellaryFragment
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import com.goglitter.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
@author-Padma Ahire
date-10/08/2023
 **/
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
@AndroidEntryPoint
class NotificationFragment : Fragment(), NotificationsRVAdapter.OnItemClickListener,
    OnDialogDismissListener {
  private lateinit var binding: FragmentNotificationBinding
    private var param1: String? = null
    private var param2: String? = null
    lateinit var notificationAdapter: NotificationsRVAdapter
    var glitter_notification_lst = ArrayList<GlitterNotificationList>()
    val viewModel: AuthViewModel by viewModels()
    var _regId: String? = null
    @Inject
    lateinit var tokenManager: TokenManager
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
        binding=FragmentNotificationBinding.inflate(layoutInflater,container,false)
        val view=binding.root
        glitter_notification_lst = ArrayList()
        _regId = tokenManager.getSalesId()
        setupAdapter()
        return view
    }
    private fun setupAdapter() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvNotification.setLayoutManager(layoutManager)
        notificationAdapter = NotificationsRVAdapter(glitter_notification_lst,this)
        binding.rvNotification.setAdapter(notificationAdapter)
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
    override fun onNotificationDetailClick(notificationList: GlitterNotificationList) {
        getNotificationReaderStatus(notificationList.notificationID)
        val notificationDetailFragment= NotificationDetailFragment(notificationList!!,this)
        notificationDetailFragment.show(requireActivity().supportFragmentManager, notificationDetailFragment.getTag())
    }

    override fun onDialogDismissed(updatedList: List<GlitterNotificationList>) {
       getNotification()
    }
    private fun getNotification() {
        viewModel.glitterNotificationResult(_regId!!)
        viewModel.glitterNotificationResponse.observe(requireActivity()){
            when(it){
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }
                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    glitter_notification_lst=it.data.result as ArrayList<GlitterNotificationList>

                    if(glitter_notification_lst.size!=0){
                        binding.tvNotificationErr.visibility=View.GONE
                        notificationAdapter.updateNotification(glitter_notification_lst)
                        binding.progressCircular.isVisible = false
                    }else{

                        binding.tvNotificationErr.visibility=View.VISIBLE
                    }
                }
            }
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
                    notificationAdapter.updateNotification(glitter_notification_lst)
                    binding.progressCircular.isVisible = false
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getNotification()
        (activity as MainActivity?)!!.fetchNotification()
    }

    override fun onDetach() {
        super.onDetach()
        getNotification()
        (activity as MainActivity?)!!.fetchNotification()
    }

}