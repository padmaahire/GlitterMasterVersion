package com.goglitter.domain.response

data class UserProfileResponse (
    val status:String?=null,
    val status_code:String?=null,
    val msg:String?=null,
    val result: GlitterUserInfo?=null
 )

data class GlitterUserInfo (
    val regId:String?=null,
    val regName:String?=null,
    val regLname:String?=null,
    val regMobile:String?=null,
    val regEmail:String?=null,
    val profile_image:String?=null,
    val otp:String?=null,
    val value:String?=null,
    val dateAdded:String?=null,
    val dateModified:String?=null,
    val status:String?=null
 )
