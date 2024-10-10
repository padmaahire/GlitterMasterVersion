package com.goglitter.domain.response

data class ContactResponse (
    val status:String?=null,
    val msg:String?=null,
    val result: ContactDetails?=null
)
data class ContactDetails (
    val contactName:String?=null,
    val contactLname:String?=null,
    val contactEmail:String?=null,
    val contactMobile:String?=null,
    val contactMessage:String?=null,
    val dateAdded:String?=null,
    val id:String?=null
 )
