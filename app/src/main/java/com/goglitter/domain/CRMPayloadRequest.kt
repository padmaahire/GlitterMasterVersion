package com.goglitter.domain

data class CRMPayloadRequest (
    var requestId:String,
    var service:String,
    var encryptedKey:String,
    var oaepHashingAlgorithm:String,
    var iv:String,
    var encryptedData:String,
    var clientInfo:String,
    var optionalParam:String,
)


