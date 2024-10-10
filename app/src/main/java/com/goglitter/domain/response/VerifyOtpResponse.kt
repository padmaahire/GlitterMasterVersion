package com.goglitter.domain.response

data class VerifyOtpResponse(
    val status:String?=null,
    val status_code:String?=null,
    val msg:String?=null,
    val result: OTPVerification?=null
)
data class OTPVerification(
    val regId:String?=null,
    val regName:String?=null,
    val regLname:String?=null,
    val regMobile:String?=null,
    val regEmail:String?=null,
    val otp:String?=null,
    val value:String?=null,
    val dateAdded:String?=null,
    val status:String?=null,
    val isRegistered:Int?=null
)

