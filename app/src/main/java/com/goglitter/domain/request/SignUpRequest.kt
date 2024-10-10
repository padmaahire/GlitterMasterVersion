package com.goglitter.domain.request
/**
@author-Padma A
date-10/06/2023
 **/
data class SignUpRequest(
    val regName:String,
    val regLname:String,
    val regMobile:String,
    val regEmail:String,
    val value:String
)
