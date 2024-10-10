package com.goglitter.domain.request

data class VerifyOtpRequest (
    val value:String?=null,
    val otp:String?=null,
    val regEmail:String?=null,
    val regMobile:String?=null
)
