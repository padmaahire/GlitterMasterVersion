package com.goglitter.domain

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


object SOMain {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val gson = Gson()
        val json = "{\"variations per 10g\":\"variations per 10g\"}"
        val json2 = "{\"per value\":\"per value\"}"
        val json1 =
            "{\"Gold 24 Karat (Rs ₹)\":\"59,480\",\"Gold 22 Karat (Rs ₹)\":\"54,523\",\"Gold 20 Karat (Rs ₹)\":\"49,567\",\"Gold 18 Karat (Rs ₹)\":\"44,610\",\"Gold 16 Karat (Rs ₹)\":\"39,653\",\"Gold 14 Karat (Rs ₹)\":\"34,697\",\"Gold 12 Karat (Rs ₹)\":\"29,740\",\"Gold 10 Karat (Rs ₹)\":\"24,783\"}"

        val goldRateResponse: GoldRateResponse = gson.fromJson(json, GoldRateResponse::class.java)
        val goldVariation: GoldVariation = gson.fromJson(json1, GoldVariation::class.java)
        val gold: GoldPrice = gson.fromJson(json2, GoldPrice::class.java)
        System.out.println(goldRateResponse)
    }
}

data class GoldRateResponse(
    val location: String? = null,
    @SerializedName("variations per 10g")
    var variationsPer10G: GoldVariation? = null,
    val GOLD: GoldPrice? = null
)

data class GoldVariation(
    @SerializedName("Gold 24 Karat (Rs ₹)")
    var gold24Karat: String? = null,
    @SerializedName("Gold 22 Karat (Rs ₹)")
    var gold22Karat: String? = null,
    @SerializedName("Gold 20 Karat (Rs ₹)")
    var gold20Karat: String? = null,
    @SerializedName("Gold 18 Karat (Rs ₹)")
    var gold18Karat: String? = null,
    @SerializedName("Gold 16 Karat (Rs ₹)")
    var gold16Karat: String? = null,
    @SerializedName("Gold 14 Karat (Rs ₹)")
    var gold14Karat: String? = null,
    @SerializedName("Gold 12 Karat (Rs ₹)")
    var gold12Karat: String? = null,
    @SerializedName("Gold 10 Karat (Rs ₹)")
    var gold10Karat: String? = null,
)

data class GoldPrice(
    val price: String? = null,
    val change: String? = null,
    @SerializedName("per value")
    val perValue: String? = null
)

