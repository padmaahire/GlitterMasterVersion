package com.goglitter.domain.response

data class ApplicationOTPResponse (
    val status:String,
    val status_code:String,
    val msg:String,
    val result: VerifiedUserResponse
        )

data class VerifiedUserResponse(
    val appId:String?=null,
    val regId:String?=null,
    val appName:String?=null,
    val appLname:String?=null,
    val appMobile:String?=null,
    val otp:String?=null,
    val emailotp:String?=null,
    val appEmail:String?=null,
    val verfiyEmail:String?=null,
    val verfiyMob:String?=null,
    val dateAdded:String?=null,
    val status:String?=null,
)


/*

{
    "status": "success",
    "status_code": "200",
    "msg": "OTP Matched Successfully",
    "result": {
    "appId": "89",
    "regId": "78",
    "appName": "ss",
    "appLname": "kk",
    "appMobile": "+919975443389",
    "otp": "5289",
    "appEmail": "ak.4fox@gmail.com",
    "verfiyEmail": "0",
    "verfiyMob": "0",
    "dateAdded": "2023-07-31 19:38:36",
    "status": "1",
    "verifyStatus": true
}
}*/
