package com.goglitter.domain.response

data class LeadResponse(
    val ObjectKey:String?=null,
    val Result: CRMResult?=null,
    val IsSuccess:Boolean,
    val Message:String?=null
)

data class CRMResult (
    val LeadID:List<Int>?=null
 )

