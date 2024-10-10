package com.goglitter.domain.response

data class VersionCodeResponse (
    val status:String?=null,
    val status_code:String?=null,
    val msg:String?=null,
    val result: AppVersionData?=null
)
data class AppVersionData(
    val appVersion:String?=null
)