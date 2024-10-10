package com.goglitter.domain.response

data class CrmEnquiryUpdateResponse (
    val status:String?=null,
    val msg:String?=null,
    val result: StatusResult?=null
 )
data class StatusResult(
    val status:String?=null
)
