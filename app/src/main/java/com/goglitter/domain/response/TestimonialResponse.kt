package com.goglitter.domain.response

data class TestimonialResponse (
    val status:String?=null,
    val msg:String?=null,
    val result:List<GlitterTestimonial>?=null
  )


data class GlitterTestimonial (
    val testID:String?=null,
    val testName:String?=null,
    val testDesignation:String?=null,
    val testImage:String?=null,
    val testDescription:String?=null,
    val status:String?=null,
  )
