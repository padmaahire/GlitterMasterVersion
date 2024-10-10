package com.goglitter.ui.Loan

import android.icu.text.NumberFormat
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.net.ParseException
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.goglitter.R
import com.goglitter.databinding.FragmentLoanBinding
import com.goglitter.domain.CustomerView
import com.goglitter.domain.SupportList
import com.goglitter.domain.response.GlitterSponsors
import com.goglitter.domain.response.GlitterTestimonial
import com.goglitter.domain.response.LoanBanner
import com.goglitter.ui.Invest.TestimonialDialogFragment
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.io.InputStream
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
@author-Padma A
date-1/6/2023
update-24/04/24
 **/

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class LoanFragment : Fragment(), View.OnClickListener,CustomerViewAdapter.onClickListener {
    lateinit var binding: FragmentLoanBinding
    private var param1: String? = null
    private var param2: String? = null
    var progressChangedValue: Int? = 0
    var currentRate: Float = 0F
    var gold_wt: Float = 0F
    lateinit var imageUrl: ArrayList<String>
    lateinit var customer_lst: ArrayList<CustomerView>
    lateinit var img_lst: ArrayList<SupportList>
    lateinit var sliderAdapter: SliderAdapter
    lateinit var cutomerAdapter: CustomerViewAdapter
    lateinit var supportAdapter: SupportAdapter
    lateinit var adapter: ArrayAdapter<*>
    var JwellerList = ArrayList<SupportList>()
    var list = ArrayList<String>()
    val viewModel: AuthViewModel by viewModels()
    var locales: Array<String> = Locale.getISOCountries()

    var bannerList: ArrayList<LoanBanner> = arrayListOf()
    var sponosorsList: ArrayList<GlitterSponsors> = arrayListOf()
    var testimonialList: ArrayList<GlitterTestimonial> = arrayListOf()

    var goldCurrentRate24: String?=null
    var goldCurrentRate22: String?=null
    var goldCurrentRate21: String?=null
    var goldCurrentRate18: String?=null
    var goldCurrentRate16: String?=null
    var goldCurrentRate23: String?=null

    //Eligibility loan calculation according to 80% (0.8)
    //Total amount is in per/gram hence divide by 10

    lateinit var dialog:TestimonialDialogFragment

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

        binding = FragmentLoanBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        getAllLoanBanners()
        getTestimonials()
        getSponsored()
        // getGoldRateList()
         //getRapidGoldRateList()
        getGoldRate()

        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
        binding.tvAmountlabel.setText("The calculated loan amount is indicative, it is based on the past market operating rate and is for user’s reference only.")
        binding.btnLoanApply.setOnClickListener(this)
        binding.btnLoanApplybtn.setOnClickListener(this)
        imageUrl = ArrayList()
        customer_lst = ArrayList()
        img_lst = ArrayList()
        JwellerList = ArrayList()
        JwellerList.add(SupportList("", R.drawable.banner_jwellery))
        for (i in 0..10) {
            customer_lst.add(
                CustomerView(
                    "Good experience overall. \n" +
                            "Proper explanation while \n" +
                            "getting loan, professional \n" +
                            "executives. ",
                    "Madhab Sahoo",
                    "Kirana Store Owner",
                    R.drawable.person_black_24dp.toString()
                )
            )
            img_lst.add(SupportList("",R.drawable.img_support))
        }

        val data: String? = loadJSONFromAsset()
        try {
            val gson = Gson()
            val listType = object : TypeToken<List<String?>?>() {}.type
            list = gson.fromJson<List<String>>(data, listType) as ArrayList<String>
            Log.d("******NAME", list.toString())
        } catch (e: Exception) {
            Log.d("******NAME", e.message!!)
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_layout, list)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        binding.tvCity.adapter = adapter

        binding.tvCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPeopleView.setLayoutManager(layoutManager)
        cutomerAdapter = CustomerViewAdapter(testimonialList,this)
        binding.rvPeopleView.setAdapter(cutomerAdapter)

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
        binding.wtSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progressChangedValue = progress
                if(progressChangedValue!=null) {
                    binding.tvGoldWt.setText(progressChangedValue.toString()+ " " + "gms")
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                binding.tvGoldWt.setText(progressChangedValue.toString() + " " + "gms")
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                binding.tvGoldWt.setText(progressChangedValue.toString() + " " + "gms")

                gold_wt = progressChangedValue!!.toFloat()
                var total = ((currentRate * gold_wt * 0.8)).roundToInt()
                val totalAmount=total.toString()
                //val totalAmount=String.format("%.2f", total)

                if(!totalAmount.equals("0")){
                    binding.tvLoanAmount.text = getString(R.string.Rs) + totalAmount
                    binding.tvLoanAmountstar.visibility=View.VISIBLE
                }
            }
        })
        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                progressChangedValue = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {

                if (progressChangedValue == 0) {
                    binding.tvCaratValue.setText("16 Carat")
                  //  val _currentRate = goldCurrentRate16!!.toInt()*5
                    val _currentRate = goldCurrentRate16!!.toInt()
                    currentRate= _currentRate.toFloat()
                } else if (progressChangedValue == 1) {
                    binding.tvCaratValue.setText("18 Carat")
                    //val _currentRate1=goldCurrentRate18!!.toInt()*5
                    val _currentRate1=goldCurrentRate18!!.toInt()
                    currentRate = _currentRate1.toFloat()
                } else if (progressChangedValue == 2) {
                    binding.tvCaratValue.setText("21 Carat")
                   // val _currentRate2=goldCurrentRate21!!.toInt()*5
                    val _currentRate2=goldCurrentRate21!!.toInt()
                    currentRate = _currentRate2.toFloat()
                } else if (progressChangedValue == 3) {
                    binding.tvCaratValue.setText("22 Carat")
                   // val _currentRate3=goldCurrentRate22!!.toInt()*5
                    val _currentRate3=goldCurrentRate22!!.toInt()
                    currentRate = _currentRate3.toFloat()
                } else if (progressChangedValue == 4) {
                    binding.tvCaratValue.setText("23 Carat")
                   // val _currentRate4=goldCurrentRate23!!.toInt()*5
                    val _currentRate4=goldCurrentRate23!!.toInt()
                    currentRate = _currentRate4.toFloat()
                } else if (progressChangedValue == 5) {
                    binding.tvCaratValue.setText("24 Carat")
                    //val _currentRate5=goldCurrentRate24!!.toInt()*5
                    val _currentRate5=goldCurrentRate24!!.toInt()
                    currentRate = _currentRate5.toFloat()

                }
                var total = ((currentRate * gold_wt * 0.8)/10).roundToInt()
                val totalAmount=total.toString()

                if(!totalAmount.equals("0")){
                    binding.tvLoanAmount.text = getString(R.string.Rs) + totalAmount
                    binding.tvLoanAmountstar.visibility=View.VISIBLE
                }
            }

        })
    }

    private fun getSponsored() {
        viewModel.getSponsoredResponse()
        viewModel.sponsorResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Failure -> {
                    showSnackBar(it.errorMessage)
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    sponosorsList = it.data.result as ArrayList<GlitterSponsors>
                    //Log.d("lst", sponosorsList.toString())
                    //val img=sponosorsList.get(0).logoImage
                    //Glide.with(requireView()).load(img).into(binding.imgSponsored)
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }
    }
    private fun getTestimonials() {
        viewModel.getTestimonialResponse()
        viewModel.testimonialResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    testimonialList = it.data.result as ArrayList<GlitterTestimonial>
                    Log.d("lst", testimonialList.toString())
                    cutomerAdapter.updateTestimonial(testimonialList)
                    cutomerAdapter.notifyDataSetChanged()
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }

    }
