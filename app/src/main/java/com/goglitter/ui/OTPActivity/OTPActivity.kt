package com.goglitter.ui.OTPActivity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.goglitter.MainActivity
import com.goglitter.databinding.ActivityOtpactivityBinding
import com.goglitter.domain.request.OTPRequest
import com.goglitter.domain.request.VerifyOtpRequest
import com.goglitter.ui.BaseActivity
import com.goglitter.ui.Listener.DialogCallbackListener
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import com.goglitter.utils.TokenManager
import com.goglitter.utils.hideKeyboard
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
@author-Padma A
date-29/5/2023
 **/
@AndroidEntryPoint
class OTPActivity : BaseActivity(), DialogCallbackListener {

    lateinit var binding: ActivityOtpactivityBinding
    val viewModel: AuthViewModel by viewModels()
    private lateinit var dialog : OTPFragment
    var _phone:String?=null
    var _emailId:String?=null
    var _value:String?=null
    var _otp:String?=null
    var isLogin:Boolean=false

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent!=null){
            _emailId = intent.getStringExtra("email")
            _value = intent.getStringExtra("checkValue")
            _phone = intent.getStringExtra("phone")
            _otp = intent.getStringExtra("otp")
            isLogin = intent.getBooleanExtra("isLogin",false)
            getEmailOTP(_value,_emailId,_phone)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(android.R.color.transparent))
        }
        showCustomUI()

        binding.btnVerifyOtp.setOnClickListener(){
            hideKeyboard()
            val verifiedOtp=binding.otpView.otp.toString()
            if(!TextUtils.isEmpty(verifiedOtp) && !(verifiedOtp.equals(""))){
            VerifiedOTP(_value!!,verifiedOtp,_emailId!!,_phone!!)
            }else{
                showSnackBar("Please enter OTP")
            }
        }
        binding.btnResendOtp.setOnClickListener(){
            binding.otpView.setOTP("")
            showSnackBar("OTP sent Successfully.")
            getEmailOTP(_value,_emailId,_phone)
        }
    }
    private fun VerifiedOTP(_value: String, verifiedOtp: String, s1: String, _phone: String) {
        if(_value.equals("1")){
                val request = VerifyOtpRequest(_value,verifiedOtp,"",_phone)
                viewModel.getVerifiedOTPResponse(request)
            }else{
                val request = VerifyOtpRequest(_value,verifiedOtp,s1,"")
                viewModel.getVerifiedOTPResponse(request)
            }

        viewModel.verifyOtpResponse.observe(this) {
            viewModel.clear()
            when(it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }
                is NetworkResult.Failure -> {
                   showSnackBar(it.errorMessage)
                    Log.d("err",it.errorMessage)
                      binding.progressCircular.isVisible = false
                }
                is  NetworkResult.Success -> {

                    if(it.data.status.equals("fail")){
                        showSnackBar(it.data.msg!!)
                        binding.progressCircular.isVisible = false
                    }else{
                        binding.progressCircular.isVisible = false
                        showSnackBar(it.data.msg!!)
                        val _regID=it.data.result!!.regId
                        Log.d("ID",_regID.toString())
                        val name=it.data.result.regName
                        val email=it.data.result.regEmail
                        val phone=it.data.result.regMobile
                        val lastname=it.data.result.regLname
                        tokenManager.saveSalesId(_regID!!)
                        tokenManager.saveFullName(name!!)
                        tokenManager.saveEmail(email!!)
                        tokenManager.saveMobile(phone!!)

                        Handler().postDelayed({
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }, 1000)

                        // Log a custom event
                        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
                        val params = Bundle()
                        params.putString("register", "goGlitter")
                        mFirebaseAnalytics.logEvent("register_user_count", params)
                    }
                }
            }
        }
    }
    private fun showOTP(_otp: String?) {
           viewModel.clear()
            dialog = OTPFragment(this).
            newInstance("OTP","$_otp is your Glitter account verification OTP Code.","ok")
            dialog.show(supportFragmentManager, "signature")
    }

    override fun onDialogOk() {
        dialog.dismiss()
    }

    override fun onDialogCancel() {
        dialog.dismiss()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun getEmailOTP(_value: String?, _emailId: String?, _phone: String?) {
        if(!isLogin){
            if(_value.equals("1")){
                val request = OTPRequest(_value,"",_phone)
                viewModel.getOTPResponse(request)
            }else{
                val request = OTPRequest(_value,_emailId,"")
                viewModel.getOTPResponse(request)
            }
        }else{
            val request = OTPRequest(_value,_emailId,_phone)
            viewModel.getOTPResponse(request)
        }
        viewModel.otpResponse.observe(this) {
            when(it) {
                is NetworkResult.Loading -> {
                    //binding.progressCircular.isVisible = it.isLoading
                    binding.progressCircular.isVisible = it.isLoading
                }
                is NetworkResult.Failure -> {
                    showSnackBar(it.errorMessage)
                    binding.progressCircular.isVisible = false
                }
                is  NetworkResult.Success -> {
                    if(it.data.status.equals("success")){
                        binding.progressCircular.isVisible = false
                        val _resend_otp=it.data.result!!.get(0).otp.toString()

                    }else{
                        showSnackBar(it.data.msg!!)
                        binding.progressCircular.isVisible = false
                    }
                }
                else -> {}
            }
        }
    }

}

