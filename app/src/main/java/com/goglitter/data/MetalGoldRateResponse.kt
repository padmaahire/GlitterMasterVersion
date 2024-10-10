package com.goglitter.data


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * @Author: Padma Ahire
 * @Date: 19/06/23
 */

object SOMain {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val gson = Gson()

        val json1 =
            "{\"10k\":\"429.34185231\",\"12k\":\"515.21022277\",\"14k\":\"601.07859324\",\"16k\":\"686.9469637\",\"18k\":\"772.81533416\",\"21k\":\"901.61788986\",\"22k\":\"944.55207509\",\"23k\":\"987.48626032\",\"24k\":\"1030.42044555\",\"6k\":\"1030.42044555\",\"8k\":\"343.47348185\",\"9k\":\"386.40766708\"}"


        val goldCaratRate: GoldCaratRate = gson.fromJson(json1, GoldCaratRate::class.java)


    }
}
data class MetalGoldRateResponse (
    val success: Boolean? = null,
    val base: String? = null,
    val timestamp: String? = null,
    val data: GoldCaratRate? = null
)


data class GoldCaratRate (

    @SerializedName("10k")
    var gold10k: String? = null,
    @SerializedName("12k")
    var gold12k: String? = null,
    @SerializedName("14k")
    var gold14k: String? = null,
    @SerializedName("16k")
    var gold16k: String? = null,
    @SerializedName("18k")
    var gold18k: String? = null,
    @SerializedName("21k")
    var gold21k: String? = null,
    @SerializedName("22k")
    var gold22k: String? = null,
    @SerializedName("23k")
    var gold23k: String? = null,
    @SerializedName("24k")
    var gold24k: String? = null,
    @SerializedName("6k")
    var gold6k: String? = null,
    @SerializedName("8k")
    var gold8k: String? = null,
    @SerializedName("9k")
    var gold9k: String? = null

    )
