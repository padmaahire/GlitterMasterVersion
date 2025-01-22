package com.goglitter.ui.Loan

import CRMEncryption
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.*
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.goglitter.R
import com.goglitter.databinding.FragmentLoanApplicationBinding
import com.goglitter.decryptionUtils.Utils
import com.goglitter.domain.SupportList
import com.goglitter.domain.request.*
import com.goglitter.domain.response.CRMDecryptResponse
import com.goglitter.domain.response.GlitterSponsors
import com.goglitter.domain.response.LoanBanner
import com.goglitter.ui.Listener.DialogCallbackListener
import com.goglitter.ui.Listener.GlitterDialogCallbackListener
import com.goglitter.ui.OTPActivity.DialogBaseFragment
import com.goglitter.ui.OTPActivity.OTPBaseDialogFragment
import com.goglitter.ui.OTPActivity.OTPFragment
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.tirgei.retrofitauthorization.data.ApiClient
import dagger.hilt.android.AndroidEntryPoint
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

/**
@author-Padma A
date-1/6/2023
Updated on-03/05/24
 **/
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class LoanApplicationFragment : Fragment(), View.OnClickListener, GlitterDialogCallbackListener,
    DialogCallbackListener, OTPDialogCallBackListener{
    lateinit var binding: FragmentLoanApplicationBinding
    private var param1: String? = null
    private var param2: String? = null
    lateinit var img_lst: ArrayList<SupportList>
    private lateinit var dialog: OTPFragment
    lateinit var sliderAdapter: SliderAdapter
    private lateinit var sessionManager: SessionManager
    val viewModel: AuthViewModel by viewModels()
    var sponosorsList: ArrayList<GlitterSponsors> = arrayListOf()
    var bannerList: ArrayList<LoanBanner> = arrayListOf()
    lateinit var crmDecryptData: CRMDecryptResponse
    private lateinit var apiClient: ApiClient
    private var regId: String? = null
    private var crm_encrypted_key:String?=null
    private var crm_encryted_data:String?=null

    @Inject
    lateinit var tokenManager: TokenManager
    var id: String = ""
    var mobile: String = ""
    var emailID: String = ""
    var otp_phone: String? = null
    var otp_email: String = ""
    var appID: String = ""
    private lateinit var glitterDialog: DialogBaseFragment
    private lateinit var otpDialog: OTPBaseDialogFragment

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
        binding = FragmentLoanApplicationBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        val textToUnderline = "Edit"

        var underlinedText = SpannableString(textToUnderline)
        underlinedText.setSpan(UnderlineSpan(), 0, textToUnderline.length, 0)

        binding.tvEdit.text = underlinedText
        binding.tvEditEmail.text = underlinedText
        id = tokenManager.getSalesId().toString()
        if (id != null) {
            getUser(id)
        }
        apiClient = ApiClient()
        sessionManager = SessionManager(requireActivity())

        binding.btnSubmit.setOnClickListener(this)
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
        binding.edtLastName.addTextChangedListener(watcher)
        binding.edtPinCode.addTextChangedListener(watcher)
        binding.edtEmailId.setOnClickListener(this)
        binding.edtPhoneNumber.setOnClickListener(this)
        binding.edtPinCode.setOnClickListener(this)
        binding.tvEdit.setOnClickListener(this)
        binding.tvEditEmail.setOnClickListener(this)
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
        //Firebase Analytics
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        val params = Bundle()
        params.putString("loan_apply", "goGlitter") // Using "loan_apply" as the parameter name
        mFirebaseAnalytics.logEvent("gold_loan_event", params) // Using "gold_loan_event" as the event name

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
            R.id.btnSubmit -> {
                if (!validateName() or !validateLastName() or !validatePhoneNmber() or !validateEmailAddr() or!validatePinCode()) {
                    return
                } else {
                    val code: String = binding.ccp.getSelectedCountryCodeWithPlus()
                    val number: String = binding.edtPhoneNumber.getText().toString().trim()
                    val phoneNumber = "$code$number"
                    val pincode = binding.edtPinCode.text.toString().trim()

                    LoanDetail(
                        binding.edtName.text.toString(),
                        id,
                        binding.edtLastName.text.toString(),
                        binding.edtEmailId.text.toString(),
                        number,
                        pincode
                    )
                }
            }
            R.id.edtEmailId -> {}
            R.id.edtPhoneNumber -> {}
            R.id.tvEdit -> {
                binding.edtPhoneNumber.setSelection(binding.edtPhoneNumber.text.length)
                binding.edtPhoneNumber.inputType = EditorInfo.TYPE_CLASS_PHONE
                binding.edtPhoneNumber.requestFocus()

                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(
                    binding.edtPhoneNumber,
                    InputMethodManager.SHOW_IMPLICIT
                )
            }
            R.id.tvEditEmail -> {
                binding.edtEmailId.setSelection(binding.edtEmailId.text.length)
                binding.edtEmailId.inputType = EditorInfo.TYPE_CLASS_TEXT
                binding.edtEmailId.requestFocus()

                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(
                    binding.edtEmailId,
                    InputMethodManager.SHOW_IMPLICIT
                )
            }
        }
    }
    private fun LoanDetail(
        name: String,
        id: String,
        lname: String,
        email: String,
        phone: String,
        pincode: String
    ) {
        val request = LoanRequest(name, id, lname, email, phone,pincode)
        viewModel.getLoanUserResponse(request)
        viewModel.loanResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }
                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    viewModel.clear()
                    if (it.data.status.equals("success")) {
                        try {
                            binding.progressCircular.isVisible = false
                            regId=it.data.result.appId

                            if (it.data.result.verfiyMob.equals("1") && it.data.result.verfiyEmail.equals("1")) {
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
                            } else if (it.data.result.verfiyMob.equals("0") && it.data.result.verfiyEmail.equals("1")) {
                                binding.progressCircular.isVisible = false

                                var _otp_phone = it.data.result.otp!!
                                var _appID = it.data.result.appId!!

                                otpDialog = OTPBaseDialogFragment(this).newInstance(
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

                            } else if (it.data.result.verfiyMob.equals("1") && it.data.result.verfiyEmail.equals("0")) {
                                binding.progressCircular.isVisible = false
                                otp_email = it.data.result.emailotp
                                appID = it.data.result.appId
                                otpDialog = OTPBaseDialogFragment(this).newInstance(
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

                            } else if (it.data.result.verfiyMob.equals("0") && it.data.result.verfiyEmail.equals("0")) {
                                binding.progressCircular.isVisible = false

                                otp_phone = it.data.result.otp
                                otp_email = it.data.result.emailotp
                                appID = it.data.result.appId
                                otpDialog = OTPBaseDialogFragment(this).newInstance(
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
                        showSnackBar(it.data.msg)
                    }

                }
                else -> {}
            }
        }
    }

    private fun PostToCRM(
        key: String,
        data: String
    ) {
         val request = CrmLoanRequest(key, data)
        Log.d("DDDD",request.toString())
        viewModel.crmLoanData(request)
        viewModel.crmLeadData.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }
                is NetworkResult.Failure -> {
                    showSnackBar(it.errorMessage)
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    val response = it.data
                    if(response.status.equals("success")){
                        binding.progressCircular.isVisible = false
                        showSnackBar(it.data.msg!!)
                        crm_encrypted_key=response.result!!.encryptedKey
                         crm_encryted_data=response.result!!.encryptedData

                         if(!crm_encryted_data.isNullOrEmpty() && !crm_encrypted_key.isNullOrEmpty()) {
                             decryption(crm_encrypted_key!!, crm_encryted_data!!)
                         }
                         glitterDialog.dismiss()
                         Handler().postDelayed({
                             Navigation.findNavController(requireView()!!).navigate(R.id.action_nav_loanApplicationFragment_to_nav_home)
                         }, 2000)

                    }else{
                        binding.progressCircular.isVisible = false
                        showSnackBar(it.data.msg.toString())
                    }
                }
                else -> {}
            }
        }
    }

    private fun decryption(crmEncryptedKey: String, crmEncrytedData: String) {

        //use our private key

      // var PRIVATE_KEY_STRING = "MIIEpQIBAAKCAQEAtMDCDQsi5SMz+UgUU7W7ndW4T0aJrM81siwcHAnBPyYF39R7gMzZ6RE8vlwnXkTNPyaEerRVq0l2MJoUVcic4xdXXKCEemXL7qAjmiqJiHRUVcYYTeZH/Z+02mpx/kCNmq2wXWCrNd8XofDrr4v4C9cDXy1L1mQUwdL98RmT1Qvf5oi/3cskYXzgmPO50GRBnqBnbi19LxdIdJhqPp4nDQcI+SyhSl4F5XlqG99ogv+AvBAdgas0CcVTRfjfgMKux12mlLPcDtbcIr1PpfxHx37AomqIY3+XFiqp9GzZ8oIM8ADX8I/4lyIiAkve+CGTnBC4dY/+FprzQf7l5Pd/gwIDAQABAoIBAQCfK9+2fMgN4CI0szSzQqYlJ79WkFdMxmwSqqVWS4DRizpBEWxwz/RVOci0GZLRgOT9Z33iUEvguqBJzGV75V3C3RshhnkjD+G2EW9Io4zZjRd3MSrV7qQWxD0O2g3oSBBq1ekpUadzmTHGedISlL+qLX3aopjWAuwEgzj8x/lW8dLYlG59ouR5a8d6JJQfXTa9wOysCHOgB7O55bFq73zPG/hLlTxgUA+ElNVkeP6jekTqsOCWlOPbK4JbWlzREONUvKmy0C37wTGqpVMibt7ed1FtnCjw2IbnxORZ5VR3e69CrsLoQCmH4zC42wQWk/gW7kJnYgRC+LutTnKRDOkBAoGBAOBOdQNJtuR5lGz0yuY66zs3Hq2Sx5OQCCjr+YoZd1x0zS6DPMh9f8umM4LBvwyieY9KXBphuGOFuzzNfG/+Yqc+/ox/1Z5zc5trhHgvO0mXBJQ+9QS2ZjxSMTs4X5AiK0oL6ACoGQ1EE39lJwzmVtj2uxTHnKVdhzglKsNUzGJZAoGBAM5K5aTLE75TSgknwtiI1Weo6PIUyuipOpzvobw63YNF27XOGLEsM2Vg0vQ6Vm01yxrMqj0l0KFRgmXuGmKu9Ncs0PMenlx/keTAxbut0fMhHp5ZdmhvjxCGvkIZDgFdSnP55QF7IC6kaCpC0GBK47ewBfdU7kF+HJvJY+1V5t07AoGAAmHS7E4EGzkPUnjnDrrhdKeJM96zL+X/sC+RXt4WXmz/INdT9e0fRlJliaX1GXVART1M7RrMkoIBEww7nhTdh1kr6uIQqrYfLC9gIMafopBwBAMHJx2oyEs3KqHWofk67HZj6F3gpjtq2ZeBaqmnknZMVlQoXbk4jmgWpCxqX0ECgYEApbi1UvVxHux6BXDTrm/+QBAJUrAxcUbBt2EqZLRFTzfHkP3ICEyh3UczMM/Mrl8Qkq7n88d1GqMIRipJOA2k01dFZImsXlJ9OW9DacnxZ4eruTnVTYCRRY15OlA2WBJBxXly5wWqjM22gHy8OR7wYJgfstmfrS1KfjwC8p6XoBECgYEA0nujwc9zu4z41ir+rmr0qt8t/RtsvacPY/kDO7e6m9w8lvaZN25Z4EyaUCiwgIlYzXEutztnnyI2B0xoycqin24npoINKd3P0DNL1USdt/Qt6EFyBmM7wyuYWqKhU21+nvMp8xekEe6mTflK5ZW+KcFpUNaJ8MNnIjK0/xI5Veg=";
       //var PRIVATE_KEY_STRING = "MIIEowIBAAKCAQEAw1ym4ce1vfgr5esgSH425Bprb9TnfE+LeqK5EM4O+KjHcXrQXEOOtmCzVqNT2gSYY6yNmtjjOu4tnPspA4sE34iHmE0qHC8HYVTQ4f9EdfyRGBY8Z7DTn0Eaio3EkSyJZIvs3jXND61m1HD/S1wGku8xLq8M3UthY8p/lCGPwVjbnSnACFDBUs2jvlUuY4yPPMHfMFjR/xy5elSSBV/bASJt+KMgxFMOlx0dqQsOKg81L9VhHC301DxSt9Os4id9DQlQqMp2L1V30qw0rTpOzEXsg9c7JDhi7BiOSclZmvDlduy2YDGNWx/8HZYup7rzisDtc+rXDwLF2l5CjX4M/wIDAQABAoIBACuzWhE/X0Qzc35m7j3JHfZAgq/tbCq+kHDKXaOItIijGJol4t4ArrfVQcirmGI4jO4e9z4gLhRMmx92f2IvgtON8ub6u+S6EGiRVHh5ZZMPAFXnA3Qedfg10/4SYFopIELBmibK2igbndj/vdvZpg7QOqw6tCH8brin34n/R+zz02Onrr0b3ADSdZs6vM8i+Xvp1+CnIr0TgSQJRPKLbCcvAIWIgF5JSWsO+fl3ll2r7z6Q6PlFBXRmZuaVDO9/yxcDVKmkRHnhmvXFdYUO4ISfBw0NTtLd4vQhyH+75gXTf/WLXG9++ec1Eoxk1i0jDhXh1cVBMqDIqUA1DlWFa4ECgYEA6kTaqqaCZ7WzwSjAve/GxtCGknlLNo+syV0k6SX1l4suy/bxMxKKMZ9QYAHlUFZyhKCVhxUxeNWW4pvYrUm8zlEO9xzzOvvsVw55fYPc9vpuUn47dWemOSHyloRoCQmQmAFi9XEqQWyrXztGB3jEEVwZL/gj51tSYQ18pL7yVhECgYEA1Xvg9U6fLpL/7akzoGgKpON6KxLaavSr+F1ELakinsdpVcp+56OBnxMsjpD6TDqH28DFNAaaKG3rcJTAePwmUvjqJngOldQZWDz2GR343w7UQSvhRneL/BKR3/7cFIli/twW9KSCRyS/LpC9Nrm8FV2qZolGsaP8NxpxvghQ4g8CgYAvlOKQ7Jbp95bV8bAMI+pMsUfT8jzbjRz8HSYFEMesCwHrPXHJ/McCwyfxCBGKWrSRyYf1UtGkiEVIK3073REzGnWerUudvFNJb1AvtRupMj48FRWAiBfT3NhHyAbVZxLuPMHbtitt9GNuSTy1ActY90rKWmz4bazyt2mMkJfp8QKBgQCT0fIjiWwZcLn7kzRnNoNgRw65spVAh+hn0hS6VSnerY9XUx5qQEIpprosgWB+1exqkmE6M/QcWHg0/RsE6vuSaj6fonPe9P0Tpyp0m9dOhFMeSUb0ZySrxyPw6OT052h/tl/CyAJZRq2Gjbwmn5cHaFLsKtOjfzvN3+awQcvXqwKBgAewDoXe1LMoZdxZMjsST0GZAOTToG0NFX5pNMTjgvqm1Tr3/a9N8iQCMQo4I4JYxjJPl9p/4NRfc9sXk83xHYT5lMVxX908YQZ/JS1eZqlsoohHKhMNJzuiEKn16RQaeucY8QxVr2D3mJ0+xv8oeQZTtkN9ixNw2pyhWHIjvSsn";
        var PRIVATE_KEY_STRING = "MIIEowIBAAKCAQEAw1ym4ce1vfgr5esgSH425Bprb9TnfE+LeqK5EM4O+KjHcXrQXEOOtmCzVqNT2gSYY6yNmtjjOu4tnPspA4sE34iHmE0qHC8HYVTQ4f9EdfyRGBY8Z7DTn0Eaio3EkSyJZIvs3jXND61m1HD/S1wGku8xLq8M3UthY8p/lCGPwVjbnSnACFDBUs2jvlUuY4yPPMHfMFjR/xy5elSSBV/bASJt+KMgxFMOlx0dqQsOKg81L9VhHC301DxSt9Os4id9DQlQqMp2L1V30qw0rTpOzEXsg9c7JDhi7BiOSclZmvDlduy2YDGNWx/8HZYup7rzisDtc+rXDwLF2l5CjX4M/wIDAQABAoIBACuzWhE/X0Qzc35m7j3JHfZAgq/tbCq+kHDKXaOItIijGJol4t4ArrfVQcirmGI4jO4e9z4gLhRMmx92f2IvgtON8ub6u+S6EGiRVHh5ZZMPAFXnA3Qedfg10/4SYFopIELBmibK2igbndj/vdvZpg7QOqw6tCH8brin34n/R+zz02Onrr0b3ADSdZs6vM8i+Xvp1+CnIr0TgSQJRPKLbCcvAIWIgF5JSWsO+fl3ll2r7z6Q6PlFBXRmZuaVDO9/yxcDVKmkRHnhmvXFdYUO4ISfBw0NTtLd4vQhyH+75gXTf/WLXG9++ec1Eoxk1i0jDhXh1cVBMqDIqUA1DlWFa4ECgYEA6kTaqqaCZ7WzwSjAve/GxtCGknlLNo+syV0k6SX1l4suy/bxMxKKMZ9QYAHlUFZyhKCVhxUxeNWW4pvYrUm8zlEO9xzzOvvsVw55fYPc9vpuUn47dWemOSHyloRoCQmQmAFi9XEqQWyrXztGB3jEEVwZL/gj51tSYQ18pL7yVhECgYEA1Xvg9U6fLpL/7akzoGgKpON6KxLaavSr+F1ELakinsdpVcp+56OBnxMsjpD6TDqH28DFNAaaKG3rcJTAePwmUvjqJngOldQZWDz2GR343w7UQSvhRneL/BKR3/7cFIli/twW9KSCRyS/LpC9Nrm8FV2qZolGsaP8NxpxvghQ4g8CgYAvlOKQ7Jbp95bV8bAMI+pMsUfT8jzbjRz8HSYFEMesCwHrPXHJ/McCwyfxCBGKWrSRyYf1UtGkiEVIK3073REzGnWerUudvFNJb1AvtRupMj48FRWAiBfT3NhHyAbVZxLuPMHbtitt9GNuSTy1ActY90rKWmz4bazyt2mMkJfp8QKBgQCT0fIjiWwZcLn7kzRnNoNgRw65spVAh+hn0hS6VSnerY9XUx5qQEIpprosgWB+1exqkmE6M/QcWHg0/RsE6vuSaj6fonPe9P0Tpyp0m9dOhFMeSUb0ZySrxyPw6OT052h/tl/CyAJZRq2Gjbwmn5cHaFLsKtOjfzvN3+awQcvXqwKBgAewDoXe1LMoZdxZMjsST0GZAOTToG0NFX5pNMTjgvqm1Tr3/a9N8iQCMQo4I4JYxjJPl9p/4NRfc9sXk83xHYT5lMVxX908YQZ/JS1eZqlsoohHKhMNJzuiEKn16RQaeucY8QxVr2D3mJ0+xv8oeQZTtkN9ixNw2pyhWHIjvSsn"

        val utils = com.goglitter.decryptionUtils.Utils()
        val symmetric = Symmetric()
        val asymmetric = Asymmetric()
        val helper = DecryptionHelper()

       /*val decodedKey: ByteArray = utils.decodeFromBase64(crmEncryptedKey.getBytes())
        val decodedContent: ByteArray = utils.decodeFromBase64(crmEncrytedData.getBytes())*/

        val decodedKey: ByteArray = utils.decodeFromBase64(crmEncryptedKey.toByteArray(Charsets.UTF_8))
        val decodedContent: ByteArray = utils.decodeFromBase64(crmEncrytedData.toByteArray(Charsets.UTF_8))

        val extractedIv: ByteArray = helper.extractBytes(decodedContent, 0, 16)
        val extractedEncryptedContent: ByteArray =
            helper.extractBytes(decodedContent, 16, decodedContent.size)
      /*  val decryptedKey: ByteArray = asymmetric.decrypt(
            decodedKey,
            PRIVATE_KEY_STRING.getBytes(),
            "RSA",
            "RSA/ECB/PKCS1Padding"
        )*/

        val decryptedKey: ByteArray = asymmetric.decrypt(
            decodedKey,
            PRIVATE_KEY_STRING.toByteArray(Charsets.UTF_8),
            "RSA",
            "RSA/ECB/PKCS1Padding"
        )
        val decryptedContent: ByteArray = symmetric.decrypt(
            extractedEncryptedContent,
            extractedIv,
            decryptedKey,
            "AES",
            "AES/CBC/PKCS5Padding"
        )
        Log.d("DECRYPT RESPONSE", String(decryptedContent))

        val decryptedString = String(decryptedContent)
        val gson = Gson()
        crmDecryptData = gson.fromJson(decryptedString, CRMDecryptResponse::class.java)

        if(crmDecryptData.IsSuccess.equals("true")){
            getUpdatedEnquiryLoanList()
        }

        val strFromBytesUTF8 = String(decryptedContent, StandardCharsets.UTF_8)
        println("String from bytes (UTF-8): $strFromBytesUTF8")
    }

    private fun getUpdatedEnquiryLoanList() {
        var request=crmEnquiryUpdateRequest(regId)
        viewModel.updateLoanEnquiry(request)
        viewModel.glitterLoanUpdateResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {}
                is NetworkResult.Failure -> {}
                is NetworkResult.Success -> {
                    if (it.data.status.equals("200")) {
                        viewModel.clear()
                        showSnackBar(it.data.msg.toString())
                    } else {
                        showSnackBar(it.data.msg.toString())
                    }
                }
                else -> {}
            }
        }
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
                    if (it.data.result != null && !it.data.result.equals("")) {
                        emailID = it.data.result!!.regEmail.toString()
                        val name=it.data.result!!.regName.toString()
                        val last_name=it.data.result!!.regLname.toString()
                        if(emailID!=null){
                            binding.edtEmailId.setText(emailID)
                        }
                        if(name!=null){
                            binding.edtName.setText(name)
                        }
                        if(last_name!=null){
                            binding.edtLastName.setText(last_name)
                        }

                        /*binding.edtName.setText(it.data.result!!.regName)
                        binding.edtLastName.setText(it.data.result.regLname)*/

                        mobile = it.data.result!!.regMobile!!
                        if(mobile!=null){
                            binding.edtPhoneNumber.setText(mobile)
                        }
                    } else {
                        showSnackBar(it.data.msg.toString())
                    }

                }
            }
        }
    }
    private fun getSponsored() {
        viewModel.getSponsoredResponse()
        viewModel.sponsorResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }
                is NetworkResult.Failure -> {
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
                }
                is NetworkResult.Failure -> {
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
    fun validateName(): Boolean {
        val mobile = binding.edtName.text.toString().trim()
        if (!TextUtils.isEmpty(mobile)) {
            // binding.edtName.error = null
            binding.tvErrName.visibility = View.GONE
            return true
        } else {
            //  binding.edtName.error = getString(R.string.error_name)
            binding.tvErrName.visibility = View.VISIBLE
            binding.tvErrName.setText(R.string.error_name)
            return false
        }
    }
    @SuppressLint("SuspiciousIndentation")
    fun validatePinCode(): Boolean {
        val _mobile = binding.edtPinCode.text.toString().trim()
        val MobilePattern = Regex("^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}\$")

        if (!TextUtils.isEmpty(_mobile)) {
            if (_mobile.matches(MobilePattern)) {
                binding.tvErrpicode.visibility=View.GONE
                return true
            } else {
                binding.tvErrpicode.visibility = View.VISIBLE
                binding.tvErrpicode.setText("Please enter valid pin code number")
                return false
            }

        } else {
            binding.tvErrpicode.visibility = View.VISIBLE
            binding.tvErrpicode.setText(R.string.error_pincode)
            return false
        }
    }
    fun validateLastName(): Boolean {
        val mobile = binding.edtLastName.text.toString().trim()
        if (!TextUtils.isEmpty(mobile)) {
            // binding.edtName.error = null
            binding.tvErrLastName.visibility = View.GONE
            return true
        } else {
            //  binding.edtName.error = getString(R.string.error_name)
            binding.tvErrLastName.visibility = View.VISIBLE
            binding.tvErrLastName.setText(R.string.error_last_name)
            return false
        }
    }
    private fun showMobileOtp() {
        viewModel.clear()
        dialog = OTPFragment(this).newInstance(
            "OTP",
            "1234 is your Glitter account verification OTP Code.",
            "ok"
        )
        dialog.show(requireActivity()!!.supportFragmentManager, "signature")
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

    //Push data to CRM on Dialog
    override fun DialogDone() {
        hybrid_Encryption()
    }
    //Encryption
    private fun hybrid_Encryption() {
        val number: String = binding.edtPhoneNumber.getText().toString().trim()
        val emailID=binding.edtEmailId.text.toString().trim()
        val fname=binding.edtName.text.toString().trim()
        val lname=binding.edtLastName.text.toString().trim()
        val pincode=binding.edtPinCode.text.toString().trim()

        val crmDataJson = """
 
            {
                "IsAsync": false,
                "CallBackUrl": "",
                "LeadType": "Individual",
                "LeadDetails": {
                    "LeadNumber": "",
                    "CountryCode": "+91",
                    "MobileNumber": "${number}",
                    "Product": "CRM728",
                    "ProductSubType": "",
                    "AlternateContactNumber": "",
                    "LeadStatus": "LS0122",
                    "LeadType": "Ind",
                    "AssignedToSelf": "True",
                    "AssignmentBasedOn": "",
                    "BranchSolId": "",
                    "CustomerType": "New",
                    "Salutation": "MISS",
                    "FirstName": "${fname}",
                    "MiddleName": "K",
                    "LastName": "${lname}",
                    "LeadSource": "LSM0069",
                     "PartnerId":"", 
                    "CampaignName": "",
                    "AccountNumber": "",
                    "AccountType": "",
                    "Remarks": "",
                    "LeadChannel": "Digital",
                    "DateOfBirth": "",
                    "Nationality": "",
                    "PANNumber": "",
                    "Gender": "",
                    "ServiceFlag": "",
                    "EmailAddress": "${emailID}",
                    "ResidencePhone": "",
                    "OfficePhone": "",
                    "ResidencyStatus": "",
                  "PreferredCallTime": "",
                    "PreferredCallStartTime": "",
                    "PreferredCallEndTime": "",
                    "ModeOfCommunication": "",
                    "timezone": "",
                    "overseascountry": "",
                    "CustomerSegment": "",
                    "AssignmentType": "",
                    "AssignmentId": "",
                    "ReferralType": "",
                    "ReferredByOtherName": "",
                    "ReferredByOtherEmail": "",
                    "ReferredByOtherPhone": "",
                    "ReferrerEmployeeId": "",
                    "ReferredByLeadId":"",
                     "ReferredByChannelPartnerId":"",
                    "CustomerId": "",
                    "UCIC": "",
                    "ReferrerCustomerId": "",
                    "ReferrerUCIC": "",
                    "ReferrerPanNumber": "",
                    "ReferrerUCC": "",
                    "ReferrerAccountNumber": "",
                    "ReferrerMobileNumber": "",
                    "CVCESegment": "",
                    "AffluentCustomer":true,
                    "UTMCampaign": "",
                    "UTMFEDID": "",
                    "UTMGAID": "",
                    "UTMGCIID": "",
                    "UTMITM": "",
                    "UTMLeadPriority": "",
                    "UTMLeadPropensity": "",
                    "UTMLeadScore": "",
                    "UTMNTBID": "",
                    "AggregatorLeadSource": "",
                    "SMSShortCode": "",
                    "PincodeLead": "",
                    "DropOffPageName": "",
                    "DropoffPageNumber": "",
                    "TimeSpentonPage": "",
                    "BREResponse": "",
                    "FirstTimePAOfferFlag": "",
                    "PAOffer": "",
                    "TimeOfLeadDrop": "",
                    "UTMLms": "",
                    "Medium": "",
                    "OnlineCoversionSR": "",
                    "UotmCode": "",
                    "UTMInfo": "",
                    "Priority": "",
                    "IndividualOrganizationName": "",
                    "ReferrerOrganizationName": "",
                    "LeadGenerator": "",
                      "PPACode": "",
                      "DestCode": "",
                     "FedIDoftheCustomer": "",
                    "EmployeeIDandSOLIDwhoassistedthecustomer": "",
                    "Successorfailurestatusonfinalpageofjourney": "",
                    "ParentLeadNumber":""
                },
                "AddressDetails": [
                    {
                        "AddressType": "",
                        "AddressLine1": "",
                        "AddressLine2": "",
                        "AddressLine3": "",
                        "AddressLine4": "",
                        "Landmark": "",
                        "Locality": "",
                        "Village": "",
                        "City": "",
                        "District": "",
                        "State": "",
                        "Country": "",
                        "Pincode": "",
                        "Latitude": "",
                        "Longitude": ""
                    },
                    {
                        "AddressType": "",
                        "AddressLine1": "",
                        "AddressLine2": "",
                        "AddressLine3": "",
                        "AddressLine4": "",
                        "Landmark": "",
                        "Locality": "",
                        "Village": "",
                        "City": "",
                        "District": "",
                        "State": "",
                        "Country": "",
                        "Pincode": "",
                        "Latitude": "",
                        "Longitude": ""
                    }
                ],
                "OrganisationDetails": {
                    "CompanyName": "",
                    "AccountNumber": "",
                    "UCC": "",
                    "PpaCode": "",
                    "MobileNo": "",
                    "EmailAddress": "",
                    "PanNumber": "",
                    "DateOfIncorporation": "",
                    "ContactPersonFirstName": "",
                    "ContactPersonMiddleName": "",
                    "ContactPersonLastName": "",
                    "ContactPersonMobileNumber": "",
                    "ContactPersonPanNumber": "",
                    "ContactPersonUCIC": ""
                },
                "AppointmentDetails": {
                    "EngagementType": "",
                    "IsJointActivity": "",
                    "InitiatedBy": "",
                    "PurposeOfMeeting": "",
                    "PlaceOfMeeting": "",
                    "StartDateTime": "",
                    "AppointmentStatus": ""
                },
                "ApplicationDetails": {
                    "GoldLoanRequest": {
                        "TransactionId": "",
                        "LoanAmount": "",
                        "LoanTenure": "",
                        "LoanAccountNumber": "",
                        "LoanAmountDisbursed": "",
                        "DisbursalDate": "",
                        "InstanceId": "",
                        "ROI": "",
                        "ApplicantId_CustId": "",
                        "AssessmentId": "",
                        "VariantFacilityType": "",
                        "Gender": "",
                        "MaritalStatus": "",
                        "Religion": "",
                        "Education": "",
                        "SourceOfFunds": "",
                        "GrossAnnualIncome": "",
                        "PersonWithDisability": "",
                        "VernacularDeclaration": "",
                        "FatherName": "",
                        "MotherMaidenName": "",
                        "SubAgentCode": ""
                    }
                }
            }
""".trimIndent()
        Log.d("JSON_DATA",crmDataJson.toString())

        var crmEncryption=CRMEncryption(crmDataJson)

        // Get the encrypted values
        val encryptedData = crmEncryption.getEncryptedValues()

        // Access encryptedKey and encryptedData
        val encodedEncryptedKey = encryptedData.encryptedKey
        val encodedIvAndEncryptedContent = encryptedData.encryptedData

        Log.d("ENCRYPTED_KEY", encodedEncryptedKey)
        Log.d("ENCRYPTED_CONTENT", encodedIvAndEncryptedContent)

        PostToCRM(encodedEncryptedKey, encodedIvAndEncryptedContent)

    }
    override fun onCustomerDialogOk() {}
    override fun onCustomerDialogCancel() {}
    private fun showOTP(_otp: String?) {
        viewModel.clear()
        dialog = OTPFragment(this).newInstance(
            "OTP",
            "$_otp is your Glitter account verification OTP Code.",
            "ok"
        )
        dialog.show(requireActivity()!!.supportFragmentManager, "signature")
    }
    private val watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            if (binding.edtName.text.length != 0) {
                binding.tvErrName.visibility = View.GONE
            }

            if (binding.edtLastName.text.length != 0) {
                binding.tvErrLastName.visibility = View.GONE
            }
            if (binding.edtPhoneNumber.text.length != 0) {
                binding.tvErrPhone.visibility = View.GONE
            }
            if (binding.edtEmailId.text.length != 0) {
                binding.tvErrEmail.visibility = View.GONE
            }
            if(binding.edtPinCode.text.length!=0){
                binding.tvErrpicode.visibility=View.GONE
            }
        }

        override fun afterTextChanged(s: Editable) {
            if (binding.edtName.text.length == 0 && binding.edtLastName.text.length == 0 && binding.edtPhoneNumber.text.length == 0 && binding.edtEmailId.text.length == 0 && binding.checkbox.isChecked == false && binding.edtPinCode.text.length==0) {
                binding.btnSubmit.isEnabled = false

            } else {
                binding.btnSubmit.isEnabled = true
                binding.btnSubmit.setBackgroundResource(R.drawable.button)

            }
        }
    }
    override fun onDialogOk() {}
    override fun onDialogCancel() {}
    override fun verifyOTP() {
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

        // Log a custom event
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        val params = Bundle()
        params.putString("loan_application", "goGlitter") // Using "loan_application" as the parameter name
        mFirebaseAnalytics.logEvent("loan_application_count", params) // Using "loan_application_success" as the event name
    }
    override fun resendOTP() {}
    override fun finishThisActivity() {
        requireActivity().finish()
    }
    open fun showSnackBar(message: String) {
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
//https://glitter-india.com/api.glitter-india.com/web/version_1/api/applicationDetails