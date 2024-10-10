package com.goglitter.domain.request

data class InvestRequest (
    val investName:String?=null,
    val investCategory:String?=null,
    val regId:String?=null,
    val investLname:String?=null,
    val investEmail:String?=null,
    val investMobile:String?=null,
    val pincode:String?=null,
)