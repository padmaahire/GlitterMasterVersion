package com.goglitter.ui.DrawerMenu

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.goglitter.R
import com.goglitter.databinding.FragmentContactUsBinding
import com.goglitter.domain.request.ContactRequest
import com.goglitter.domain.response.JewelleryBanner
import com.goglitter.ui.Listener.GlitterDialogCallbackListener
import com.goglitter.ui.OTPActivity.DialogBaseFragment
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import com.goglitter.utils.TokenManager
import com.goglitter.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

/**
@author-Padma A
date-27/6/2023
 **/

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class ContactUsFragment : Fragment(), GlitterDialogCallbackListener {
    lateinit var binding: FragmentContactUsBinding
    private var param1: String? = null
    private var param2: String? = null
    var bannerList: ArrayList<JewelleryBanner> = arrayListOf()
    lateinit var sliderAdapter: BannerAdapter
    val viewModel: AuthViewModel by viewModels()
    private lateinit var glitterDialog: DialogBaseFragment

    @Inject
    lateinit var tokenManager: TokenManager

    var id:String = ""
    var mobile:String=""

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
        binding=FragmentContactUsBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        binding.edtName.addTextChangedListener(watcher)
        binding.edtLastName.addTextChangedListener(watcher)
        binding.edtPhoneNumber.addTextChangedListener(watcher)
        binding.edtEmailId.addTextChangedListener(watcher)
        binding.edtMessage.addTextChangedListener(watcher)
        getAllLoanBanners()
        sliderAdapter = BannerAdapter(bannerList)
        binding.slider.setSliderAdapter(sliderAdapter)
        binding.slider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.slider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.slider.setScrollTimeInSec(2); //set scroll delay in seconds :
        binding.slider.startAutoCycle();

        customTextView(binding.tvPrivacy)
        customTextView1(binding.tvContact)

        id= tokenManager.getSalesId().toString()
        getUser(id)

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

        binding.btnSubmit.setOnClickListener(){
            if(!validateName() or !validateLastName() or !validatePhoneNmber() or !validateEmailAddr() or !validateMessage()){
                return@setOnClickListener
            }else{
                hideKeyboard()
                contactDetails(binding.edtName.text.toString(),binding.edtLastName.text.toString(),binding.edtPhoneNumber.text.toString(),binding.edtEmailId.text.toString(),binding.edtMessage.text.toString())
            }
        }

        return view
    }

    private fun contactDetails(
        name: String,
        lname: String,
        phone: String,
        email: String,
        msg: String
    ) {
        val request = ContactRequest(name,lname,email,phone,msg)
        viewModel.glitterContact(request)
        viewModel.contactResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                    //   binding.progressCircular.isVisible = false
                }

                is NetworkResult.Failure -> {
                    //  Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    if(it.data.status.equals("success")){
                        binding.progressCircular.isVisible = false

                        Handler().postDelayed({
                            glitterDialog = DialogBaseFragment(this).newInstance(
                                R.drawable.ic_ok,
                                "Thank You",
                                "Thank you for submitting your details. \n" +
                                        " We will reach out to you soon",
                                "Done",
                                "No",
                                false
                            )
                            glitterDialog.show(requireActivity().supportFragmentManager, "signature")
                        }, 200)

                        // Log a custom event
                        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
                        val params = Bundle()
                        params.putString("contact_us", "goGlitter")
                        mFirebaseAnalytics.logEvent("contact_us_count", params)
                    }else{
                      //  Toast.makeText(requireActivity(),it.data.msg, Toast.LENGTH_SHORT).show()
                        showSnackBar(it.data.msg.toString())
                    }
                }
            }
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
    private fun getAllLoanBanners() {
        viewModel.getJewelleryBannerList()
        viewModel.jewellerBannerResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }
                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    bannerList = it.data.result as ArrayList<JewelleryBanner>
                    Log.d("lst", bannerList.toString())
                    sliderAdapter.updateBanner(bannerList)
                    binding.progressCircular.visibility = View.GONE
                }
                else -> {}
            }
        }

    }

    override fun DialogDone() {
        Navigation.findNavController(requireView()!!).navigate(R.id.action_contactUsFragment_to_nav_home)
    }
    override fun onCustomerDialogOk() {}
    override fun onCustomerDialogCancel() {}
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
    fun validateMessage(): Boolean {
        val mobile = binding.edtMessage.text.toString().trim()
        if (!TextUtils.isEmpty(mobile)) {
            binding.tvErrMessage.visibility=View.GONE
            return true
        } else {
            binding.tvErrMessage.visibility=View.VISIBLE
            binding.tvErrMessage.setText(R.string.error_msg)
            return false
        }
    }
    fun validateEmailAddr(): Boolean {
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

    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            if (binding.edtName.text.length!=0){
                binding.tvErrName.visibility=View.GONE
            }

            if (binding.edtLastName.text.length!=0){
                binding.tvErrLastName.visibility=View.GONE
            }
            if(binding.edtPhoneNumber.text.length!=0){
                binding.tvErrPhone.visibility=View.GONE
            }
            if(binding.edtEmailId.text.length!=0){
                binding.tvErrEmail.visibility=View.GONE
            }

            if(binding.edtMessage.text.length!=0){
                binding.tvErrMessage.visibility=View.GONE
            }

        }
        override fun afterTextChanged(s: Editable) {
        }

    }
    private fun customTextView(view: TextView) {
        val spanTxt = SpannableStringBuilder(
            "By Contacting Us, you agree to the "
        )
        spanTxt.append("Terms of Services")
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {

                lateinit var _dialog: TandCBottomSheetFragment
                _dialog= TandCBottomSheetFragment()
                _dialog.show(activity!!.supportFragmentManager, "BSDialogFragment")
            }
        }, spanTxt.length - "Terms of Services".length, spanTxt.length, 0)
        spanTxt.append(" and")
        // spanTxt.setSpan(ForegroundColorSpan(Color.BLACK), 32, spanTxt.length, 0)
        spanTxt.append(" Privacy Policy")
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                lateinit var _dialog: PrivacyPolicyBottomSheetFragment
                _dialog= PrivacyPolicyBottomSheetFragment()
                _dialog.show(activity!!.supportFragmentManager, "BSDialogFragment")
            }

        }, spanTxt.length - " Privacy Policy".length, spanTxt.length, 0)
        spanTxt.append(" of GoGlitter Avenues Pvt. Ltd.")
        view.movementMethod = LinkMovementMethod.getInstance()
        view.setText(spanTxt, TextView.BufferType.SPANNABLE)
    }
    private fun customTextView1(view: TextView) {
        val spanTxt = SpannableStringBuilder(
            "Reach us for any query/complaint or feedback at:  "
        )
        spanTxt.append("support@glitter-india.com")
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("support@glitter-india.com"))
                val pm = activity!!.packageManager
                val matches = pm.queryIntentActivities(intent, 0)
                var best: ResolveInfo? = null
                for (info in matches) if (info.activityInfo.packageName.endsWith(".gm") ||
                    info.activityInfo.name.lowercase(Locale.getDefault()).contains("gmail")
                ) best = info
                if (best != null) intent.setClassName(
                    best.activityInfo.packageName,
                    best.activityInfo.name
                )
                startActivity(intent)
            }
        }, spanTxt.length - "support@glitter-india.com".length, spanTxt.length, 0)

        view.movementMethod = LinkMovementMethod.getInstance()
        view.setText(spanTxt, TextView.BufferType.SPANNABLE)
    }
    private fun getUser(id:String) {
        viewModel.getUserResponse(id)
        viewModel.userProfileResponse.observe(requireActivity()) {
            when(it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }
                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }
                is  NetworkResult.Success -> {
                    if(it.data.result!=null){
                        binding.edtName.setText(it.data.result!!.regName)
                        binding.edtLastName.setText(it.data.result.regLname)
                        binding.edtEmailId.setText(it.data.result!!.regEmail)
                        mobile= it.data.result!!.regMobile.toString()
                        binding.edtPhoneNumber.setText(mobile)
                    }else{
                        Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                    }

                }
            }
        }

    }
}