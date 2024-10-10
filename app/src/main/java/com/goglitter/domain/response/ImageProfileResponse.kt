package com.goglitter.domain.response

data class ImageProfileResponse (
    val status:String?=null,
    val msg:String?=null,
    val result: ProfileImageResponse?=null
)
data class ProfileImageResponse(
    val regId:String?=null,
    val regName:String?=null,
    val regLname:String?=null,
    val regEmail:String?=null,
    val regMobile:String?=null,
    val dateAdded:String?=null,
    val profile_image:String?=null
)
