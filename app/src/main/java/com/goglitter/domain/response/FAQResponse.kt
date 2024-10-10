package com.goglitter.domain.response

data class FAQResponse(
    val status:String?=null,
    val msg:String?=null,
    val result:List<FAQsLIST>?=null
)
data class FAQsLIST(
    val faqId:String?=null,
    val faqHeading:String?=null,
    val faqDescription:String?=null,
    val isActive:String?=null,
    val status:String?=null,
    var isExpanded:Boolean = false
)



