package com.goglitter.domain.request

data class CRMICICIRequest (
    var CampaignKey:String?=null,
    var Custom:CustomData?=null,
    var Email:String?=null,
    var FirstName:String?=null,
    var LastName:String?=null,
    var MiddleName:String?=null,
    var LayoutKey:String?=null,
    var OwnerCode:String?=null,
    var LeadOwnerTypeID:String?=null,
    var MobilePhone:String?=null,
    var ProductCategoryID:String?=null,
    var ProductKey:String?=null,
    var RatingKey:String?=null,
    var SalutationKey:String?=null,
    var StatusCodeKey:String?=null,
)
data class CustomData(
    var Source_of_Lead_l:String?=null,
    var Type_l:String?=null,
    var Transaction_ID_l:String?=null,
    var Field279:String?=null,
    var Routing_Parameter_l:String?=null,
    var Business_Line_l:String?=null,
    var SUB_Agent_code_l:String?=null,
    var C_Pincode_l:String?=null,
    var Product_Sub_Type_l:String?=null,
    var Loan_Amount_l:String?=null,
)
