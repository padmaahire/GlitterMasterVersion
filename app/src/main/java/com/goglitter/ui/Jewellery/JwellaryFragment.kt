package com.goglitter.ui.Jewellery

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.goglitter.R
import com.goglitter.databinding.FragmentJwellaryBinding
import com.goglitter.domain.SupportList
import com.goglitter.domain.request.JewellRequest
import com.goglitter.domain.request.LeadRequest
import com.goglitter.domain.response.GlitterSponsors
import com.goglitter.domain.response.LoanBanner
import com.goglitter.ui.DrawerMenu.PrivacyPolicyBottomSheetFragment
import com.goglitter.ui.DrawerMenu.TandCBottomSheetFragment
import com.goglitter.ui.Listener.GlitterDialogCallbackListener
import com.goglitter.ui.Loan.OTPDialogCallBackListener
import com.goglitter.ui.Loan.SliderAdapter
import com.goglitter.ui.OTPActivity.DialogBaseFragment
import com.goglitter.ui.OTPActivity.JewelleryOTPBaseDialogFragment
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import com.goglitter.utils.TokenManager
import com.google.android.material.snackbar.Snackbar
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

/**
@author-Padma A
date-2/6/2023
updated-25/04/24
 add jewellery application on apply now button
 **/

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint

class JwellaryFragment : Fragment(),View.OnClickListener {
    lateinit var binding: FragmentJwellaryBinding
    private var param1: String? = null
    private var param2: String? = null
    lateinit var img_lst: ArrayList<SupportList>
    val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    var id: String = ""
    var mobile: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentJwellaryBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        id = tokenManager.getSalesId().toString()
        binding.btnJwelleryApply.setOnClickListener(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        img_lst = ArrayList()
        img_lst.add(SupportList("", R.drawable.img_support))

    }
    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.btnJwellery_apply->{
                Navigation.findNavController(v)
                    .navigate(R.id.action_nav_jwellery_to_jewelleryApplicationFragment)
            }
        }
    }
    override fun onResume() {
        super.onResume()
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
/*class JwellaryFragment : Fragment(),View.OnClickListener, GlitterDialogCallbackListener,
    OTPDialogCallBackListener {
    lateinit var binding: FragmentJwellaryBinding
    private var param1: String? = null
    private var param2: String? = null
    lateinit var img_lst: ArrayList<SupportList>
    var crm_lst: ArrayList<LeadRequest> = arrayListOf()

    var bannerList: ArrayList<LoanBanner> = arrayListOf()
    lateinit var sliderAdapter: SliderAdapter
    val viewModel: AuthViewModel by viewModels()
    var sponosorsList: ArrayList<GlitterSponsors> = arrayListOf()

    var emailID: String = ""
    var otp_phone: String? = null
    var otp_email: String = ""
    var appID: String = ""

    private lateinit var glitterDialog: DialogBaseFragment
    private var firstName: String? = null
    private var LastName: String? = null
    private var login_ID: String? = null
    private lateinit var otpDialog: JewelleryOTPBaseDialogFragment

    @Inject
    lateinit var tokenManager: TokenManager

    var id: String = ""
    var mobile: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentJwellaryBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        val textToUnderline = "Edit"
        var underlinedText = SpannableString(textToUnderline)
        underlinedText.setSpan(UnderlineSpan(), 0, textToUnderline.length, 0)
        binding.tvEdit.text = underlinedText
        binding.tvEditEmail.text = underlinedText

        id = tokenManager.getSalesId().toString()
        getUser(id)
        binding.btnSubmit.setOnClickListener(this)
        binding.tvEdit.setOnClickListener(this)
        binding.tvEditEmail.setOnClickListener(this)
        binding.btnJwelleryApply.setOnClickListener(this)
        getAllLoanBanners()

        sliderAdapter = SliderAdapter(bannerList)
        binding.slider.setSliderAdapter(sliderAdapter)
        binding.slider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.slider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.slider.setScrollTimeInSec(2); //set scroll delay in seconds :
        binding.slider.startAutoCycle();

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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSponsored()
        img_lst = ArrayList()
        img_lst.add(SupportList("", R.drawable.img_support))

    }
    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.btnJwellery_apply->{
                Navigation.findNavController(v)
                    .navigate(R.id.action_nav_jwellery_to_jewelleryApplicationFragment)
            }

         *//*   R.id.btnSubmit -> {
                if (!validateName() or !validateLastName() or !validatePhoneNmber() or !validateEmailAddr()) {
                    return
                } else {
                    val code: String = binding.ccp.getSelectedCountryCodeWithPlus()
                    val number: String = binding.edtPhoneNumber.getText().toString().trim()
                    val phoneNumber = "$code$number"

                    LoanDetail(
                        binding.edtName.text.toString(),
                        id,
                        binding.edtLastName.text.toString(),
                        binding.edtEmailId.text.toString(),
                        number
                    )
                }
            }

            R.id.tvEdit->{
                binding.edtPhoneNumber.setSelection(binding.edtPhoneNumber.text.length)
                binding.edtPhoneNumber.inputType = EditorInfo.TYPE_CLASS_PHONE
                binding.edtPhoneNumber.requestFocus()

                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(binding.edtPhoneNumber, InputMethodManager.SHOW_IMPLICIT)

            }
            R.id.tvEditEmail->{
                binding.edtEmailId.setSelection(binding.edtEmailId.text.length)
                binding.edtEmailId.inputType = EditorInfo.TYPE_CLASS_TEXT
                binding.edtEmailId.requestFocus()

                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(binding.edtEmailId, InputMethodManager.SHOW_IMPLICIT)
            }*//*
        }
    }


    override fun DialogDone() {
        Navigation.findNavController(requireView()!!).navigate(R.id.action_nav_jwellery_to_nav_home)

    }
    override fun onCustomerDialogOk() {}
    override fun onCustomerDialogCancel() {}

    private fun LoanDetail(
        name: String,
        id: String,
        lname: String,
        email: String,
        phone: String
    ) {
        val request = JewellRequest(name, id, lname, email, phone)
        viewModel.getApplicationUserResponse(request)

        viewModel.jewellerUserResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }
                is NetworkResult.Failure -> {
                    showSnackBar(it.errorMessage.toString())
                    //  Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    if (it.data.status.equals("success")) {
                        viewModel.clear()
                        try {
                            binding.progressCircular.isVisible = false
                            if (it.data.result.verfiyMob.equals("1") && it.data.result.verfiyEmail.equals(
                                    "1"
                                )
                            ) {
                                binding.progressCircular.isVisible = false
                                glitterDialog = DialogBaseFragment(this).newInstance(
                                    R.drawable.ic_ok,
                                    "Thank You",
                                    " Thank you for submitting your details. \n" +
                                            " We will reach out to you soon",
                                    "Done",
                                    "No",
                                    false
                                )
                                glitterDialog.show(
                                    requireActivity().supportFragmentManager,
                                    "signature"
                                )
                            } else if (it.data.result.verfiyMob.equals("0") && it.data.result.verfiyEmail.equals(
                                    "1"
                                )
                            ) {
                                binding.progressCircular.isVisible = false

                                var _otp_phone = it.data.result.otp!!
                                var _appID = it.data.result.jewId!!

                                otpDialog = JewelleryOTPBaseDialogFragment(this).newInstance(
                                    "1",
                                    phone,
                                    "",
                                    _appID,
                                    _otp_phone,
                                    ""
                                )
                                otpDialog.show(
                                    requireActivity()!!.supportFragmentManager,
                                    "signature"
                                )

                            } else if (it.data.result.verfiyMob.equals("1") && it.data.result.verfiyEmail.equals(
                                    "0"
                                )
                            ) {
                                binding.progressCircular.isVisible = false
                                otp_email = it.data.result.emailotp
                                appID = it.data.result.jewId
                                otpDialog = JewelleryOTPBaseDialogFragment(this).newInstance(
                                    "2",
                                    "",
                                    email,
                                    appID,
                                    "",
                                    otp_email
                                )
                                otpDialog.show(
                                    requireActivity()!!.supportFragmentManager,
                                    "signature"
                                )

                            } else if (it.data.result.verfiyMob.equals("0") && it.data.result.verfiyEmail.equals(
                                    "0"
                                )
                            ) {
                                binding.progressCircular.isVisible = false

                                otp_phone = it.data.result.otp
                                otp_email = it.data.result.emailotp
                                appID = it.data.result.jewId
                                otpDialog = JewelleryOTPBaseDialogFragment(this).newInstance(
                                    "3",
                                    phone,
                                    email,
                                    appID,
                                    otp_phone!!,
                                    otp_email
                                )
                                otpDialog.show(
                                    requireActivity()!!.supportFragmentManager,
                                    "signature"
                                )
                            }
                        } catch (e: Exception) {
                            showSnackBar(e.message.toString())
                        }
                    } else {
                        binding.progressCircular.isVisible = false
                        showSnackBar(it.data.msg.toString())
                    }

                }
                else -> {}
            }
        }
    }

    private fun getSponsored() {
        viewModel.getSponsoredResponse()
        viewModel.sponsorResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    // binding.progressCircular.isVisible = it.isLoading
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Failure -> {
                    showSnackBar(it.errorMessage.toString())
                    //  Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    sponosorsList = it.data.result as ArrayList<GlitterSponsors>
                    val img=sponosorsList.get(0).logoImage
                    Glide.with(requireView()).load(img).into(binding.imgSponsored)
                    Log.d("lst", sponosorsList.toString())
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }
    }

    private fun getAllLoanBanners() {
        viewModel.getBannerList()
        viewModel.bannerResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                    // binding.progressCircular.isVisible = false
                }
                is NetworkResult.Failure -> {
                    // Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    bannerList = it.data.result as ArrayList<LoanBanner>
                    Log.d("lst", bannerList.toString())
                    sliderAdapter.updateBanner(bannerList)
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
    }
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
                    if (it.data.result != null) {
                        binding.edtName.setText(it.data.result!!.regName)
                        binding.edtLastName.setText(it.data.result.regLname)
                        binding.edtEmailId.setText(it.data.result!!.regEmail)

                        mobile = it.data.result!!.regMobile.toString()
                        binding.edtPhoneNumber.setText(mobile)
                    } else {
                        showSnackBar(it.data.msg.toString())
                    }

                }
            }
        }

    }

    fun validateCheckBox(): Boolean {
        if (binding.checkbox.isChecked == true) {
            binding.checkbox.error = null
            return true
        } else {
            showSnackBar("Terms of Services should be checked")
            return false
        }
    }

    fun validateLastName(): Boolean {
        val mobile = binding.edtLastName.text.toString().trim()
        if (!TextUtils.isEmpty(mobile)) {
            binding.tvErrLastName.visibility = View.GONE
            return true
        } else {
            binding.tvErrLastName.visibility = View.VISIBLE
            binding.tvErrLastName.setText(R.string.error_last_name)
            return false
        }
    }

    fun validateName(): Boolean {
        val mobile = binding.edtName.text.toString().trim()
        if (!TextUtils.isEmpty(mobile)) {
            binding.tvErrName.visibility = View.GONE
            return true
        } else {
            binding.tvErrName.visibility = View.VISIBLE
            binding.tvErrName.setText(R.string.error_name)
            return false
        }
    }

    fun validatePhone(): Boolean {
        val MobilePattern = Regex("[0-9]{10}")
        val mobile = binding.edtPhoneNumber.text.toString().trim()
        if (!TextUtils.isEmpty(mobile) && mobile.matches(MobilePattern)) {
            binding.tvErrPhone.visibility = View.GONE
            return true
        } else {
            binding.tvErrPhone.visibility = View.VISIBLE
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
            binding.tvErrEmail.visibility = View.GONE
            return true
        } else {
            binding.tvErrEmail.visibility = View.VISIBLE
            binding.tvErrEmail.setText(R.string.error_email)
            return false
        }
    }

    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            if (binding.edtName.text.length != 0) {
                binding.tvErrName.visibility = View.GONE
            }
            if (binding.edtPhoneNumber.text.length != 0) {
                binding.tvErrPhone.visibility = View.GONE
            }
            if (binding.edtEmailId.text.length != 0) {
                binding.tvErrEmail.visibility = View.GONE
            }

        }

        override fun afterTextChanged(s: Editable) {
            if (binding.edtName.text.length == 0 && binding.edtPhoneNumber.text.length == 0 && binding.edtEmailId.text.length == 0 && binding.checkbox.isChecked == false) {
                binding.btnSubmit.isEnabled = false

            } else {
                binding.btnSubmit.isEnabled = true
                binding.btnSubmit.setBackgroundResource(R.drawable.button)

            }
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

                _dialog.show(activity!!.supportFragmentManager, "BSDialogFragment")


            }
        }, spanTxt.length - "Terms of Services".length, spanTxt.length, 0)
        spanTxt.append(" and")
        // spanTxt.setSpan(ForegroundColorSpan(Color.BLACK), 32, spanTxt.length, 0)
        spanTxt.append(" Privacy Policy")
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {

                lateinit var _dialog: PrivacyPolicyBottomSheetFragment
                _dialog = PrivacyPolicyBottomSheetFragment()
                _dialog.show(activity!!.supportFragmentManager, "BSDialogFragment")

            }

        }, spanTxt.length - " Privacy Policy".length, spanTxt.length, 0)
        spanTxt.append(" of GoGlitter Avenues Pvt. Ltd")

        view.movementMethod = LinkMovementMethod.getInstance()
        view.setText(spanTxt, TextView.BufferType.SPANNABLE)


    }

    override fun verifyOTP() {
        glitterDialog = DialogBaseFragment(this).newInstance(
            R.drawable.ic_ok,
            "Thank You",
            "Thank you for submitting your details. \n" +
                    " We will reach out to you soon.",
            "Done",
            "No",
            false
        )
        glitterDialog.show(requireActivity().supportFragmentManager, "signature")
    }

    override fun resendOTP() {}

    override fun finishThisActivity() {
        requireActivity().finish()
    }

    fun validateEmailAddr(): Boolean {
        val email = binding.edtEmailId.text.toString().trim()
        if (!TextUtils.isEmpty(email)) {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmailId.error = null
                // binding.tvErrEmail.visibility = View.GONE
                return true
            } else {
                //  binding.edtEmailId.requestFocus()
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

}*/
