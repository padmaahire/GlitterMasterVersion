package com.goglitter.ui.GoldRate


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.ParseException
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.goglitter.R
import com.goglitter.databinding.FragmentGlitterRateBinding
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.util.*
import kotlin.math.roundToInt

/**
@author-Padma A
date-04/07/2023
update-24/04/24
 **/
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class GlitterRateFragment : Fragment() {
    private lateinit var binding: FragmentGlitterRateBinding
    private var param1: String? = null
    private var param2: String? = null
    val viewModel: AuthViewModel by viewModels()
    var goldCurrentRate24: String?=null
    var goldCurrentRate22:  String?=null
    var goldCurrentRate21:  String?=null
    var goldCurrentRate18:  String?=null
    var goldCurrentRate16:  String?=null
    var goldCurrentRate23:  String?=null

    val troy=31.1035

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
        binding= FragmentGlitterRateBinding.inflate(layoutInflater,container,false)
        val view=binding.root
      // getGoldRateList()
      // getRapidGoldRateList()
        getGoldRate()

        // Log a custom event
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        val params = Bundle()
        params.putString("gold_rate_value", "goGlitter") // Using "gold_rate_value" as the parameter name
        mFirebaseAnalytics.logEvent("gold_rate_event", params) // Using "gold_rate_event" as the event name

        return view
    }
    fun getGoldRateList() {
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


                    goldCurrentRate16 = it.data.data!!.gold16k!!
                    goldCurrentRate18 = it.data.data!!.gold18k!!
                    goldCurrentRate22 = it.data.data!!.gold22k!!
                    goldCurrentRate24 = it.data.data!!.gold24k!!
                    goldCurrentRate23 = it.data.data!!.gold23k!!
                    goldCurrentRate21 = it.data.data!!.gold21k!!

                    binding.rate16K.text= getString(R.string.Rs)+ (goldCurrentRate16!!.toDouble() * 5).roundToInt().toString()
                    binding.rate18K.text= getString(R.string.Rs)+(goldCurrentRate18!!.toDouble() * 5).roundToInt().toString()
                    binding.rate22K.text= getString(R.string.Rs)+(goldCurrentRate22!!.toDouble() * 5).roundToInt().toString()
                    binding.rate23K.text= getString(R.string.Rs)+(goldCurrentRate23!!.toDouble() * 5).roundToInt().toString()
                    binding.rate21K.text= getString(R.string.Rs)+(goldCurrentRate21!!.toDouble() * 5).roundToInt().toString()
                    binding.rate24K.text= getString(R.string.Rs)+(goldCurrentRate24!!.toDouble() * 5).roundToInt().toString()

                    binding.progressCircular.isVisible = false
                }

            }
        }
    }
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
                        binding.mainLay.visibility=View.VISIBLE
                        binding.tvError.visibility-View.GONE
                        goldCurrentRate24 = goldRateData.find { it.state == "Maharashtra" }?.TenGram24K ?: ""
                        goldCurrentRate22 = goldRateData.find { it.state == "Maharashtra" }?.TenGram22K ?: ""

                       goldCurrentRate23= calGoldRate(goldCurrentRate24!!.toBigDecimal(),"23".toBigDecimal())
                       goldCurrentRate21=calGoldRate(goldCurrentRate24!!.toBigDecimal(),"21".toBigDecimal())
                        goldCurrentRate18=calGoldRate(goldCurrentRate24!!.toBigDecimal(),"18".toBigDecimal())
                        goldCurrentRate16=calGoldRate(goldCurrentRate24!!.toBigDecimal(),"16".toBigDecimal())

                        binding.rate24K.text=getString(R.string.Rs)+(goldCurrentRate24!!.toInt()/10)
                        binding.rate22K.text=getString(R.string.Rs)+(goldCurrentRate22!!.toInt()/10)
                        binding.rate23K.text=getString(R.string.Rs)+(goldCurrentRate23!!.toInt()/10)
                        binding.rate21K.text=getString(R.string.Rs)+(goldCurrentRate21!!.toInt()/10)
                        binding.rate18K.text=getString(R.string.Rs)+(goldCurrentRate18!!.toInt()/10)
                        binding.rate16K.text=getString(R.string.Rs)+(goldCurrentRate16!!.toInt()/10)

                    } else {
                        binding.mainLay.visibility=View.GONE
                        binding.tvError.visibility=View.VISIBLE

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
                            binding.mainLay.visibility=View.VISIBLE
                            binding.tvError.visibility-View.GONE
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

                            binding.rate24K.text=getString(R.string.Rs)+(goldCurrentRate24!!.toInt())
                            binding.rate22K.text=getString(R.string.Rs)+(goldCurrentRate22!!.toInt())
                            binding.rate23K.text=getString(R.string.Rs)+(goldCurrentRate23!!.toInt())
                            binding.rate21K.text=getString(R.string.Rs)+(goldCurrentRate21!!.toInt())
                            binding.rate18K.text=getString(R.string.Rs)+(goldCurrentRate18!!.toInt())
                            binding.rate16K.text=getString(R.string.Rs)+(goldCurrentRate16!!.toInt())

                        } else {
                            binding.mainLay.visibility=View.GONE
                            binding.tvError.visibility=View.VISIBLE
                        }
                        binding.progressCircular.isVisible = false
                    }else{

                    }

                }

            }
        }
    }

    @Throws(ParseException::class)
    private fun getDoubleFromString(value: Double): String? {
        val formatter = Formatter()
        val number=formatter.format("%,.2f",value)
        return number.toString()
    }

    override fun onResume() {
        super.onResume()
      //  getGoldRateList()
       // getRapidGoldRateList()
        getGoldRate()
    }

    fun calGoldRate(goldCurrentRate24: BigDecimal, s: BigDecimal): String {
        val amt = goldCurrentRate24 * s
        val rate = amt / BigDecimal(24)
        return rate.toString()
    }
}