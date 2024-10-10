package com.goglitter.domain.response

class InvestorApplicationResponse(
val status:String,
val msg:String,
val result: InvestorUser
)
data class InvestorUser (
    val investID:String,
    val regId:String,
    val investCategory:String,
    val investName:String,
    val investLname:String,
    val investMobile:String,
    val investEmail:String,
    val otp:String,
    val emailotp:String,
    val verfiyEmail:String,
    val verfiyMob:String,
    val dateAdded:String,
    val status:String
)
