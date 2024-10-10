package com.goglitter.domain.response

data class JewellerApplicationResponse(
    val status:String,
    val msg:String,
    val result: JewellerUser
    )
data class JewellerUser (
    val jewId:String,
    val regId:String,
    val jewName:String,
    val jewLname:String,
    val jewMobile:String,
    val jewEmail:String,
    val otp:String,
    val emailotp:String,
    val verfiyEmail:String,
    val verfiyMob:String,
    val dateAdded:String,
    val status:String
)

