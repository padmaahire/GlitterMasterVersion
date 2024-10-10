package com.goglitter.domain.response

data class TokenResponse (
    val access_token:String?=null,
    val expires_in:Int?=null,
    val token_type:String?=null,
    val refresh_token:String?=null
 )
