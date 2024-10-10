package com.goglitter.domain.response


data class LoanResponse(
    val status:String,
    val msg:String,
    val result: LoanUser
 )
data class LoanUser (
    val appId:String,
    val regId:String,
    val appName:String,
    val appLname:String,
    val appMobile:String,
    val otp:String,
    val emailotp:String,
    val appEmail:String,
    val verfiyEmail:String,
    val verfiyMob:String,
    val LeadID:String,
    val dateAdded:String,
    val status:String
)

/*data class LoanUser (
    val regId:String,
    val appName:String,
    val appLname:String,
    val appEmail:String,
    val appMobile:String,
    val dateAdded:String,
    val appId:String
)*/




