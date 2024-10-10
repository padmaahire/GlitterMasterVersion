package com.goglitter.domain.response

data class SponsorResponse (
    val status:String?=null,
    val msg:String?=null,
    val result:List<GlitterSponsors>?=null
 )

data class GlitterSponsors(
    val logoId:String?=null,
    val logoHeading:String?=null,
    val logoImage:String?=null,
    val sortOrder:String?=null,
    val status:String?=null
)
