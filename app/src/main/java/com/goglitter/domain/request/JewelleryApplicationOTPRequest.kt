package com.goglitter.domain.request

data class JewelleryApplicationOTPRequest (
    val value:String?=null,
    val otp:String?=null,
    val emailotp:String?=null,
    val jewMobile:String?=null,
    val jewEmail:String?=null,
    val jewId:String?=null
)

