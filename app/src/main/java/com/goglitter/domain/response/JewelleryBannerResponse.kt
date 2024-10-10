package com.goglitter.domain.response

data class JewelleryBannerResponse (
    val status: String? = null,
    val msg: String? = null,
    val result: List<JewelleryBanner>? = null
)
data class JewelleryBanner (
    val homeId:String?=null,
    val homeHeading:String?=null,
    val homeImage:String?=null,
    val OrderBy:String?=null,
    val status:String?=null
)