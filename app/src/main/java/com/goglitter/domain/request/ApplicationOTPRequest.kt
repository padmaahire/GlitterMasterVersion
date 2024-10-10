package com.goglitter.domain.request

data class ApplicationOTPRequest(
    val value:String?=null,
    val otp:String?=null,
    val emailotp:String?=null,
    val appMobile:String?=null,
    val appEmail:String?=null,
    val appId:String?=null
)

