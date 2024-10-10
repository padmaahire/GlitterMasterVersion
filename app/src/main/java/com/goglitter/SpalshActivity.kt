package com.goglitter

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import com.goglitter.databinding.ActivitySpalshBinding
import com.goglitter.domain.response.UserProfileResponse
import com.goglitter.ui.BaseActivity
import com.goglitter.ui.Registration.LoginActivity
import com.goglitter.ui.Registration.SignUpActivity
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.NetworkResult
import com.goglitter.utils.TokenManager
import com.goglitter.utils.isNetworkAvailable
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SpalshActivity : BaseActivity() {
    lateinit var binding: ActivitySpalshBinding
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpalshBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(android.R.color.transparent))
        }
        val handler = Handler()
        handler.postDelayed({
            val id = tokenManager.getSalesId()

            if (isNetworkAvailable(this)) {
                if (tokenManager.getSalesId() != null && !tokenManager.getSalesId().equals("")) {
                    getUser(id!!)
                } else {
                    val intent = Intent(this, SignUpActivity::class.java)
                  //  val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                showSnackBar(" No internet connection! ")
            }
        }, 1000)
    }

    override fun onResume() {
        super.onResume()
        //Use to hide status bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }

    private fun getUser(id: String) {
        viewModel.getUserResponse(id)
        viewModel.userProfileResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.progressCircular.isVisible = it.isLoading
                }
                is NetworkResult.Failure -> {
                    if (!isNetworkAvailable(this)) {
                        showSnackBar(" No internet connection! ")
                    } else {
                        showSnackBar(it.errorMessage)
                    }
                    binding.progressCircular.isVisible = false
                }
                is NetworkResult.Success -> {
                    if(!it.data.status.equals("fail")){
                        navigate(it.data)
                    }else{
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
            }
        }
    }

    fun navigate(data: UserProfileResponse) {

        binding.progressCircular.visibility = View.GONE
        if (tokenManager.getSalesId() != null && !tokenManager.getSalesId().equals("")) {
            val handler = Handler()
            handler.postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)

        } else {
            val handler = Handler()
            handler.postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }
    }
}