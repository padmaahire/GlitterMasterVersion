package com.goglitter.domain

data class InvestCategoryResponse (
        val status: String? = null,
        val msg: String? = null,
        val result: List<InvestCategory>? = null
)
data class InvestCategory (
        val categoryID:String?=null,
        val categoryName:String?=null,
        val status:String?=null
)
