package com.goglitter.domain.request

data class LoanRequest (
    val appName:String?=null,
    val regId:String?=null,
    val appLname:String?=null,
    val appEmail:String?=null,
    val appMobile:String?=null,
    val pincode:String?=null
)
