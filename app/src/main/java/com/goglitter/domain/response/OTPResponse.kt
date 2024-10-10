package com.goglitter.domain.response

data class OTPResponse (
    val status:String?=null,
    val status_code:String?=null,
    val msg:String?=null,
    val result:List<OTPSendResponse>?=null,
 )

data class OTPSendResponse (
    val otp:String?=null,
    val regEmail:String?=null,
    val regMobile:String?=null
 )


