package com.goglitter.domain.response

data class LoanBannerResponse (
    val status: String? = null,
    val msg: String? = null,
    val result: List<LoanBanner>? = null
)
data class LoanBanner (
    val bannerId:String?=null,
    val bannerHeading:String?=null,
    val bannerImage:String?=null,
    val OrderBy:String?=null,
    val status:String?=null
)