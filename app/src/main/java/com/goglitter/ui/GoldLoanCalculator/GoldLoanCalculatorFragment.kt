package com.goglitter.ui.GoldLoanCalculator

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.goglitter.R
import com.goglitter.databinding.FragmentGoldLoanCalculatorBinding
import com.goglitter.domain.response.LoanBanner
import com.goglitter.ui.Loan.SliderAdapter
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import com.google.android.material.snackbar.Snackbar
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.pow
import kotlin.math.roundToInt

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class GoldLoanCalculatorFragment : Fragment() {
    private lateinit var binding: FragmentGoldLoanCalculatorBinding
    private var param1: String? = null
    private var param2: String? = null
    val viewModel: AuthViewModel by viewModels()
    var progressChangedValue: Int? = 0
    var principal:Double = 0.0
    var rate:Float?=0.0F
    var tenureMonths:Int? = 0
    lateinit var sliderAdapter: SliderAdapter
    var bannerList: ArrayList<LoanBanner> = arrayListOf()
    var list = ArrayList<String>()

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
        binding=FragmentGoldLoanCalculatorBinding.inflate(layoutInflater,container,false)
        val view=binding.root
        binding.tvAmountlabel.setText("The calculated EMI amount is indicative, and is for user’s reference only.")
        getAllLoanBanners()

        binding.tvLoanValue.filters = arrayOf(InputFilterMinMax(0, 5000000))
        binding.tvYear.filters = arrayOf(InputFilterMinMax(0, 120))

        binding.tvInterestRate.setRawInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)

        sliderAdapter = SliderAdapter(bannerList)
        binding.slider.setSliderAdapter(sliderAdapter)
        binding.slider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.slider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.slider.setScrollTimeInSec(2); //set scroll delay in seconds :
        binding.slider.startAutoCycle();
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLoanApply.setOnClickListener(){
            Navigation.findNavController(view)
                .navigate(R.id.action_goldLoanCalculatorFragment_to_nav_loanApplicationFragment)
        }

        binding.btnLoanApplybtn.setOnClickListener(){
            Navigation.findNavController(view)
                .navigate(R.id.action_goldLoanCalculatorFragment_to_nav_loanApplicationFragment)
        }

        binding.tvLoanValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence, after: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                binding.tvLoanError1.visibility=View.GONE
                binding.tvLoanValue.setSelection(binding.tvLoanValue.getText().length);

                try {
                    val loanAmt=binding.tvLoanValue.text!!.toString()

                    if(loanAmt.equals(0)){
                        binding.amountSeekBar.setProgress(0)
                    }else{
                        if(loanAmt.toInt() >= 50000 && loanAmt.toInt() <= 5000000){

                            binding.amountSeekBar.setProgress(loanAmt.toInt())

                            principal=loanAmt.toDouble()
                            if(rate!=0.0F && tenureMonths!=0){
                                val emi=calculateEMI(principal,rate!!,tenureMonths!!)
                               // val totalAmount=String.format("%.2f", emi)
                                val totalAmount=emi.roundToInt()
                                if(!totalAmount.equals("0.00")){
                                    binding.tvLoanAmount.text = getString(R.string.Rs) + totalAmount
                                    binding.tvLoanAmountstar.visibility=View.VISIBLE
                                }
                            }else{
                                binding.tvLoanAmount.setText("0.0")
                            }
                        }else{
                            binding.tvLoanAmount.setText("0.0")
                            binding.tvLoanError1.visibility=View.VISIBLE
                            binding.tvLoanAmountstar.visibility=View.GONE
                        }
                    }

                } catch (e: java.lang.Exception) {
                }
            }
        })
        binding.tvInterestRate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence, after: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                binding.tvInterestError.visibility=View.GONE
                binding.tvInterestRate.setSelection(binding.tvInterestRate.getText().length);

                try {
                   binding.tvInterestRate.setSelection(binding.tvInterestRate.getText().length)

                        try {
                            val progress=binding.tvInterestRate.text!!.toString()
                            Log.d(TAG,"progress =====> $progress")
                            val p = (progress.toInt()*10)
                            binding.interestSeekbar.setProgress(p)
                        }catch (e :Exception){
                            Log.d(TAG,e.message.toString())
                        }
                    val _rate=binding.tvInterestRate.text!!.toString()
                    Log.d(TAG,"Rate =====> $_rate")
                    if(_rate.toFloat()>=5.0 && _rate.toFloat()<=25.0){
                        rate=_rate.toFloat()
                        if(principal!=0.0 && tenureMonths!=0){
                            val emi=calculateEMI(principal, rate!!,tenureMonths!!)
                            val totalAmount=emi.roundToInt()
                          //  val totalAmount=String.format("%.2f", emi)
                            Log.d(TAG,"totalAmount =====> $totalAmount")
                            if(!totalAmount.equals("0.00")){
                                binding.tvLoanAmount.text = getString(R.string.Rs) + totalAmount
                                binding.tvLoanAmountstar.visibility=View.VISIBLE
                            }
                        }else{
                            binding.tvLoanAmount.setText("0.0")
                        }

                    }else {
                        binding.tvInterestError.visibility = View.VISIBLE
                        binding.tvLoanAmount.setText("0.0")
                        binding.tvLoanAmountstar.visibility=View.GONE
                    }

                } catch (e: java.lang.Exception) {
                    Log.d(TAG,e.message.toString())
                }

            }
        })
        binding.tvYear.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int,count: Int,after: Int) { }
            override fun onTextChanged(charSequence: CharSequence, after: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding.tvTenureError.visibility=View.GONE
                binding.tvYear.setSelection(binding.tvYear.getText().length);
                try {
                    val _tenure=binding.tvYear.text!!.toString()

                    if(_tenure.toDouble()>=12 && _tenure.toDouble()<=120){

                        val s=binding.tvYear.text!!.toString()

                        binding.tenureSeekbar.setProgress(_tenure.toInt())

                        tenureMonths=s.toInt()

                        if(principal!=0.0 && rate!=0.0F){
                            val emi=calculateEMI(principal,rate!!,tenureMonths!!)
                            val totalAmount=emi.roundToInt()
                            //val totalAmount=String.format("%.2f", emi)

                            if(!totalAmount.equals("0.00")){
                                binding.tvLoanAmount.text = getString(R.string.Rs) + totalAmount
                                binding.tvLoanAmountstar.visibility=View.VISIBLE
                            }
                        }else{
                            binding.tvLoanAmount.setText("0.0")
                        }
                    }else{
                        binding.tvTenureError.visibility=View.VISIBLE
                        binding.tvLoanAmountstar.visibility=View.GONE
                        binding.tvLoanAmount.setText("0.0")
                    }
                } catch (e: java.lang.Exception) {
                }
            }
        })
        binding.amountSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                binding.tvLoanValue.setText(progress.toString())
                progressChangedValue = progress

            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        binding.interestSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                val _p=progress/10.0F
                progressChangedValue=progress

                binding.tvInterestRate.setText(_p.toString())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
        binding.tenureSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvYear.setText(progress.toString())
                progressChangedValue = progress

            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {


            }

        })
    }

    private fun getAllLoanBanners() {
        viewModel.getBannerList()
        viewModel.bannerResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    // binding.progressCircular.isVisible = it.isLoading
                    binding.progressCircular.isVisible = false
                }

                is NetworkResult.Failure -> {
                    showSnackBar(it.errorMessage)
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
        sliderAdapter.updateBanner(bannerList)
    }
    fun calculateEMI(principal: Double, interestRate: Float, tenureMonths: Int): Double {
        val monthlyInterestRate = interestRate / 12 / 100
        Log.d("inr",monthlyInterestRate.toString())
        val numerator = principal * monthlyInterestRate * (1 + monthlyInterestRate).pow(tenureMonths)
        val denominator = (1 + monthlyInterestRate).pow(tenureMonths) - 1
        return numerator / denominator

        // P x R x (1+R)^N / [(1+R)^N-1]

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