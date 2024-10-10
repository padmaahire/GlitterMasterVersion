package com.goglitter.ui.OTPActivity

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
import com.goglitter.domain.request.JewelleryApplicationOTPRequest
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


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class JewelleryOTPBaseDialogFragment(private val listner: OTPDialogCallBackListener) : DialogFragment(),
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

    @Inject
    lateinit var tokenManager: TokenManager

    fun newInstance(
        value: String,
        phone: String,
        email: String,
        appID: String,
        otp_phone: String,
        otp_email: String
    ): JewelleryOTPBaseDialogFragment {

        val args = Bundle()
        args.putString(Constants.KEY_OTP, value)
        args.putString(Constants.KEY_OTP_FIELD_PHONE, phone)
        args.putString(Constants.KEY_OTP_FIELD_EMAIL, email)
        args.putString(Constants.KEY_APP_ID, appID)
        args.putString(Constants.KEY_OTP_PHONE, otp_phone)
        args.putString(Constants.KEY_OTP_EMAIL, otp_email)
        val fragment = JewelleryOTPBaseDialogFragment(listner)
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

                if (_value == "1") {
                   // showOTP(mobile_otp)
                    binding.otpLay.visibility = View.GONE
                    showSnackBar("OTP sent successfully on your registered phone number.")
                }
                if (_value == "2") {
                    binding.otpLay.visibility = View.VISIBLE
                    binding.otpMobielLay.visibility = View.GONE
                    showSnackBar("OTP sent on your Email Id")

                }
                if (_value == "3") {
                   // showOTP(mobile_otp)
                    binding.otpLay.visibility = View.VISIBLE
                    binding.otpMobielLay.visibility = View.VISIBLE
                    showSnackBar("OTP sent successfully on your registered phone number.")
                }
            }

            binding.btnVerifyOtp.setOnClickListener() {

                hideKeyboard()

                val verifiedOtp = binding.otpView.otp.toString()
                val verifiedEmailOtp = binding.otpViewEmail.otp.toString()


                when (_value) {
                    "1" -> {
                        if (!TextUtils.isEmpty(verifiedOtp) && !(verifiedOtp.equals(""))) {
                            verifyOTP(_value!!, verifiedOtp, "", _phone, "", _appID)
                        } else {
                            showSnackBar("Please enter OTP")
                        }

                    }
                    "2" -> {
                        if (!TextUtils.isEmpty(verifiedEmailOtp) && !(verifiedEmailOtp.equals(""))) {
                            verifyOTP(_value!!, "", verifiedEmailOtp, "", _email, _appID)
                        } else {

                            showSnackBar("Please enter OTP")
                        }
                    }
                    "3" -> {
                        if (!TextUtils.isEmpty(verifiedOtp) && !(verifiedOtp.equals(""))) {
                            if (!TextUtils.isEmpty(verifiedEmailOtp) && !(verifiedEmailOtp.equals(""))) {

                                verifyOTP(
                                    _value!!,
                                    verifiedOtp,
                                    verifiedEmailOtp,
                                    _phone,
                                    _email,
                                    _appID
                                )
                            } else {
                                showSnackBar("Please enter OTP")
                            }
                        } else {
                            showSnackBar("Please enter OTP")
                        }

                    }
                }
/*
                if(!TextUtils.isEmpty(verifiedOtp) && !(verifiedOtp.equals(""))){
                    verifyOTP(_value!!,mobile_otp,email_otp,_phone,_email,_appID)
                }else if(!TextUtils.isEmpty(verifiedEmailOtp) && !(verifiedEmailOtp.equals(""))){
                    verifyOTP(_value!!,mobile_otp,email_otp,_phone,_email,_appID)
                }else{
                    Toast.makeText(requireContext(),"Please enter OTP", Toast.LENGTH_SHORT).show()
                }*/

            }


        } catch (e: Exception) {
            Log.d(ContentValues.TAG, e.toString())
        }

    }

    private fun verifyOTP(
        value: String,
        phoneotp: String?,
        emailotp: String?,
        phoneField: String,
        emailField: String,
        _appID: String
    ) {
        if (this._value.equals("1")) {
            val request = JewelleryApplicationOTPRequest(value, phoneotp, "", phoneField, "", _appID)
            viewModel.JewelleryOTPResonse(request)
        } else if (this._value.equals("2")) {
            val request = JewelleryApplicationOTPRequest(value, "", emailotp, "", emailField, _appID)
            viewModel.JewelleryOTPResonse(request)
        } else if (_value.equals("3")) {
            val request =
                JewelleryApplicationOTPRequest(value, phoneotp, emailotp, phoneField, emailField, _appID)
            viewModel.JewelleryOTPResonse(request)
        }
        viewModel.jewelleryOTPResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                    //   binding.progressCircular.isVisible = false
                }
                is NetworkResult.Failure -> {
                    //Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
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
                       // Toast.makeText(requireActivity(), it.data.msg, Toast.LENGTH_SHORT).show()
                        showSnackBar(it.data.msg)
                        binding.otpView.otp.equals("")
                       // dismiss()
                    }
                    /* if(it.data.status.equals("fail")){
                         Toast.makeText(requireActivity(),it.data.msg, Toast.LENGTH_SHORT).show()
                         binding.progressCircular.isVisible = false
                     }else{
                         binding.progressCircular.isVisible = false
                         Toast.makeText(requireActivity(),it.data.msg, Toast.LENGTH_SHORT).show()
                     }*/
                }
                else -> {}
            }
        }

    }

    private fun showOTP(_otp: String?) {
        viewModel.clear()
        //  Toast.makeText(requireActivity(),_otp,Toast.LENGTH_SHORT).show()
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