package com.goglitter.domain.response

data class PageResponse (
    val status:String?=null,
    val status_code:String?=null,
    val msg:String?=null,
    val result: PageDetails?=null
)
data class PageDetails (
    val pageTitle:String?=null,
    val heading:String?=null,
    val pageDescription:String?=null
)
