package com.goglitter.ui.GoldRate
import com.goglitter.utils.JSONConvertable

data class GoldRateResponse (
    val status:String?=null,
    val msg:String?=null,
    val result: ArrayList<GoldRateData>?=null
):JSONConvertable
data class GoldRateData(
    val goldID:String?=null,
    val goldCarat:String?=null,
    val goldRate:String?=null,
    val isActive:String?=null,
    val status:String?=null
):JSONConvertable

