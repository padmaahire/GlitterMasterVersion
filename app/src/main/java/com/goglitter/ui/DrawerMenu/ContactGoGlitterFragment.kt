package com.goglitter.ui.DrawerMenu

import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.goglitter.databinding.FragmentContactGoGlitterBinding
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import java.util.*

/**
@author-Padma A
date-18/07/2023
 **/
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class ContactGoGlitterFragment : Fragment() {
    lateinit var binding: FragmentContactGoGlitterBinding
    private var param1: String? = null
    private var param2: String? = null
    val viewModel: AuthViewModel by viewModels()

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
        binding=FragmentContactGoGlitterBinding.inflate(layoutInflater,container,false)
        val view=binding.root

        //getContactUsPage()

        binding.tvEmailContact.setOnClickListener(){
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("support@GoGlitter-india.com"))
            val pm = requireActivity()!!.packageManager
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
        binding.tvPhoneContact.setOnClickListener(){
            val intent = Intent(Intent.ACTION_DIAL)
          //  intent.data = Uri.parse("tel:022-454545467")
            intent.data = Uri.parse("tel:7558511128")
            startActivity(intent)
        }
        return view
    }
    private fun getContactUsPage() {

        viewModel.getPageResponse("contact-us")

        viewModel.glitterPageResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }

                is NetworkResult.Failure -> {
                    binding.progressCircular.isVisible = false

                }
                is NetworkResult.Success -> {
                  //  binding.tvContact.setText(it.data.result!!.pageDescription)
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }

    }

}