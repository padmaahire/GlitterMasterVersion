package com.goglitter.ui.GoldRate

/**
 * @Author: Padma Ahire
 * @Date: 19/04/24
 */
data class RapidGoldRateResponse (
    val GoldRate: List<RapidGoldRate>?=null
)
data class RapidGoldRate(
    val state: String,
    val TenGram22K: String,
    val TenGram24K: String
)