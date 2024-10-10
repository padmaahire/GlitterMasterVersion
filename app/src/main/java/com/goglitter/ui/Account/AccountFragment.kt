package com.goglitter.ui.Account

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.goglitter.MainActivity
import com.goglitter.R
import com.goglitter.databinding.FragmentAccountBinding
import com.goglitter.domain.request.DeleteAccountRequest
import com.goglitter.ui.Listener.GlitterDialogCallbackListener
import com.goglitter.ui.OTPActivity.DialogBaseFragment
import com.goglitter.ui.Registration.LoginActivity
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.Constants
import com.goglitter.utils.NetworkResult
import com.goglitter.utils.TokenManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
@author-Padma A
date-2/6/2023
 **/

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class AccountFragment : Fragment(), View.OnClickListener, GlitterDialogCallbackListener,
    GlitterDeleteAccountCallbackListener {
    lateinit var binding: FragmentAccountBinding
    private var param1: String? = null
    private var param2: String? = null
    val viewModel: AuthViewModel by viewModels()
    var id: String = ""
    var mobile: String = ""
    var _profileImg: String?=null

    private lateinit var glitterDialog: DialogBaseFragment
    private lateinit var dialog: DeleteDialogBaseFragment

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
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        id = tokenManager.getSalesId().toString()
        getUser(id)

        binding.profileLay.setOnClickListener(this)
        binding.enquiryLay.setOnClickListener(this)
        binding.logoutLay.setOnClickListener(this)
        binding.deleteLay.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.profileLay -> {
                Navigation.findNavController(v).navigate(R.id.profileFragment)
            }
            R.id.enquiryLay -> {
                Navigation.findNavController(v).navigate(R.id.enquiryFragment)
            }
            R.id.logoutLay -> {
                Handler().postDelayed({
                    glitterDialog = DialogBaseFragment(this).newInstance(
                        R.drawable.ic_ok,
                        "Logout",
                        "Are you sure want to logout?",
                        "Yes",
                        "No",
                        true
                    )
                    glitterDialog.show(requireActivity()!!.supportFragmentManager, "signature")
                }, 200)

            }
            R.id.deleteLay->{
                Handler().postDelayed({
                    dialog= DeleteDialogBaseFragment(this).newInstance(
                        R.drawable.ic_ok,
                        "Delete Account",
                        "Are you sure want to delete account?",
                        "Yes",
                        "No",
                        true
                    )
                    dialog.show(requireActivity()!!.supportFragmentManager,"aa")
                },200)
            }
        }
    }
    override fun DialogDone() {}
    override fun onCustomerDialogOk() {
        tokenManager.saveSalesId("")
        tokenManager.saveEmail("")
        tokenManager.saveMobile("")
        startActivity(Intent(requireActivity(), LoginActivity::class.java))
        requireActivity().finishAffinity()
    }
    override fun onCustomerDialogCancel() {}
    private fun getUser(id: String) {
        viewModel.getUserResponse(id)
        viewModel.userProfileResponse.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }

                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    try {
                        if (it.data.result != null){
                            if(it.data.status.equals("success")){
                                if (it.data.result.profile_image != null && !it.data.result.profile_image.equals("")){
                                    val url = Constants.IMAGE_PATH + it.data.result.profile_image
                                    Log.e("TAG", "URL ====> $url")

                                     _profileImg= Constants.IMAGE_PATH + it.data.result.profile_image

                                    Glide.with(requireActivity())
                                        .load(Constants.IMAGE_PATH + it.data.result.profile_image)
                                        .into(binding.ivProfileImage)

                                    (activity as MainActivity).loadImg(_profileImg!!)

                                }else{
                                    binding.ivProfileImage.setImageResource(R.drawable.ic_avatar)
                                }
                                val fname = it.data.result!!.regName
                                val lname = it.data.result!!.regLname

                                binding.tvUserName.setText(fname + " " + lname)
                                binding.progressCircular.isVisible = false

                            }else{
                                Log.e("TAG", "ERROR")
                            }
                        }else{
                            Log.e("TAG", "ERROR")
                        }
                    }catch (e :Exception){
                        Log.e("TAG", e.toString())
                    }
                }
            }
        }
    }
    private fun accountDeleteStatus(regId: String?) {
        var request= DeleteAccountRequest(regId)
        viewModel.deleteUserAccount(request)

        viewModel.glitterDeleteResponse.observe(requireActivity()){
            when(it){
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    if(it.data.status=="200"){
                        binding.progressCircular.isVisible = false
                        showSnackBar(it.data.msg.toString())
                    }else{
                        showSnackBar(it.data.msg.toString())
                        //binding.progressCircular.isVisible = true
                    }
                }
            }
        }
    }

    override fun onDeleteAccountOk() {
        accountDeleteStatus(id)
        tokenManager.saveSalesId("")
        tokenManager.saveEmail("")
        tokenManager.saveMobile("")
        tokenManager.clear()

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }, 2000)


    }

    override fun onDeleteAccountCancel() {
    }
    open fun showSnackBar(message :String) {
        val rootView: View? = getRootView()
        if (rootView != null) Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
    }
    open fun getRootView(): View? {
        val contentViewGroup = activity?.findViewById<View>(android.R.id.content) as ViewGroup
        var rootView: View? = null
        if (contentViewGroup != null) rootView = contentViewGroup.getChildAt(0)
        if (rootView == null) rootView = activity?.window?.decorView?.rootView
        return rootView
    }
}