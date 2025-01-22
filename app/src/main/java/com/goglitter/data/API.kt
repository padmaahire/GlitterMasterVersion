package com.goglitter.data

import com.goglitter.domain.InvestCategoryResponse
import com.goglitter.domain.request.*
import com.goglitter.domain.response.*
import com.goglitter.utils.Constants
import com.goglitter.utils.Constants.CRM_LOAN_APPLICATION
import com.goglitter.utils.Constants.DELETE_ACCOUNT
import com.goglitter.utils.Constants.EDIT_PROFILE_IMAGE
import com.goglitter.utils.Constants.GET_TOKEN
import com.goglitter.utils.Constants.INVESTMENT_LIST
import com.goglitter.utils.Constants.INVEST_APPLICATION_OTP_VERIFICATION
import com.goglitter.utils.Constants.NOTIFICATION_READ
import com.goglitter.domain.request.ApplicationOTPRequest
import com.goglitter.domain.request.ContactRequest
import com.goglitter.domain.request.CrmLoanRequest
import com.goglitter.domain.request.DeleteAccountRequest
import com.goglitter.domain.request.GlitterNotificationRequest
import com.goglitter.domain.request.GlitterTokenRequest
import com.goglitter.domain.request.InvestApplicationOTPRequest
import com.goglitter.domain.request.InvestRequest
import com.goglitter.domain.request.JewellRequest
import com.goglitter.domain.request.JewelleryApplicationOTPRequest
import com.goglitter.domain.request.LoanRequest
import com.goglitter.domain.request.OTPRequest
import com.goglitter.domain.request.SignUpRequest
import com.goglitter.domain.request.VerifyOtpRequest
import com.goglitter.domain.response.ApplicationOTPResponse
import com.goglitter.domain.response.CRMResponse
import com.goglitter.domain.response.ContactResponse
import com.goglitter.domain.response.EnquiryListResponse
import com.goglitter.domain.response.FAQResponse
import com.goglitter.domain.response.GlitterNotificationResponse
import com.goglitter.domain.response.GlitterTokenResponse
import com.goglitter.domain.response.ImageProfileResponse
import com.goglitter.domain.response.InvestApplicationOTPResponse
import com.goglitter.domain.response.InvestorApplicationResponse
import com.goglitter.domain.response.JewellaryApplicationOTPResponse
import com.goglitter.domain.response.JewellerApplicationResponse
import com.goglitter.domain.response.JewellerListResponse
import com.goglitter.domain.response.JewelleryBannerResponse
import com.goglitter.domain.response.LoanBannerResponse
import com.goglitter.domain.response.LoanResponse
import com.goglitter.domain.response.NotificationResponse
import com.goglitter.domain.response.OTPResponse
import com.goglitter.domain.response.PageResponse
import com.goglitter.domain.response.SignUpResponse
import com.goglitter.domain.response.SponsorResponse
import com.goglitter.domain.response.TestimonialResponse
import com.goglitter.domain.response.TokenResponse
import com.goglitter.domain.response.UserProfileResponse
import com.goglitter.domain.response.VerifyOtpResponse
import com.goglitter.ui.GoldRate.GoldRateResponse
import com.goglitter.ui.GoldRate.RapidGoldRateResponse
import com.goglitter.utils.Constants.GOLD_RATE_SYSTEM
import com.goglitter.utils.Constants.UPDATE_EMAIL
import com.goglitter.utils.Constants.UPDATE_LOAN_ENQUIRY
import com.goglitter.utils.Constants.UPDATE_VERSION
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @Author: Padma Ahire
 * @Date: 05/06/23
 */
interface API {

  /*  @GET(Constants.GOLD_RATE)
    suspend fun getGoldRate(@Header("X-RapidAPI-Key") key: String): GoldRateResponse
*/
    @GET(Constants.GOLD_RATE)
    suspend fun getGoldRate(@Header("X-API-KEY") key: String,
                            @Query("X-API-KEY") key1: String,
                            @Query("base") base: String): MetalGoldRateResponse

    @GET(Constants.RAPID_GOLD_RATE)
    suspend fun getRapidGoldRate(@Header("X-RapidAPI-Key") key: String,
                                 @Header("X-RapidAPI-Host") hostKey: String):RapidGoldRateResponse

    @GET(GOLD_RATE_SYSTEM)
    suspend fun getGoldRateSystem():GoldRateResponse

    @POST(Constants.SIGN_UP)
    suspend fun signUp(@Body Request: SignUpRequest): SignUpResponse

    @GET(Constants.Loan_BANNER)
    suspend fun getLoanBanner(): LoanBannerResponse

    @GET(Constants.JEWELLERY_BANNER)
    suspend fun getJewelleryBanner(): JewelleryBannerResponse

    @POST(Constants.APPLICATION_DETAILS)
    suspend fun LoanApplication(@Body Request: LoanRequest): LoanResponse

