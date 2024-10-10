package com.goglitter.ui

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.goglitter.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren


abstract class BaseActivity : AppCompatActivity() {

    val job = Job()
    val coroutineScope = CoroutineScope(Dispatchers.Main + job)
    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.coroutineContext.cancelChildren()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarGradiant(activity: Activity, background : Drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
            window.navigationBarColor = activity.resources.getColor(android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }
    }

    private fun takeToPlaystore(packageName: String?) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            val url = "https://play.google.com/store/apps/details?id=$packageName"
            openUrl(url)
        }
    }

    private fun openUrl(url: String) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No related activity found", Toast.LENGTH_SHORT).show()
        }
    }

    fun showAlert( message : String,button_title : String = "Okay", isCancellable : Boolean = true,
                   showCancel : Boolean = true, sucessBlock : () -> Unit) {

        try {
            val builder1 = AlertDialog.Builder(this)
            builder1.setMessage(message)
            builder1.setTitle(getString(R.string.app_name))
            builder1.setCancelable(isCancellable)
            builder1.setPositiveButton(button_title) { dialog, id ->
                sucessBlock()
            }
            if (showCancel) {
                builder1.setNegativeButton(
                    "Cancel"
                ) { dialog, id ->
                    dialog.cancel()
                }
            }
            val alert11 = builder1.create()
            alert11.show()
        } catch (e: Exception) {

        }
    }

    open fun getRootView(): View? {
        val contentViewGroup = findViewById<View>(android.R.id.content) as ViewGroup
        var rootView: View? = null
        if (contentViewGroup != null) rootView = contentViewGroup.getChildAt(0)
        if (rootView == null) rootView = window.decorView.rootView
        return rootView
    }

    open fun showSnackBarWithOK(@StringRes res: Int) {
        val rootView: View? = getRootView()
        if (rootView != null) {
            val snackbar = Snackbar.make(getRootView()!!, res, Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction(R.string.ok, object : View.OnClickListener {
                override fun onClick(v: View?) {
                    snackbar.dismiss()
                }
            })
            snackbar.show()
        }
    }

    open fun showSnackBarWithOK(message: String) {
        val rootView: View? = getRootView()
        if (rootView != null) {
            val snackbar = Snackbar.make(getRootView()!!, message, Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction(R.string.ok, object : View.OnClickListener {
                override fun onClick(v: View?) {
                    snackbar.dismiss()
                }
            })
            snackbar.show()
        }
    }

    open fun showSnackBar(@StringRes res: Int) {
        val rootView: View? = getRootView()
        if (rootView != null) Snackbar.make(rootView, res, Snackbar.LENGTH_LONG).show()
    }

    open fun showSnackBar(message :String) {
        val rootView: View? = getRootView()
        if (rootView != null) Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()


       /* to Show snackbar at Top of the screen

       val snack: Snackbar = Snackbar.make(rootView!!, message, Snackbar.LENGTH_LONG)
        val view = snack.view
        val params = view!!.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.translationY = 80.toFloat() // Adjust the value accordingly
        view!!.layoutParams = params
        snack.show()*/
    }

    fun View.hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }



  fun showCustomUI() {
        val decorView: View = window.decorView
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        )
    }



}