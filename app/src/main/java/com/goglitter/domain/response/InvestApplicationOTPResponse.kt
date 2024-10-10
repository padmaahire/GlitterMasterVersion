package com.goglitter.domain.response

class InvestApplicationOTPResponse (
    val status:String,
    val status_code:String,
    val msg:String,
    val result: VerifiedInvestUserResponse
)

data class VerifiedInvestUserResponse (
    val investID:String?=null,
    val regId:String?=null,
    val investCategory:String?=null,
    val investName:String?=null,
    val investLname:String?=null,
    val investMobile:String?=null,
    val investEmail:String?=null,
    val otp:String?=null,
    val emailotp:String?=null,
    val jewEmail:String?=null,
    val verfiyEmail:String?=null,
    val verfiyMob:String?=null,
    val dateAdded:String?=null,
    val status:String?=null
)
