package com.goglitter.ui.OTPActivity

interface OTPListener {

    fun onInteractionListener()

    fun onOTPComplete(otp: String)
}