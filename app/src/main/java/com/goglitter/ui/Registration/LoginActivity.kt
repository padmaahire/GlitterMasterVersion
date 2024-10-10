package com.goglitter.ui.Registration

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.goglitter.R
import com.goglitter.databinding.ActivityLoginBinding
import com.goglitter.domain.request.OTPRequest
import com.goglitter.ui.BaseActivity
import com.goglitter.ui.DrawerMenu.PrivacyPolicyBottomSheetFragment
import com.goglitter.ui.DrawerMenu.TandCBottomSheetFragment
import com.goglitter.ui.OTPActivity.OTPActivity
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
updated-17/04/24
 **/
@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    var _phone: String? = null
    var _value: String? = null
    var _regId: String? = null
    val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.edtPhoneNumber.addTextChangedListener(watcher)
        binding.edtEmailId.addTextChangedListener(watcher)
        _value = "2"

        showCustomUI()
        customTextView(binding.tvPrivacy)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(android.R.color.transparent))
        }


        binding.rdBtnEmail.setOnClickListener() {
            hideKeyboard()
            _value = "2"
            tokenManager.saveValue(_value!!)
            binding.edtEmailId.visibility = View.VISIBLE
            binding.layPhone.visibility = View.GONE
            if (!TextUtils.isEmpty(binding.edtEmailId.text.toString().trim())) {
                binding.tvErr.setText("Mobile number is mandatory.")
            } else {
                binding.tvErr.setText("Email Id is mandatory.")
            }

        }

        binding.rdBtnPhone.setOnClickListener() {
            hideKeyboard()
            _value = "1"
            tokenManager.saveValue(_value!!)
            binding.edtEmailId.visibility = View.GONE
            binding.layPhone.visibility = View.VISIBLE
            if (!TextUtils.isEmpty(binding.edtPhoneNumber.text.toString().trim())) {
                binding.tvErr.setText("Email Id is mandatory.")
            } else {
                binding.tvErr.setText("Mobile number is mandatory.")
            }
        }
        binding.btnGetOTP.setOnClickListener() {
            hideKeyboard()
            var checkValue = "2"
            var email_id = ""
            var phone_no = ""

            if (binding.rdBtnEmail.isChecked) {
                checkValue = "2"
                email_id = binding.edtEmailId.text.toString()

                if (!validateEmailAddr()) {
                    return@setOnClickListener
                } else {
                    getEmailOTP(checkValue, email_id, phone_no)
                }
            } else if (binding.rdBtnPhone.isChecked) {
                checkValue = "1"
                val code: String = binding.ccp.getSelectedCountryCodeWithPlus()
                val number: String = binding.edtPhoneNumber.getText().toString().trim()
                val phoneNumber = "$code$number"
                //  phone_no=binding.edtPhoneNumber.text.toString()
                if (!validatePhoneNmber()) {
                    return@setOnClickListener
                } else {
                   // getEmailOTP(checkValue, email_id, number)
                   val handler = Handler()
                    handler.postDelayed({
                        val intent = Intent(this, OTPActivity::class.java)
                        intent.putExtra("phone", number)
                        intent.putExtra("email", email_id)
                        intent.putExtra("checkValue", _value)
                        intent.putExtra("isLogin",false)
                        startActivity(intent)
                    }, 2000)
                }
            }
        }

        binding.tvNext.setOnClickListener() {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }
    private fun getEmailOTP(_value: String?, _emailId: String?, _phone: String?) {

        if (_value.equals("1")) {
            val request = OTPRequest(_value, "", _phone)
            viewModel.getOTPResponse(request)
        } else {
            val request = OTPRequest(_value, _emailId, "")
            viewModel.getOTPResponse(request)
        }

        viewModel.otpResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is NetworkResult.Failure -> {
                 showSnackBar(it.errorMessage.toString())
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    if (it.data.status.equals("success")) {
                        viewModel.clear()
                        binding.progressCircular.isVisible = false
                        showSnackBar(it.data.msg!!)
                        Handler().postDelayed({
                            val _otp = it.data.result!!.get(0).otp
                            if (_otp != null) {
                                val intent = Intent(this, OTPActivity::class.java)
                                intent.putExtra("email", _emailId)
                                intent.putExtra("phone", _phone)
                                intent.putExtra("otp", _otp)
                                intent.putExtra("checkValue", _value)
                                intent.putExtra("isLogin", true)
                                startActivity(intent)
                            } else {
                                showSnackBar(it.data.msg!!)
                            }
                        }, 2000)
                    } else {
                        showSnackBar(it.data.msg!!)
                        binding.progressCircular.isVisible = false
                    }

                }
                else -> {}
            }
        }
    }

    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            if (binding.edtEmailId.text.length != 0) {
                binding.tvErr.visibility = View.GONE
            }
            if (binding.edtPhoneNumber.text.length != 0) {
                binding.tvErr.visibility = View.GONE
            }


        }

        override fun afterTextChanged(s: Editable) {

        }

    }

    private fun customTextView(view: TextView) {
        val spanTxt = SpannableStringBuilder(
            "By logging in or registering, you agree to the "
        )
        spanTxt.append("Terms of Services")
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                lateinit var _dialog: TandCBottomSheetFragment
                _dialog = TandCBottomSheetFragment()
                _dialog.show(supportFragmentManager, "BSDialogFragment")
            }
        }, spanTxt.length - "Terms of Services".length, spanTxt.length, 0)
        spanTxt.append(" and")
        spanTxt.append(" Privacy Policy")
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                lateinit var _dialog: PrivacyPolicyBottomSheetFragment
                _dialog = PrivacyPolicyBottomSheetFragment()
                _dialog.show(supportFragmentManager, "BSDialogFragment")

            }

        }, spanTxt.length - " Privacy Policy".length, spanTxt.length, 0)
        spanTxt.append(" of GoGlitter Avenues Pvt. Ltd.")
        view.movementMethod = LinkMovementMethod.getInstance()
        view.setText(spanTxt, TextView.BufferType.SPANNABLE)

    }

    fun validateEmailAddr(): Boolean {
        val email = binding.edtEmailId.text.toString().trim()
        if (!TextUtils.isEmpty(email)) {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmailId.error = null
                return true
            } else {
                binding.tvErr.visibility = View.VISIBLE
                binding.tvErr.setText("Please enter valid email address")
                return false
            }
        } else {
            binding.tvErr.visibility = View.VISIBLE
            binding.tvErr.setText(R.string.error_email)
            return false
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun validatePhoneNmber(): Boolean {
        val _mobile = binding.edtPhoneNumber.text.toString().trim()
        val MobilePattern = Regex("[0-9]{10}")

        if (!TextUtils.isEmpty(_mobile)) {
            if (_mobile.matches(MobilePattern)) {
                return true
            } else {
                binding.tvErr.visibility = View.VISIBLE
                binding.tvErr.setText("Please enter valid phone number.")
                return false
            }
        } else {
            binding.tvErr.visibility = View.VISIBLE
            binding.tvErr.setText(R.string.error_mobile)
            return false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}