/*    private fun getGoldRateList() {
        viewModel.getGoldResponseList()
        viewModel.goldRateResponse.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    goldCurrentRate12 = getDoubleFromString(it.data.data!!.gold12k!!).roundToInt().toString()
                    goldCurrentRate10 = getDoubleFromString(it.data.data!!.gold10k!!).roundToInt().toString()
                    goldCurrentRate14 = getDoubleFromString(it.data.data!!.gold14k!!).roundToInt().toString()
                    goldCurrentRate16 = getDoubleFromString(it.data.data!!.gold16k!!).roundToInt().toString()
                    goldCurrentRate18 = getDoubleFromString(it.data.data!!.gold18k!!).roundToInt().toString()
                    goldCurrentRate22 = getDoubleFromString(it.data.data!!.gold22k!!).roundToInt().toString()
                    goldCurrentRate24 = getDoubleFromString(it.data.data!!.gold24k!!).roundToInt().toString()
                    goldCurrentRate23 = getDoubleFromString(it.data.data!!.gold23k!!).roundToInt().toString()
                    goldCurrentRate21 = getDoubleFromString(it.data.data!!.gold21k!!).roundToInt().toString()
                    goldCurrentRate6 = getDoubleFromString(it.data.data!!.gold6k!!).roundToInt().toString()
                    goldCurrentRate8 = getDoubleFromString(it.data.data!!.gold8k!!).roundToInt().toString()
                    goldCurrentRate9 = getDoubleFromString(it.data.data!!.gold9k!!).roundToInt().toString()


                }
            }
        }
    }*/
