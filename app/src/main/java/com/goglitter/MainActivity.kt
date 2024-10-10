package com.goglitter

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.TooltipCompat
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.goglitter.databinding.ActivityMainBinding
import com.goglitter.domain.InvestCategory
import com.goglitter.domain.request.GlitterTokenRequest
import com.goglitter.domain.response.GlitterNotificationList
import com.goglitter.ui.BaseActivity
import com.goglitter.ui.GlitterUpdateCallBackListener
import com.goglitter.ui.Listener.GlitterDialogCallbackListener
import com.goglitter.ui.OTPActivity.DialogBaseFragment
import com.goglitter.ui.Registration.LoginActivity
import com.goglitter.ui.UpdateDialogFragment
import com.goglitter.ui.auth.AuthViewModel
import com.goglitter.utils.Constants
import com.goglitter.utils.NetworkResult
import com.goglitter.utils.TokenManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt


/**
@author-Padma A
date-30/5/2023
update-26/4/24 App Update pop up
 **/
@AndroidEntryPoint
class MainActivity : BaseActivity(), GlitterDialogCallbackListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var glitterDialog: DialogBaseFragment
    private lateinit var dialog: UpdateDialogFragment
    var _regId: String? = null
    val viewModel: AuthViewModel by viewModels()
    val PERMISSION_REQUEST_CODE = 112
    var glitter_notification_lst = ArrayList<GlitterNotificationList>()
    private var version_code: String? = null
    private var app_version_code: String? = null

    @Inject
    lateinit var tokenManager: TokenManager
    private val RC_APP_UPDATE = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //  getVersionCode()
        initViews()
        initToolbar()
        _regId = tokenManager.getSalesId()
        getUser(_regId!!)
        //  getNotification()
        fetchNotification()

        val versionName = getVersionName()
        binding.btnversion.text = "Version $versionName"

        try {
            val pInfo: PackageInfo =
                this.getPackageManager().getPackageInfo(this.getPackageName(), 0)
            app_version_code = pInfo.versionCode.toString()
            Log.d("VVV", app_version_code.toString())
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")) {
                getNotificationPermission()
            }
        }
        binding.notification.setOnClickListener() {
            binding.tvNotiCount.visibility = View.GONE
            navController.navigate(R.id.notificationFragment)
        }

        //Get Firebase Messaging Token for notification
        FirebaseMessaging.getInstance().token.addOnSuccessListener { result ->
            if (result != null) {
                val fcm_token = result.toString()
                Log.d("fcm_token", fcm_token)
                getGlitterToken(_regId, fcm_token)
            } else {
            }
        }
        binding.bottomNav.menu.forEach {
            TooltipCompat.setTooltipText(binding.bottomNav.findViewById(it.itemId), null)
        }
        binding.btnContactUs.setOnClickListener() {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navController.navigate(R.id.contactGoGlitterFragment)
            binding.imgHamburger.visibility = View.GONE
        }
        binding.about.setOnClickListener() {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navController.navigate(R.id.aboutFragment)
            binding.imgHamburger.visibility = View.GONE
        }
        binding.btnFaq.setOnClickListener() {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navController.navigate(R.id.FAQFragment)
            binding.imgHamburger.visibility = View.GONE
        }
        binding.btnCondition.setOnClickListener() {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navController.navigate(R.id.termsConditionFragment)
            binding.imgHamburger.visibility = View.GONE
        }
        binding.btnDisclaimer.setOnClickListener() {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navController.navigate(R.id.disclaimerFragment)
            binding.imgHamburger.visibility = View.GONE
        }
        binding.btnPrivacyPolicy.setOnClickListener() {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            navController.navigate(R.id.privacyPolicyFragment)
            binding.imgHamburger.visibility = View.GONE
        }
        binding.btnLogout.setOnClickListener() {
            Handler().postDelayed({
                glitterDialog = DialogBaseFragment(this).newInstance(
                    R.drawable.ic_ok,
                    "Logout",
                    "Are you sure want to logout?",
                    "Yes",
                    "No",
                    true
                )
                glitterDialog.show(this.supportFragmentManager, "signature")
            }, 200)
        }
        drawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, 0, 0)
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.setDrawerIndicatorEnabled(false)
        drawerToggle.syncState()
        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))
        binding.imgHamburger.setOnClickListener({
            if (!binding.drawerLayout.isDrawerVisible(binding.navView)) {
                binding.drawerLayout.openDrawer(binding.navView)
            } else {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        })

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    binding.imgHamburger.visibility = View.VISIBLE
                    binding.drawerLayout.closeDrawer(GravityCompat.START)

                    true
                }
                R.id.nav_loan -> {
                    binding.imgHamburger.visibility = View.VISIBLE
                    binding.drawerLayout.closeDrawer(GravityCompat.START)

                    true
                }
                R.id.nav_jwellery -> {
                    binding.imgHamburger.visibility = View.VISIBLE
                    binding.drawerLayout.closeDrawer(GravityCompat.START)

                    true
                }
                R.id.nav_invest -> {
                    binding.imgHamburger.visibility = View.VISIBLE
                    binding.drawerLayout.closeDrawer(GravityCompat.START);

                    true
                }
                else -> {
                    binding.imgHamburger.visibility = View.GONE
                    if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    false
                }
            }
        }
    }

    private fun initViews() {
        navigationImagesMargin(binding.bottomNav)
        binding.bottomNav.setOnNavigationItemSelectedListener { it ->
            binding.bottomNav.post {
                navigationImagesMargin(binding.bottomNav)
            }
            true
        }
        setSupportActionBar(binding.toolbar)
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home,
            R.id.nav_loan,
            R.id.nav_jwellery,
            R.id.nav_invest,
            R.id.nav_account
        ).build()

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            navigationImagesMargin(binding.bottomNav)
            if (destination.id == R.id.nav_loanApplicationFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("Application Details")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.goldLoanCalculatorFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("Loan Eligibility Calculator")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.jewelleryApplicationFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("Application Details")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.investApplicationFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("Application Details")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.contactGoGlitterFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("Contact Us")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.contactUsFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("Contact Us")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.aboutFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("About us")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.FAQFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("FAQ's")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.disclaimerFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("Disclaimer")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.privacyPolicyFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("Privacy Policy")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.termsConditionFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("Terms & Conditions")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.glitterRateFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("Gold Rate")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.profileFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("My Profile")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.enquiryFragment) {
                binding.notification.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("My Enquiry")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
                binding.tvNotiCount.visibility = View.GONE
            } else if (destination.id == R.id.notificationFragment) {
                binding.tvNotiCount.visibility = View.GONE
                binding.imgHamburger.visibility = View.GONE
                binding.notification.visibility = View.GONE
                binding.toolbarsubTitle.visibility = View.VISIBLE
                binding.toolbarsubTitle.setText("Notification")
                binding.logo.visibility = View.GONE
                binding.bottomNav.visibility = View.GONE
            } else {
                binding.notification.visibility = View.VISIBLE
                binding.imgHamburger.visibility = View.VISIBLE
                binding.logo.visibility = View.VISIBLE
                binding.bottomNav.visibility = View.VISIBLE
                binding.toolbarsubTitle.visibility = View.GONE

                if (getNotificationUnreadCount(glitter_notification_lst) > 0) {
                    binding.tvNotiCount.visibility = View.VISIBLE
                } else {
                    binding.tvNotiCount.visibility = View.GONE
                }
            }
        }
        binding.bottomNav.itemIconTintList = null
    }

    private fun navigationImagesMargin(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                navigationImagesMargin(child)
            }
        } else if (view is ImageView) {
            val param = view.layoutParams as ViewGroup.MarginLayoutParams
            param.topMargin = convertDpToPx(2)
            view.layoutParams = param
        }
    }

    private fun convertDpToPx(dp: Int): Int {
        return (dp * (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onResume() {
        super.onResume()
        fetchNotification()
        getVersionCode()

       /* appUpdateManager!!.getAppUpdateInfo()
            .addOnSuccessListener(OnSuccessListener<AppUpdateInfo> { result ->
                if (result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    try {
                        appUpdateManager!!.startUpdateFlowForResult(
                            result,
                            AppUpdateType.IMMEDIATE,
                            this@MainActivity,
                            RC_APP_UPDATE
                        )
                    } catch (e: SendIntentException) {
                        e.printStackTrace()
                    }
                }
            })*/
    }

    override fun DialogDone() {}
    override fun onCustomerDialogOk() {
        tokenManager.saveSalesId("")
        tokenManager.saveEmail("")
        tokenManager.saveMobile("")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    override fun onCustomerDialogCancel() {}
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            updateNotificationCount()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_APP_UPDATE && resultCode != RESULT_OK) {
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getGlitterToken(_regId: String?, fcm_token: String) {
        val request = GlitterTokenRequest(_regId, fcm_token)
        viewModel.GlitterTokenResult(request)

        viewModel.glitterTokenResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {}
                is NetworkResult.Failure -> {}
                is NetworkResult.Success -> {}
                else -> {}
            }
        }
    }

    private fun getUser(_regId: String) {
        viewModel.getUserResponse(_regId)
        viewModel.userProfileResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {}
                is NetworkResult.Failure -> {}
                is NetworkResult.Success -> {
                    try {
                        if (it.data.result != null) {
                            if (it.data.status.equals("success")) {
                                if (it.data.result.profile_image != null && !it.data.result.profile_image.equals(
                                        ""
                                    )
                                ) {
                                    val url = Constants.IMAGE_PATH + it.data.result.profile_image
                                    Log.e("TAG", "URL ====> $url")
                                    Glide.with(this)
                                        .load(Constants.IMAGE_PATH + it.data.result.profile_image)
                                        .into(binding.ivProfileImage)
                                } else {
                                    Glide.with(this).load("")
                                        .placeholder(R.drawable.ic_avatar)
                                        .into(binding.ivProfileImage)
                                }
                                val fname = it.data.result!!.regName
                                val lname = it.data.result!!.regLname
                                binding.tvUserName.setText(fname + " " + lname)
                                val _phoneNo = tokenManager.getMobile()
                                val str = _phoneNo!!.length - 10
                                val substring: String = _phoneNo.toString().substring(str)
                                val str1 = _phoneNo!!
                                val strNew: String = str1.replace(substring, "")
                                binding.tvUserPhone.setText("+91" + "-" + substring)
                                // binding.tvUserPhone.setText(substring)
                            } else {
                                Log.e("TAG", "ERROR")
                            }
                        } else {
                            Log.e("TAG", "ERROR")
                        }
                    } catch (e: Exception) {
                        Log.e("TAG", e.toString())
                    }
                }
                else -> {}
            }
        }
    }

    fun loadImg(img: String) {
        if (img != null && !img.equals("")) {
            Glide.with(this)
                .load(img)
                .into(binding.ivProfileImage)
        } else {
            Glide.with(this).load("")
                .placeholder(R.drawable.ic_avatar)
                .into(binding.ivProfileImage)
        }
    }

    fun getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
        } catch (e: java.lang.Exception) {
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE ->
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // allow

                } else {
                    //deny
                }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        updateNotificationCount()
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    //Navigate bottom menu from fragment
    fun navigate(id: Int) {
        try {
            val bottomNavigationView: BottomNavigationView = binding.bottomNav
            bottomNavigationView.selectedItemId = id
        } catch (e: Exception) {
        }
    }

    fun fetchNotification() {
        if (_regId != null) {
            getNotification()
        }
    }
    private fun getNotification() {
        viewModel.glitterNotificationResult(_regId!!)
        viewModel.glitterNotificationResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {}
                is NetworkResult.Failure -> {}
                is NetworkResult.Success -> {
                    glitter_notification_lst = it.data.result as ArrayList<GlitterNotificationList>
                    if (glitter_notification_lst.size > 0) {
                        if (getNotificationUnreadCount(glitter_notification_lst) > 0) {
                            binding.tvNotiCount.setText(
                                getNotificationUnreadCount(
                                    glitter_notification_lst
                                ).toString() + ""
                            )
                            binding.tvNotiCount.visibility = View.VISIBLE
                        } else {
                            binding.tvNotiCount.visibility = View.GONE
                        }
                    } else {
                        binding.tvNotiCount.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun getNotificationUnreadCount(notificationList: List<GlitterNotificationList>): Int {
        var count = 0
        try {
            for (i in 0 until notificationList.size) {
                if (notificationList[i].is_read!!.equals(0)) {
                    count = count + 1
                }
            }
        } catch (e: java.lang.Exception) {
            e.message
        }
        return count
    }

    fun updateNotificationCount() {
        if (glitter_notification_lst.size > 0) {
            if (getNotificationUnreadCount(glitter_notification_lst) > 0) {
                binding.tvNotiCount.setText(
                    getNotificationUnreadCount(
                        glitter_notification_lst
                    ).toString() + ""
                )
                binding.tvNotiCount.visibility = View.VISIBLE
            } else {
                binding.tvNotiCount.visibility = View.GONE
            }
        } else {
            binding.tvNotiCount.visibility = View.GONE
        }
    }

    private fun getVersionCode() {
        viewModel.updateVersionCode()
        viewModel.glitterAppResponse.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {}
                is NetworkResult.Failure -> {}
                is NetworkResult.Success -> {
                    if (it.data.status_code.equals("200")) {
                        viewModel.clear()
                        version_code = it.data.result!!.appVersion
                      //  version_code = "6"
                        if (!version_code.isNullOrEmpty()) {
                            val currentVersion = version_code.toString()
                            val appVersion = app_version_code.toString()
                            when {
                                currentVersion < appVersion -> {
                                 //   showSnackBar("No Update")
                                }
                                currentVersion > appVersion -> {
                                    //showSnackBar("Update")
                                    dialog = UpdateDialogFragment(object :
                                        GlitterUpdateCallBackListener {
                                        override fun DialogUpdate() {
                                            //PlayStore Url- https://play.google.com/store/apps/details?id=com.goglitter
                                            try {
                                                startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse("https://play.google.com/store/apps/details?id=com.goglitter")
                                                    )
                                                )
                                            } catch (e: ActivityNotFoundException) {
                                                // Handle if Google Play Store app is not installed
                                                // You can open the link in a web browser
                                            }
                                        }
                                    }).newInstance(
                                        "Go-Glitter",
                                        "To use this app, download the latest version.",
                                        "Update"
                                    )
                                    dialog.show(supportFragmentManager, "")
                                }
                                currentVersion == appVersion -> {
                                    // showSnackBar("EQUAL")
                                }
                            }
                        }
                    } else {
                        showSnackBar(it.data.msg.toString())
                    }
                }
                else -> {}
            }
        }
    }
    private fun getVersionName(): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "Unknown"
        }
    }

}

/*
Sign-in SHA1 key generation
MD5: F4:38:4F:B6:EB:D5:52:73:47:D8:FD:6D:E2:DE:EC:5B
SHA1: B3:C0:F7:C8:AE:10:BA:58:00:BA:9F:31:58:08:FC:7B:20:81:73:79
SHA-256: 84:A4:DB:01:C1:EC:3B:FF:84:91:90:11:DE:38:57:64:2B:87:5E:C1:D5:0F:8F:F9:CB:2F:A9:17:F3:91:FC:28*/
