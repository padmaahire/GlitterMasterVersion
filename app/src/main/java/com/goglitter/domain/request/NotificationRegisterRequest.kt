package com.goglitter.domain.request

data class NotificationRegisterRequest (
    val device: String? = null,
    val fcmToken: String? = null
 )