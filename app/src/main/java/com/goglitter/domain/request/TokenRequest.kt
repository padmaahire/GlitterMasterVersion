package com.goglitter.domain.request

import com.google.gson.annotations.SerializedName

/*data class TokenRequest (
    val userName:String?=null,
    val password:String?=null,
 )*/

data class TokenRequest(
    @SerializedName("grant_type") val grantType: String
)
