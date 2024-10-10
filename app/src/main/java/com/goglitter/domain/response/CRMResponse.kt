package com.goglitter.domain.response

import com.goglitter.utils.JSONConvertable

data class CRMResponse (
    val status:String?=null,
    val msg:String?=null,
    val result: GlitterCRMData?=null
)

data class GlitterCRMData(
    var requestId:String,
    var service:String,
    var encryptedKey:String,
    var oaepHashingAlgorithm:String,
    var iv:String,
    var encryptedData:String,
    var clientInfo:String,
    var optionalParam:String,
)
