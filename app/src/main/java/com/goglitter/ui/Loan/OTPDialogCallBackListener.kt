package com.goglitter.ui.Loan

interface OTPDialogCallBackListener {

    fun verifyOTP()
    fun resendOTP()
    fun finishThisActivity()
}