fun getRapidGoldRateList() {
    viewModel.getRapidGoldResponseList()
    viewModel.rapidgoldRateResponse.observe(requireActivity()) {
        when (it) {
            is NetworkResult.Loading -> {
                binding.progressCircular.isVisible = true
            }
            is NetworkResult.Failure -> {
                binding.progressCircular.isVisible = false
            }
            is NetworkResult.Success -> {
                val goldRateData = it.data.GoldRate
                if (goldRateData != null) {
                    goldCurrentRate24 = goldRateData.find { it.state == "Maharashtra" }?.TenGram24K ?: ""
                    goldCurrentRate22 = goldRateData.find { it.state == "Maharashtra" }?.TenGram22K ?: ""
                    goldCurrentRate23= calGoldRate(goldCurrentRate24!!.toBigDecimal(),"23".toBigDecimal())
                    goldCurrentRate21=calGoldRate(goldCurrentRate24!!.toBigDecimal(),"21".toBigDecimal())
                    goldCurrentRate18=calGoldRate(goldCurrentRate24!!.toBigDecimal(),"18".toBigDecimal())
                    goldCurrentRate16=calGoldRate(goldCurrentRate24!!.toBigDecimal(),"16".toBigDecimal())

                } else {

                }

                binding.progressCircular.isVisible = false
            }

        }
    }
}
    fun getGoldRate(){
        viewModel.getGoldResponseSystemList()
        viewModel.goldRateResponseSystem.observe(requireActivity()) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    if(it.data.status.equals("success")){
                        val goldRateData = it.data.result
                        if (goldRateData != null) {
                            for(i in 0 until goldRateData.size){
                                when(goldRateData.get(i).goldCarat){
                                    "16K"->{
                                        goldCurrentRate16=goldRateData.get(i).goldRate
                                    }
                                    "18K"->{
                                        goldCurrentRate18= goldRateData.get(i).goldRate
                                    }
                                    "21K"->{
                                        goldCurrentRate21=goldRateData.get(i).goldRate
                                    }
                                    "22K"->{
                                        goldCurrentRate22=goldRateData.get(i).goldRate
                                    }
                                    "23K"->{
                                        goldCurrentRate23=goldRateData.get(i).goldRate
                                    }
                                    "24K"->{
                                        goldCurrentRate24=goldRateData.get(i).goldRate
                                    }
                                }

                            }
                        }

                        binding.progressCircular.isVisible = false
                    }else{

                    }

                }

            }
        }
    }
    private fun getAllLoanBanners() {

        viewModel.getBannerList()
        viewModel.bannerResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
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
        cutomerAdapter.updateTestimonial(testimonialList)
        var total = ((currentRate * gold_wt * 0.8)/10).toFloat()
        val totalAmount=String.format("%.2f", total)
        if(!totalAmount.equals("0.00")){
            binding.tvLoanAmount.text = getString(R.string.Rs) + totalAmount
            binding.tvLoanAmountstar.visibility=View.VISIBLE
        }

    }
    fun loanCalculator(currentRate: Float, gold_wt: Float): Int {
        var total = (currentRate * gold_wt * 0.8).roundToInt()
        binding.tvLoanAmount.setText(total)
        return total
    }
    fun loadJSONFromAsset(): String? {
        var json: String? = null
        json = try {
            val `is`: InputStream = requireActivity()!!.getAssets().open("city.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnLoanApply -> {

                Navigation.findNavController(v)
                    .navigate(R.id.action_nav_loan_to_nav_loanApplicationFragment)
            }

            R.id.btnLoanApplybtn->{
                Navigation.findNavController(v)
                    .navigate(R.id.action_nav_loan_to_nav_loanApplicationFragment)
            }
        }
    }

    @Throws(ParseException::class)
    private fun getDoubleFromString(value: String): Double {
        val format: NumberFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NumberFormat.getInstance(Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val number: Number = format.parse(value)
        return number.toDouble()
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
    fun calGoldRate(goldCurrentRate24: BigDecimal, s: BigDecimal): String {
        val amt = goldCurrentRate24 * s
        val rate = amt / BigDecimal(24)
        return rate.toString()
    }

    override fun onTestimonialViewClick(_clist: GlitterTestimonial, position: Int) {
        val str = Html.fromHtml(_clist.testDescription ?: "").toString()
        val strLength = str.length
        var name=_clist.testName
        dialog=TestimonialDialogFragment(str,name)
        activity?.supportFragmentManager?.let { dialog.show(it, "testimonial") }
    }
}
