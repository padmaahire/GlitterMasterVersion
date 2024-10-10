package com.goglitter.domain.request

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


object SOMain {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val gson = Gson()
        val json = "{\"Opening Branch_ID_l\":\"Opening Branch_ID_l\"}"

        val crmData: CRMData = gson.fromJson(json, CRMData::class.java)

    }
}

data class Data(
    val _arr:ArrayList<LeadRequest>
)
data class LeadRequest(

 //   val _arr:ArrayList<LeadCRMRequest>

    val ItemId:Int?=null,
    val ItemType:String?=null,
    val ProcessMode:String?=null,
    val OutputFieldList: Array<String>,
    val ObjectData: CRMData?=null
)

/*data class LeadCRMRequest (
    val ItemId:Int?=null,
    val ItemType:String?=null,
    val ProcessMode:String?=null,
    val OutputFieldList: Array<String>,
    val ObjectData:CRMData?=null
 )*/

data class CRMData (
    val Source_of_Lead_l:String?=null,
    @SerializedName("Opening Branch_ID_l")
    val Opening_Branch_ID_l:String?=null,
    val Type_l:String?=null,
    val Channel_l:String?=null,
    val Business_Line_l:String?=null,
    val Customer_Relationship_l:String?=null,
    val Lead_Warmth_l:String?=null,
    val PAN_Number_l:String?=null,
    val IsDedupeSearch:String?=null,
    val Remarks_Aggregate_l:String?=null,
    val Remarks_l:String?=null,
    val FirstName:String?=null,
    val LastName:String?=null,
    val LayoutID:String?=null,
    val LeadOwnerKey:String?=null,
    val LeadOwnerTypeID:String?=null,
    val Email:String?=null,
    val MiddleName:String?=null,
    val MobilePhone:String?=null,
    val ProductCategoryID:String?=null,
    val ProductID:String?=null,
    val RatingID:String?=null,
    val SalutationID:String?=null,
    val StatusCodeID:String?=null,
  )




