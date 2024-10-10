package com.goglitter.domain.response

data class GlitterNotificationResponse (
    val status:String?=null,
    val msg:String?=null,
    val result: NotificationResult?=null,
 )
data class NotificationResult(
    val is_read:String?=null
)