package com.goglitter.data

data class JewelleryEnquiryListResponse(

val status:String?=null,
val msg:String?=null,
val result:List<JewelleryApplicationLeads>?=null
)
data class JewelleryApplicationLeads(
    val jewId:String?=null,
    val regId:String?=null,
    val jewName:String?=null,
    val jewLname:String?=null,
    val jewMobile:String?=null,
    val jewEmail:String?=null,
    val dateAdded:String?=null,
    val status:String?=null
)