   /* @POST(Constants.GLITTER_LOAN_APPLICATION)
    suspend fun LoanApplication(@Body Request: LoanRequest): LoanResponse
*/
    @POST(Constants.JEWELLER_APPLICATION_DETAILS)
    suspend fun JewellerApplication(@Body Request: JewellRequest): JewellerApplicationResponse

    @POST(Constants.INVESTOR_APPLICATION_DETAILS)
    suspend fun InvestorApplication(@Body Request: InvestRequest): InvestorApplicationResponse

    @POST(Constants.SEND_OTP)
    suspend fun sendOtp(@Body Request: OTPRequest): OTPResponse

    @POST(Constants.VERIFY_OTP)
    suspend fun verifyOtp(@Body Request: VerifyOtpRequest): VerifyOtpResponse

    @GET(Constants.USER_PROFILE)
    suspend fun getUserInfo(@Query("regId") regId:String): UserProfileResponse

    @GET(Constants.ENQUIRYLIST)
    suspend fun getEnquiryList(@Query("regId") regId:String): EnquiryListResponse

    @GET(Constants.JWELLERYENQUIRYLIST)
    suspend fun getJewelleryEnquiryList(@Query("regId") regId:String): JewelleryEnquiryListResponse

    @GET(Constants.TESTIMONIAL)
    suspend fun getTestimonial(): TestimonialResponse

    @GET(Constants.INVESTOR_TESTIMONIAL)
    suspend fun getInvestorTestimonial(): TestimonialResponse

    @GET(Constants.SPONSORS)
    suspend fun getSponsors(): SponsorResponse

    @GET(Constants.JEWELLERS)
    suspend fun getJewellers(): JewellerListResponse

    /*@POST(Constants.CRM_TOKEN)
    suspend fun getCRMToken(@Body Request: TokenRequest): TokenResponse
*/
   /* @POST(Constants.CRM_TOKEN)
    suspend fun getCRMToken(
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
        @Header("Authorization") authorization: String,
        @Body request: TokenRequest
    ): TokenResponse*/

    @FormUrlEncoded
    @POST(Constants.CRM_TOKEN)
    suspend fun getCRMToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String
    ): TokenResponse

    @POST(Constants.CONTACTS)
    suspend fun glitterContacts(@Body Request: ContactRequest): ContactResponse

    @GET(Constants.GLITTERPAGES)
    suspend fun getPages(@Query("pageName") pageName:String): PageResponse

    @GET(Constants.FAQ_LIST)
    suspend fun getFaqList(): FAQResponse

    @POST(Constants.APPLICATION_OTP_VERIFICATION)
    suspend fun getApplicationOTP(@Body Request: ApplicationOTPRequest): ApplicationOTPResponse

    @POST(Constants.JEWELLARY_APPLICATION_OTP_VERIFICATION)
    suspend fun getJewellaryApplicationOTP(@Body Request: JewelleryApplicationOTPRequest): JewellaryApplicationOTPResponse

    @POST(INVEST_APPLICATION_OTP_VERIFICATION)
    suspend fun getInvestOTP(@Body request: InvestApplicationOTPRequest): InvestApplicationOTPResponse

   /* @Multipart
    @POST(EDIT_PROFILE_IMAGE)
    suspend fun updateProfileImage(
        @Part regId :  MultipartBody.Part,
        @Part profile_image: MultipartBody.Part,
    ): ImageProfileResponse*/

    @Multipart
    @POST(EDIT_PROFILE_IMAGE)
    suspend fun updateProfileImage(
        @Header("Accept") acceptHeader: String,
        @Part("regId") regId: RequestBody,
        @Part profile_image: MultipartBody.Part,
    ): ImageProfileResponse


    @PUT(GET_TOKEN)
    suspend fun getGlitterToken(@Body Request: GlitterTokenRequest): GlitterTokenResponse

    @GET(Constants.NOTIFICATION_LIST)
    suspend fun getNotificationList(@Query("regId") regId:String): NotificationResponse

    @PUT(NOTIFICATION_READ)
    suspend fun notificationReader(@Body Request: GlitterNotificationRequest): GlitterNotificationResponse

    @POST(CRM_LOAN_APPLICATION)
    suspend fun crmLoanData(@Body Request: CrmLoanRequest): CRMResponse

   /* @POST(CRM_LOAN_APPLICATION)
    suspend fun crmLoanData(@Body Request:CrmRequest): CRMResponse*/

    @GET(INVESTMENT_LIST)
    suspend fun investList(): InvestCategoryResponse

    @PUT(DELETE_ACCOUNT)
    suspend fun accountDelete(@Body Request: DeleteAccountRequest): GlitterNotificationResponse

    @POST(UPDATE_EMAIL)
    suspend fun updateEmail(@Body Request:EmailRequest):ImageProfileResponse

    @GET(UPDATE_VERSION)
    suspend fun updateVersion():VersionCodeResponse

    @PUT(UPDATE_LOAN_ENQUIRY)
    suspend fun updateLoanEnquiryList(@Body Request:crmEnquiryUpdateRequest):CrmEnquiryUpdateResponse

}

