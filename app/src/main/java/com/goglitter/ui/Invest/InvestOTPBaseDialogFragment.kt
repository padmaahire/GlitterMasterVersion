package com.goglitter.ui.Invest

import android.content.ContentValues
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.goglitter.R
import com.goglitter.databinding.FragmentOTPBaseDialogBinding
import com.goglitter.domain.request.InvestApplicationOTPRequest
import com.goglitter.ui.Listener.DialogCallbackListener
import com.goglitter.ui.Listener.GlitterDialogCallbackListener
import com.goglitter.ui.Loan.OTPDialogCallBackListener
import com.goglitter.ui.OTPActivity.DialogBaseFragment
import com.goglitter.ui.OTPActivity.OTPFragment
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.Constants
import com.goglitter.utils.NetworkResult
import com.goglitter.utils.TokenManager
import com.goglitter.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
@author-Padma A
date-13/02/2024
 **/
@AndroidEntryPoint
class InvestOTPBaseDialogFragment(private val listner: OTPDialogCallBackListener) : DialogFragment(),
    DialogCallbackListener, GlitterDialogCallbackListener {

    lateinit var binding: FragmentOTPBaseDialogBinding
    private var param1: String? = null
    private var param2: String? = null
    val viewModel: AuthViewModel by viewModels()
    private lateinit var dialog: OTPFragment
    private lateinit var glitterDialog: DialogBaseFragment

    var _value = ""
    var _phone = ""
    var _email = ""
    var mobile_otp = ""
    var email_otp = ""
    var _appID = ""
    var _investID = ""

    @Inject
    lateinit var tokenManager: TokenManager

    fun newInstance(
        value: String,
        investID: String,
        phone: String,
        email: String,
        investMobile: String,
        investEmail: String
    ): InvestOTPBaseDialogFragment {

        val args = Bundle()
        args.putString(Constants.KEY_OTP, value)
        args.putString(Constants.KEY_INVEST, investID)
        args.putString(Constants.KEY_OTP_FIELD_PHONE, phone)
        args.putString(Constants.KEY_OTP_FIELD_EMAIL, email)
        args.putString(Constants.KEY_OTP_PHONE, investMobile)
        args.putString(Constants.KEY_OTP_EMAIL, investEmail)
        val fragment = InvestOTPBaseDialogFragment(listner)
        fragment.arguments = args
        return fragment
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOTPBaseDialogBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        isCancelable = true

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
                _value = requireArguments().getString(Constants.KEY_OTP, "")
                _phone = requireArguments()!!.getString(Constants.KEY_OTP_FIELD_PHONE, "")
                _email = requireArguments()!!.getString(Constants.KEY_OTP_FIELD_EMAIL, "")
                mobile_otp = requireArguments()!!.getString(Constants.KEY_OTP_PHONE, "")
                email_otp = requireArguments()!!.getString(Constants.KEY_OTP_EMAIL, "")
                _appID = requireArguments()!!.getString(Constants.KEY_APP_ID, "")
                _investID = requireArguments()!!.getString(Constants.KEY_INVEST, "")

                if (_value == "1") {
                    //showOTP(mobile_otp)
                    showSnackBar("OTP sent successfully on your registered phone number.")
                    binding.otpLay.visibility = View.GONE
                }
                if (_value == "2") {
                    binding.otpLay.visibility = View.VISIBLE
                    binding.otpMobielLay.visibility = View.GONE

                    showSnackBar("OTP sent successfully on your Email Id")

                }
                if (_value == "3") {
                   // showOTP(mobile_otp)
                    showSnackBar("OTP sent successfully on your registered phone number.")
                    binding.otpLay.visibility = View.VISIBLE
                    binding.otpMobielLay.visibility = View.VISIBLE
                }
            }

            binding.btnVerifyOtp.setOnClickListener() {

                hideKeyboard()

                val verifiedOtp = binding.otpView.otp.toString()
                val verifiedEmailOtp = binding.otpViewEmail.otp.toString()

                when (_value) {
                    "1" -> {
                        if (!TextUtils.isEmpty(verifiedOtp) && !(verifiedOtp.equals(""))) {
                            verifyOTP(_value,_investID,verifiedOtp,"",_phone,"")
                        } else {
                            showSnackBar("Please enter OTP")
                        }

                    }
                    "2" -> {
                        if (!TextUtils.isEmpty(verifiedEmailOtp) && !(verifiedEmailOtp.equals(""))) {
                            verifyOTP(_value!!, _investID,"", verifiedEmailOtp, "", _email)
                        } else {

                            showSnackBar("Please enter OTP")
                        }
                    }
                    "3" -> {
                        if (!TextUtils.isEmpty(verifiedOtp) && !(verifiedOtp.equals(""))) {
                            if (!TextUtils.isEmpty(verifiedEmailOtp) && !(verifiedEmailOtp.equals(""))) {

                                verifyOTP(
                                    _value!!,
                                    _investID,
                                    verifiedOtp,
                                    verifiedEmailOtp,
                                    _phone,
                                    _email
                                )
                            } else {
                                showSnackBar("Please enter OTP")
                            }
                        } else {
                            showSnackBar("Please enter OTP")
                        }

                    }
                }

            }


        } catch (e: Exception) {
            Log.d(ContentValues.TAG, e.toString())
        }

    }

    private fun verifyOTP(
        value: String,
        investID: String,
        phoneotp: String,
        emailotp: String,
        phoneField: String,
        emailField: String
    ) {
        if (this._value.equals("1")) {
            val request = InvestApplicationOTPRequest(value,investID, phoneotp, "", phoneField, "")
            viewModel.InvestOTPResonse(request)
        } else if (this._value.equals("2")) {
            val request = InvestApplicationOTPRequest(value, investID,"", emailotp, "", emailField)
            viewModel.InvestOTPResonse(request)
        } else if (this._value.equals("3")) {
            val request = InvestApplicationOTPRequest(value, investID,phoneotp, emailotp, phoneField, emailField)
            viewModel.InvestOTPResonse(request)
        }
        viewModel.investOTPResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading

                }
                is NetworkResult.Failure -> {

                    showSnackBar(it.errorMessage)
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    if (it.data.status.equals("success")) {
                        binding.progressCircular.isVisible = false
                        showSnackBar(it.data.msg)
                        dismiss()
                        listner.verifyOTP()

                    } else {
                        binding.progressCircular.isVisible = false
                        showSnackBar(it.data.msg)
                        binding.otpView.otp.equals("")

                    }
                }
                else -> {}
            }
        }

    }

    private fun showOTP(_otp: String?) {
        viewModel.clear()
        dialog = OTPFragment(this).newInstance(
            "OTP",
            "$_otp is your Mobile account verification OTP Code.",
            "ok"
        )
        dialog.show(requireActivity()!!.supportFragmentManager, "signature")

    }

    override fun onDialogOk() {}

    override fun onDialogCancel() {}

    override fun DialogDone() {}

    override fun onCustomerDialogOk() {}

    override fun onCustomerDialogCancel() {}

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