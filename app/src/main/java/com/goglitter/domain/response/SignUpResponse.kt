package com.goglitter.domain.response

/**
@author-Padma A
date-10/06/2023
 **/
data class SignUpResponse(
    val status:String,
    val msg:String,
    val result: GlitterUser

)
data class GlitterUser (
    val regName:String,
    val regLname:String,
    val regEmail:String,
    val regMobile:String,
    val value:String,
    val dateAdded:String,
    val regId:String
)
