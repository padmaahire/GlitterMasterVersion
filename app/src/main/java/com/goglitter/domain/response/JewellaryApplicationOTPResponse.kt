package com.goglitter.domain.response

data class JewellaryApplicationOTPResponse (
    val status:String,
    val status_code:String,
    val msg:String,
    val result: VerifiedJewellUserResponse
)

data class VerifiedJewellUserResponse (
    val jewId:String?=null,
    val regId:String?=null,
    val jewName:String?=null,
    val jewLname:String?=null,
    val jewMobile:String?=null,
    val otp:String?=null,
    val emailotp:String?=null,
    val jewEmail:String?=null,
    val verfiyEmail:String?=null,
    val verfiyMob:String?=null,
    val dateAdded:String?=null,
    val status:String?=null
)

/*
"jewId": "37",
"regId": "78",
"jewName": "Tomas",
"jewLname": "Kiyar",
"jewMobile": "+919975443309",
"jewEmail": "ss@gmail.com",
"otp": "4991",
"emailotp": "",
"verfiyEmail": "1",
"verfiyMob": "0",
"dateAdded": "2023-08-16 16:12:03",
"status": "1",
"verifyStatus": true*/
