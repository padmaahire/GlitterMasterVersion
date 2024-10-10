package com.goglitter.data.repositories

import com.goglitter.data.API
import com.goglitter.domain.request.*
import com.goglitter.domain.response.TokenResponse
import com.goglitter.ui.Utils
import com.goglitter.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AuthRepository  @Inject constructor(private val apiService: API) {

    /*suspend fun getGoldRateList()  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getGoldRate("a8eb15842amsh93132d7fa2fcdd6p1c8b01jsn55be8ea781af")
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }*/

    suspend fun getGoldRateList()  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getGoldRate("debb369c880924856540fcf3501b453a","debb369c880924856540fcf3501b453a","INR")
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun getRapidGoldRateList()  = flow {
        emit(NetworkResult.Loading(true))
       // val response = apiService.getRapidGoldRate("a8eb15842amsh93132d7fa2fcdd6p1c8b01jsn55be8ea781af","gold-rates-india.p.rapidapi.com")
        val response = apiService.getRapidGoldRate("b2e01a1891msh960d17fcfccbea2p16818djsnb8c1fe90816f","gold-rates-india.p.rapidapi.com")
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun getGoldRateSystemList()  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getGoldRateSystem()
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun getUser(request: SignUpRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.signUp(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun getLoanBanner()  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getLoanBanner()
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun getJewelleryBanner()  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getJewelleryBanner()
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }


    suspend fun loanUser(request: LoanRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.LoanApplication(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun Customer(request: JewellRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.JewellerApplication(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun Investor(request: InvestRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.InvestorApplication(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }


    suspend fun OTP(request: OTPRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.sendOtp(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }


    suspend fun OTPVerification(request: VerifyOtpRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.verifyOtp(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }


    suspend fun getUserProfile(resId:String)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getUserInfo(resId)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }


    suspend fun getLeadList(resId:String)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getEnquiryList(resId)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun getJewelleryLeadList(resId:String)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getJewelleryEnquiryList(resId)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun getTestimonialInfo()  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getTestimonial()
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun getInvestorTestimonialInfo()  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getInvestorTestimonial()
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun getSponsoredList()  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getSponsors()
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }


    suspend fun getJewellersList()  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getJewellers()
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }
   /*suspend fun getCrmToken(request: TokenRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getCRMToken(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }*/


        suspend fun getCrmToken(authorization: String): Flow<NetworkResult<TokenResponse>> = flow {

            emit(NetworkResult.Loading(true))

            try {
                val response = apiService.getCRMToken(authorization, "client_credentials")
                emit(NetworkResult.Success(response))
            } catch (e: Exception) {
                emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
            }
        }.catch { e ->
            emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
        }

   /* suspend fun getCrmToken(authorization: String, request: TokenRequest): Flow<NetworkResult<TokenResponse>> = flow {
        val requestBody = TokenRequest(grantType = "client_credentials")
        emit(NetworkResult.Loading(true))
        val response = apiService.getCRMToken(authorization = authorization, request = request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        if (e is AuthenticationException) {
            throw e // Re-throw the AuthenticationException
        } else {
            emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
        }
    }
*/

    suspend fun glitterContact(request: ContactRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.glitterContacts(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }


    suspend fun getGlitterPage(pageName:String)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getPages(pageName)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }


    suspend fun getFaqsList()  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getFaqList()
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }


    suspend fun appOTPResponse(request: ApplicationOTPRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getApplicationOTP(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }


    suspend fun jewelleryOTPResponse(request: JewelleryApplicationOTPRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getJewellaryApplicationOTP(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun investOTPResponse(request: InvestApplicationOTPRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getInvestOTP(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun uploadProfileImage(regId: RequestBody,profile_image: MultipartBody.Part)  = flow {
        emit(NetworkResult.Loading(true))
        val acceptHeader = "*/*"
        val response = apiService.updateProfileImage(acceptHeader,regId,profile_image)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun uploadProfileEmailId(request:EmailRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.updateEmail(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun glitterTokenUpdate(request: GlitterTokenRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getGlitterToken(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun glitterNotification(regId:String)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getNotificationList(regId)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun glitterNotificationUpdate(request: GlitterNotificationRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.notificationReader(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun crmLoanData(request: CrmLoanRequest) = flow {
        emit(NetworkResult.Loading(true))
        val response=apiService.crmLoanData(request)
        emit(NetworkResult.Success(response))
    }.catch { e->
        emit(NetworkResult.Failure(Utils.resolveError(e)?:"Unknown Error"))
    }


    suspend fun getInvestList()  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.investList()
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }


    suspend fun deleteUserAccount(request: DeleteAccountRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.accountDelete(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }


    suspend fun updateVersionCode()  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.updateVersion()
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }

    suspend fun updateLoanEnquiry(request: crmEnquiryUpdateRequest)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.updateLoanEnquiryList(request)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Failure(Utils.resolveError(e) ?: "Unknown Error"))
    }
}