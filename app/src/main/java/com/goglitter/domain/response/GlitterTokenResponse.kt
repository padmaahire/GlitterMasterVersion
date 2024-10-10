package com.goglitter.domain.response

data class GlitterTokenResponse (
    private val status:String?=null,
    private val msg:String?=null,
    private val result: GlitterTokenResult?=null,
        )

data class GlitterTokenResult (
    private val status:String?=null)
