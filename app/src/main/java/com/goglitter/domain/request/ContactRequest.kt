package com.goglitter.domain.request

data class ContactRequest (
    val contactName:String?=null,
    val contactLname:String?=null,
    val contactEmail:String?=null,
    val contactMobile:String?=null,
    val contactMessage:String?=null
 )

