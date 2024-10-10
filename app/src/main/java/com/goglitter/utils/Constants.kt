package com.goglitter.utils

object Constants {

    const val TAG = "Glitter_APP"
    const val KEY_TITLE = "key_title"
    const val KEY_IMG = "key_img"
    const val KEY_SUBTITLE = "key_subtitle"
    const val KEY_OTP = "key_otp"
    const val KEY_INVEST = "key_invest"
    const val KEY_OTP_FIELD_PHONE = "key_otp_field_phone"
    const val KEY_OTP_FIELD_EMAIL = "key_otp_field_email"
    const val KEY_OTP_PHONE = "key_otp_phone"
    const val KEY_OTP_EMAIL = "key_otp_email"
    const val KEY_APP_ID = "app_id"
    const val KEY_BUTTON_POSITIVE = "key_positive"
    const val KEY_BUTTON_NEGETIVE = "key_negetive"

    //Share preference constants
    const val AUTH_TOKEN = "auth_token"
    const val PREFS_TOKEN_FILE = "prefs_token_file"
    const val EVERIFIED = "everified"
    const val VALUE = "value"
    const val EMAIL = "email"
    const val GLITTER_ID = "glitter_id"
    const val FCM_TOKEN = "fcmToken"
    const val NAME = "user_name"
    const val OTP = "otp"
    const val EMAILOTP = "email_otp"
    const val IMAGE_URL = "profile_url"

    //DEMO
    /*const val BASE_URL_1 = "https://demos.thewebdemo.net/gillter-dev/web/"
    const val IMAGE_PATH = "https://demos.thewebdemo.net/gillter-dev/uploads/profile/"*/

    //LIVE
    const val BASE_URL_1 = "https://glitter-india.com/api.glitter-india.com/web/"
    const val IMAGE_PATH = "https://glitter-india.com/api.glitter-india.com/uploads/profile/"

    //GOLD RATE
    const val BASE_URL = "https://api.metalpriceapi.com/v1/"
    const val BASE_URL_GOLD = "https://gold-rates-india.p.rapidapi.com/"
    const val BASE_URL_2 = "https://apibankingonesandbox.icicibank.com/"
    const val VERSION = "version_1"

    // const val BASE_URL = "https://gold-silver-live-prices.p.rapidapi.com/"
   /* const val BASE_URL_2 = "https://scrmuat.icicibank.com/restapi/"*/
    //https://demos.thewebdemo.net/gillter-dev/web/uploads/profile/version_1/415881-img_20230625_170128.jpg
    // const val GOLD_RATE = BASE_URL+"getGoldRate"
    const val GOLD_RATE = BASE_URL + "carat"
    const val RAPID_GOLD_RATE = BASE_URL_GOLD + "api/state-gold-rates"

   /* const val CRM_TOKEN = BASE_URL_2 + "oauth2/token"*/
    const val CRM_TOKEN = BASE_URL_2 + "clientcredentials/GenerateAccessToken"
   /* const val CRM_LEAD = BASE_URL_2 + "crmwebapi/saveobject"*/
    const val CRM_LEAD = BASE_URL_2 + "api/v0/CRMNext/save"

    const val SIGN_UP = BASE_URL_1 + VERSION + "/api/signup"
    const val Loan_BANNER = BASE_URL_1 + VERSION + "/api/getGoldBanner"
    const val APPLICATION_DETAILS = BASE_URL_1 + VERSION + "/api/applicationDetails"
    const val JEWELLER_APPLICATION_DETAILS = BASE_URL_1 + VERSION + "/api/jewelleryapplicationDetails"
    const val INVESTOR_APPLICATION_DETAILS = BASE_URL_1 + VERSION + "/api/investapplicationDetails"
    const val JEWELLERY_BANNER = BASE_URL_1 + VERSION + "/api/getBanner"
    const val SEND_OTP = BASE_URL_1 + VERSION + "/api/getOtp"
    const val VERIFY_OTP = BASE_URL_1 + VERSION + "/api/verifyOtp"
    const val USER_PROFILE = BASE_URL_1 + VERSION + "/api/userProfile"
    const val TESTIMONIAL = BASE_URL_1 + VERSION + "/api/getTestimonial"
    const val INVESTOR_TESTIMONIAL = BASE_URL_1 + VERSION + "/api/getInvestorTestimonial"
    const val SPONSORS = BASE_URL_1 + VERSION + "/api/getLogos"
    const val JEWELLERS = BASE_URL_1 + VERSION + "/api/getJewellers"
    const val CONTACTS = BASE_URL_1 + VERSION + "/api/contactDetails"
    const val GLITTERPAGES = BASE_URL_1 + VERSION + "/api/getPages"
    const val ENQUIRYLIST = BASE_URL_1 + VERSION + "/api/enquiryList"
    const val JWELLERYENQUIRYLIST = BASE_URL_1 + VERSION + "/api/jewlleryEnquiryList"
    const val APPLICATION_OTP_VERIFICATION = BASE_URL_1 + VERSION + "/api/verifyOtpApp"
    const val FAQ_LIST = BASE_URL_1 + VERSION + "/api/getFaqList"
    const val EDIT_PROFILE_IMAGE = BASE_URL_1 + VERSION + "/api/editProfile"
    const val GET_TOKEN = BASE_URL_1 + VERSION + "/api/getToken"
    const val NOTIFICATION_LIST = BASE_URL_1 + VERSION + "/api/getNotificationList"
    const val NOTIFICATION_READ = BASE_URL_1 + VERSION + "/api/notificationRead"
    const val JEWELLARY_APPLICATION_OTP_VERIFICATION = BASE_URL_1 + VERSION + "/api/verifyOtpJew"
    const val INVEST_APPLICATION_OTP_VERIFICATION = BASE_URL_1 + VERSION + "/api/verifyOtpInvest"
    const val GLITTER_LOAN_APPLICATION = BASE_URL_1 + VERSION + "/api/loanApplicationForm"
    const val CRM_LOAN_APPLICATION = BASE_URL_1 + VERSION + "/api/crmSave"
    const val INVESTMENT_LIST = BASE_URL_1 + VERSION + "/api/getInvestCategory"
    const val DELETE_ACCOUNT = BASE_URL_1 + VERSION + "/api/deleteUser"
    const val GOLD_RATE_SYSTEM = BASE_URL_1 + VERSION + "/api/getGoldRate"
    const val UPDATE_EMAIL = BASE_URL_1 + VERSION + "/api/updateProfile"
    const val UPDATE_VERSION = BASE_URL_1 + VERSION + "/api/getUpdatedVersion"
    const val UPDATE_LOAN_ENQUIRY = BASE_URL_1 + VERSION + "/api/crmUpdate"
}