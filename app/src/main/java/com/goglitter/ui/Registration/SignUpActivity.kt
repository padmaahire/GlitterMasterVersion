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
import android.widget.TextView.BufferType
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.goglitter.R
import com.goglitter.databinding.ActivitySignUpBinding
import com.goglitter.domain.request.SignUpRequest
import com.goglitter.ui.BaseActivity
import com.goglitter.ui.DrawerMenu.PrivacyPolicyBottomSheetFragment
import com.goglitter.ui.DrawerMenu.TandCBottomSheetFragment
import com.goglitter.ui.OTPActivity.DialogBaseFragment
import com.goglitter.ui.OTPActivity.OTPActivity
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import com.goglitter.utils.TokenManager
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
@author-Padma A
date-29/5/2023
Updated-17/4/2024
 **/
@AndroidEntryPoint
class SignUpActivity : BaseActivity() {
    lateinit var binding: ActivitySignUpBinding
    var _name: String? = null
    var _value=0
    val viewModel: AuthViewModel by viewModels()
    private lateinit var glitterDialog: DialogBaseFragment
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

       customTextView(binding.tvPrivacy)
        showCustomUI()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(android.R.color.transparent))
        }
        _name = binding.edtName.toString().trim()
        binding.btnSignUp.isEnabled = false

        binding.edtName.addTextChangedListener(watcher)
        binding.edtPhoneNumber.addTextChangedListener(watcher)
        binding.edtEmailId.addTextChangedListener(watcher)
        val ps = Regex("^[a-zA-Z ]+$")
        binding.edtName.setFilters(arrayOf(
            InputFilter { src, start, end, dst, dstart, dend ->
                if (src == "") { // for backspace
                    return@InputFilter src
                }
                if (src.toString().matches(ps)) {
                    src
                } else ""
            }
        ))
        binding.edtLastName.setFilters(arrayOf(
            InputFilter { src, start, end, dst, dstart, dend ->
                if (src == "") { // for backspace
                    return@InputFilter src
                }
                if (src.toString().matches(ps)) {
                    src
                } else ""
            }
        ))

      /*  binding.btnSignUp.setOnClickListener() {

            if (!validateName() or !validateLastName() or !validatePhoneNmber() or !validateEmailAddr() or !validateCheckBox() or !validateOption()) {
                return@setOnClickListener

            } else {
                val code: String = binding.ccp.getSelectedCountryCodeWithPlus()
                val number: String = binding.edtPhoneNumber.getText().toString().trim()
                val phoneNumber = "$code$number"
                signUp(
                    binding.edtName.text.toString(),
                    binding.edtLastName.text.toString(),
                    number,
                    binding.edtEmailId.text.toString(),
                    _value.toString()
                )

            }

        }*/

        binding.btnSignUp.setOnClickListener() {

            val strEmail=binding.edtEmailId.text.toString()

            if(!strEmail.isNullOrEmpty()){
              //  binding.checkboxEmail.isEnabled=true
                if (!validateName() or !validateLastName() or !validatePhoneNmber() or !validateEmailAddr() or !validateCheckBox() or !validateOption()) {
                    return@setOnClickListener
                } else {
                    val code: String = binding.ccp.getSelectedCountryCodeWithPlus()
                    val number: String = binding.edtPhoneNumber.getText().toString().trim()
                    val phoneNumber = "$code$number"
                    signUp(
                        binding.edtName.text.toString(),
                        binding.edtLastName.text.toString(),
                        number,
                        binding.edtEmailId.text.toString(),
                        _value.toString()
                    )

                }
            }else{
               // binding.checkboxEmail.isEnabled=false
                if (!validateName() or !validateLastName() or !validatePhoneNmber() or !validateCheckBox() or !validateOption()) {
                    return@setOnClickListener

                } else {
                    val code: String = binding.ccp.getSelectedCountryCodeWithPlus()
                    val number: String = binding.edtPhoneNumber.getText().toString().trim()
                    val phoneNumber = "$code$number"
                    signUp(
                        binding.edtName.text.toString(),
                        binding.edtLastName.text.toString(),
                        number,
                        binding.edtEmailId.text.toString(),
                        _value.toString()
                    )

                }
            }

        }
        binding.tvNext.setOnClickListener() {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.checkboxEmail.setOnClickListener() {
            binding.checkboxEmail.isChecked = true
            binding.checkboxPhone.isChecked = false
        }
        binding.checkboxPhone.setOnClickListener() {
            binding.checkboxPhone.isChecked = true
            binding.checkboxEmail.isChecked = false
        }
    }
    private fun validateOption(): Boolean {
        if (binding.checkboxEmail.isChecked == true) {
            _value=2
            return true
        } else if (binding.checkboxPhone.isChecked == true) {
            _value=1
            return true
        } else {
            showSnackBar("Click the checkbox to receiving OTP")
            return false
        }
    }

    private fun signUp(name: String, lastname:String,phone: String, email: String, _value: String) {
        val request = SignUpRequest(name, lastname,phone, email, _value)
        viewModel.getUserResponse(request)
        viewModel.userResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                is NetworkResult.Failure -> {
                    binding.progressCircular.visibility = View.GONE
                    showSnackBar(it.errorMessage)
                }
                is NetworkResult.Success -> {
                    if (it.data.status.equals("fail")) {
                        val msg = it.data.msg
                        showSnackBar(msg)
                        binding.progressCircular.visibility = View.GONE
                    } else {
                        binding.progressCircular.visibility = View.VISIBLE
                        showSnackBar(it.data.msg)
                        val handler = Handler()
                        handler.postDelayed({
                            val intent = Intent(this, OTPActivity::class.java)
                            intent.putExtra("phone", phone)
                            intent.putExtra("email", email)
                            intent.putExtra("checkValue", _value)
                            intent.putExtra("isLogin",false)
                            startActivity(intent)
                        }, 2000)


                    }
                }
            }
        }
    }
    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            val hasSpace = s.contains(" ")
            if (binding.edtName.text.isNotEmpty() && hasSpace) {
                binding.tvErrName.visibility = View.VISIBLE
            } else {
                binding.tvErrName.visibility = View.GONE
            }

            if (binding.edtLastName.text.isNotEmpty() && hasSpace) {
                binding.tvErrLastName.visibility = View.GONE
            }
            if (binding.edtPhoneNumber.text.isNotEmpty()) {
                binding.tvErrPhone.visibility = View.GONE
            }
            if (binding.edtEmailId.text.isNotEmpty()) {
                binding.checkboxEmail.isEnabled=true
                binding.tvErrEmail.visibility = View.GONE
            }

        }
        override fun afterTextChanged(s: Editable) {
            if (binding.edtName.text.length == 0 && binding.edtLastName.text.length == 0 && binding.edtPhoneNumber.text.length == 0 && binding.edtEmailId.text.length == 0 && binding.checkbox.isChecked == false) {
                binding.btnSignUp.isEnabled = false
            } else {
                binding.btnSignUp.isEnabled = true
                binding.btnSignUp.setBackgroundResource(R.drawable.button)

            }

            if(binding.edtEmailId.text.length == 0 ){
                binding.checkboxEmail.isEnabled=false
                binding.checkboxEmail.isChecked=false
            }else{
                binding.checkboxEmail.isEnabled=true
            }
        }

    }
    fun validateName(): Boolean {
        val mobile = binding.edtName.text.toString().trim()
        if (!TextUtils.isEmpty(mobile)) {
            binding.tvErrName.visibility=View.GONE
            return true
        } else {
            binding.tvErrName.visibility=View.VISIBLE
            binding.tvErrName.setText(R.string.error_name)
            return false
        }
    }
    fun validateLastName(): Boolean {
        val mobile = binding.edtLastName.text.toString().trim()
        if (!TextUtils.isEmpty(mobile)) {
            binding.tvErrLastName.visibility=View.GONE
            return true
        } else {
            binding.tvErrLastName.visibility=View.VISIBLE
            binding.tvErrLastName.setText(R.string.error_last_name)
            return false
        }
    }
    fun validatePhone(): Boolean {
        val MobilePattern = Regex("[0-9]{10}")
        val mobile = binding.edtPhoneNumber.text.toString().trim()
        if (!TextUtils.isEmpty(mobile) && mobile.matches(MobilePattern)) {
            binding.tvErrPhone.visibility=View.GONE
            return true
        } else {
            binding.tvErrPhone.visibility=View.VISIBLE
            binding.tvErrPhone.setText(R.string.error_mobile)
            return false
        }
    }

    fun validateEmail(): Boolean {
        val email = binding.edtEmailId.text.toString().trim()
        if (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) {
            binding.edtEmailId.error = null
            binding.tvErrEmail.visibility=View.GONE
            return true
        } else {

            binding.tvErrEmail.visibility=View.VISIBLE
            binding.tvErrEmail.setText(R.string.error_email)
            return false
        }
    }

    fun validateCheckBox(): Boolean {
        if (binding.checkbox.isChecked == true) {
            binding.checkbox.error = null
            return true
        } else {
            showSnackBar("Terms of Services & Privacy Policy should be check")
            return false
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
                _dialog= TandCBottomSheetFragment()
                _dialog.show(supportFragmentManager, "BSDialogFragment")


            }
        }, spanTxt.length - "Terms of Services".length, spanTxt.length, 0)
        spanTxt.append(" and")
        spanTxt.append(" Privacy Policy")
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                lateinit var _dialog: PrivacyPolicyBottomSheetFragment
                _dialog= PrivacyPolicyBottomSheetFragment()
                _dialog.show(supportFragmentManager, "BSDialogFragment")
            }

        }, spanTxt.length - " Privacy Policy".length, spanTxt.length, 0)
        spanTxt.append(" of GoGlitter Avenues Pvt. Ltd.")
        view.movementMethod = LinkMovementMethod.getInstance()
        view.setText(spanTxt, BufferType.SPANNABLE)
    }

   /* fun validateEmailAddr(): Boolean {
        val email = binding.edtEmailId.text.toString().trim()
        if (!TextUtils.isEmpty(email)) {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmailId.error = null
                return true
            } else {
                binding.tvErrEmail.visibility = View.VISIBLE
                binding.tvErrEmail.setText("Please enter valid email address")
                return false
            }
        } else {
            binding.tvErrEmail.visibility = View.VISIBLE
            binding.tvErrEmail.setText(R.string.error_email)
            return false
        }
    }
*/

    @SuppressLint("SuspiciousIndentation")
    fun validateEmailAddr(): Boolean {
        val email = binding.edtEmailId.text.toString().trim()
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmailId.error = null
                return true
            } else {
                binding.tvErrEmail.visibility = View.VISIBLE
                binding.tvErrEmail.setText("Please enter valid email address")
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
                binding.tvErrPhone.visibility = View.VISIBLE
                binding.tvErrPhone.setText("Please enter valid phone number.")
                return false
            }

        } else {
            binding.tvErrPhone.visibility = View.VISIBLE
            binding.tvErrPhone.setText(R.string.error_mobile)
            return false
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}