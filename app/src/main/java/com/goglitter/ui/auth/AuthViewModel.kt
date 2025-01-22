package com.goglitter.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goglitter.data.JewelleryEnquiryListResponse
import com.goglitter.data.MetalGoldRateResponse
import com.goglitter.data.repositories.AuthRepository
import com.goglitter.domain.InvestCategoryResponse
import com.goglitter.domain.request.*
import com.goglitter.domain.response.*
import com.goglitter.utils.NetworkResult
import com.goglitter.ui.GoldRate.GoldRateResponse
import com.goglitter.ui.GoldRate.RapidGoldRateResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private var authRepository: AuthRepository) : ViewModel() {

    /*private var _goldRateResponse = MutableLiveData<NetworkResult<GoldRateResponse>>()
    val goldRateResponse: LiveData<NetworkResult<GoldRateResponse>> = _goldRateResponse
*/

    private var _goldRateResponse = MutableLiveData<NetworkResult<MetalGoldRateResponse>>()
    val goldRateResponse: LiveData<NetworkResult<MetalGoldRateResponse>> = _goldRateResponse

    private var _rapidgoldRateResponse = MutableLiveData<NetworkResult<RapidGoldRateResponse>>()
    val rapidgoldRateResponse: LiveData<NetworkResult<RapidGoldRateResponse>> = _rapidgoldRateResponse

    private var _goldRateResponseSystem = MutableLiveData<NetworkResult<GoldRateResponse>>()
    val goldRateResponseSystem: LiveData<NetworkResult<GoldRateResponse>> = _goldRateResponseSystem

    private var _leadResponse = MutableLiveData<NetworkResult<LeadResponse>>()
    val leadResponse: LiveData<NetworkResult<LeadResponse>> = _leadResponse

    private var _userResponse = MutableLiveData<NetworkResult<SignUpResponse>>()
    val userResponse: LiveData<NetworkResult<SignUpResponse>> = _userResponse


    private var _bannerResponse = MutableLiveData<NetworkResult<LoanBannerResponse>>()
    val bannerResponse: LiveData<NetworkResult<LoanBannerResponse>> = _bannerResponse

    private var _jewellerBannerResponse = MutableLiveData<NetworkResult<JewelleryBannerResponse>?>()
    val jewellerBannerResponse: LiveData<NetworkResult<JewelleryBannerResponse>?> = _jewellerBannerResponse

    private var _loanResponse = MutableLiveData<NetworkResult<LoanResponse>?>()
    val loanResponse: LiveData<NetworkResult<LoanResponse>?> = _loanResponse

    private var _jewellerUserResponse =
        MutableLiveData<NetworkResult<JewellerApplicationResponse>?>()
    val jewellerUserResponse: LiveData<NetworkResult<JewellerApplicationResponse>?> =
        _jewellerUserResponse


    private var _investorUserResponse = MutableLiveData<NetworkResult<InvestorApplicationResponse>?>()
    val investorUserResponse: LiveData<NetworkResult<InvestorApplicationResponse>?> = _investorUserResponse

    private var _otpResponse = MutableLiveData<NetworkResult<OTPResponse>?>()
    val otpResponse: LiveData<NetworkResult<OTPResponse>?> = _otpResponse

    private var _verifyOtpResponse = MutableLiveData<NetworkResult<VerifyOtpResponse>>()
    val verifyOtpResponse: LiveData<NetworkResult<VerifyOtpResponse>> = _verifyOtpResponse

    private var _userProfileResponse = MutableLiveData<NetworkResult<UserProfileResponse>>()
    val userProfileResponse: LiveData<NetworkResult<UserProfileResponse>> = _userProfileResponse

    private var _leadsResponse = MutableLiveData<NetworkResult<EnquiryListResponse>>()
    val leadsResponse: LiveData<NetworkResult<EnquiryListResponse>> = _leadsResponse

    private var _jewelleryLeadsResponse =
        MutableLiveData<NetworkResult<JewelleryEnquiryListResponse>>()
    val jewelleryLeadsResponse: LiveData<NetworkResult<JewelleryEnquiryListResponse>> =
        _jewelleryLeadsResponse

    private var _glitterPageResponse = MutableLiveData<NetworkResult<PageResponse>>()
    val glitterPageResponse: LiveData<NetworkResult<PageResponse>> = _glitterPageResponse

    private var _testimonialResponse = MutableLiveData<NetworkResult<TestimonialResponse>>()
    val testimonialResponse: LiveData<NetworkResult<TestimonialResponse>> = _testimonialResponse

    private var _investorTestimonialResponse = MutableLiveData<NetworkResult<TestimonialResponse>>()
    val investorTestimonialResponse: LiveData<NetworkResult<TestimonialResponse>> = _investorTestimonialResponse

    private var _sponosorResponse = MutableLiveData<NetworkResult<SponsorResponse>>()
    val sponsorResponse: LiveData<NetworkResult<SponsorResponse>> = _sponosorResponse

    private var _jewellerResponse = MutableLiveData<NetworkResult<JewellerListResponse>>()
    val jewellerResponse: LiveData<NetworkResult<JewellerListResponse>> = _jewellerResponse

    private var _tokenResponse = MutableLiveData<NetworkResult<TokenResponse>>()
    val tokenResponse: LiveData<NetworkResult<TokenResponse>> = _tokenResponse

    private var _contactResponse = MutableLiveData<NetworkResult<ContactResponse>>()
    val contactResponse: LiveData<NetworkResult<ContactResponse>> = _contactResponse

    private var _faqResponse = MutableLiveData<NetworkResult<FAQResponse>>()
    val faqResponse: LiveData<NetworkResult<FAQResponse>> = _faqResponse

    private var _appOTPResponse = MutableLiveData<NetworkResult<ApplicationOTPResponse>>()
    val appOTPResponse: LiveData<NetworkResult<ApplicationOTPResponse>> = _appOTPResponse

    private var _jewelleryOTPResponse =
        MutableLiveData<NetworkResult<JewellaryApplicationOTPResponse>?>()
    val jewelleryOTPResponse: LiveData<NetworkResult<JewellaryApplicationOTPResponse>?> =
        _jewelleryOTPResponse

    private var _investOTPResponse =
        MutableLiveData<NetworkResult<InvestApplicationOTPResponse>?>()
    val investOTPResponse: LiveData<NetworkResult<InvestApplicationOTPResponse>?> =
        _investOTPResponse

    private var _profileImageResponse = MutableLiveData<NetworkResult<ImageProfileResponse>?>()
    val profileImageResponse: LiveData<NetworkResult<ImageProfileResponse>?> = _profileImageResponse

    private var _profileEmailResponse = MutableLiveData<NetworkResult<ImageProfileResponse>?>()
    val profileEmailResponse: LiveData<NetworkResult<ImageProfileResponse>?> = _profileEmailResponse


    private var _glitterTokenResponse = MutableLiveData<NetworkResult<GlitterTokenResponse>>()
    val glitterTokenResponse: LiveData<NetworkResult<GlitterTokenResponse>> = _glitterTokenResponse

    private var _glitterNotificationResponse =
        MutableLiveData<NetworkResult<NotificationResponse>>()
    val glitterNotificationResponse: LiveData<NetworkResult<NotificationResponse>> =
        _glitterNotificationResponse

    private var _glitterNotificationReaderResponse =
        MutableLiveData<NetworkResult<GlitterNotificationResponse>>()
    val glitterNotificationReaderResponse: LiveData<NetworkResult<GlitterNotificationResponse>> =
        _glitterNotificationReaderResponse

    private var _crmLeadData = MutableLiveData<NetworkResult<CRMResponse>>()
    val crmLeadData: LiveData<NetworkResult<CRMResponse>> = _crmLeadData

    private var _investCategoryResponse = MutableLiveData<NetworkResult<InvestCategoryResponse>?>()
    val investCategoryResponse: LiveData<NetworkResult<InvestCategoryResponse>?> = _investCategoryResponse

    private var _glitterDeleteResponse = MutableLiveData<NetworkResult<GlitterNotificationResponse>>()
    val glitterDeleteResponse: LiveData<NetworkResult<GlitterNotificationResponse>> = _glitterDeleteResponse

    private var _glitterAppCodeResponse = MutableLiveData<NetworkResult<VersionCodeResponse>?>()
    val glitterAppResponse: LiveData<NetworkResult<VersionCodeResponse>?> = _glitterAppCodeResponse

    private var _glitterLoanUpdateResponse = MutableLiveData<NetworkResult<CrmEnquiryUpdateResponse>?>()
    val glitterLoanUpdateResponse: LiveData<NetworkResult<CrmEnquiryUpdateResponse>?> = _glitterLoanUpdateResponse

    /* fun getGoldResponseList() {
         viewModelScope.launch {
             authRepository.getGoldRateList().collect {
                 _goldRateResponse.postValue(it)
             }
         }
     }*/

    fun getGoldResponseList() {
        viewModelScope.launch {
            authRepository.getGoldRateList().collect {
                _goldRateResponse.postValue(it)
            }
        }
    }

    fun getRapidGoldResponseList() {
        viewModelScope.launch {
            authRepository.getRapidGoldRateList().collect {
                _rapidgoldRateResponse.postValue(it)
            }
        }
    }

    fun getGoldResponseSystemList() {
        viewModelScope.launch {
            authRepository.getGoldRateSystemList().collect {
                _goldRateResponseSystem.postValue(it)
            }
        }
    }

    fun getUserResponse(request: SignUpRequest) {
        viewModelScope.launch {
            authRepository.getUser(request).collect {
                _userResponse.postValue(it)
            }
        }
    }

    fun getBannerList() {
        viewModelScope.launch {
            authRepository.getLoanBanner().collect {
                _bannerResponse.postValue(it)
            }
        }
    }

    fun getJewelleryBannerList() {
        viewModelScope.launch {
            authRepository.getJewelleryBanner().collect {
                _jewellerBannerResponse.postValue(it)
            }
        }
    }

    fun getLoanUserResponse(request: LoanRequest) {
        viewModelScope.launch {
            authRepository.loanUser(request).collect {
                //   _loanResponse.postValue(it)
                _loanResponse.value = it
            }
        }
    }

    fun getApplicationUserResponse(request: JewellRequest) {
        viewModelScope.launch {
            authRepository.Customer(request).collect {
                //  _jewellerUserResponse.postValue(it)
                _jewellerUserResponse.value = it
            }
        }
    }

    fun getApplicationInvestorResponse(request: InvestRequest) {
        viewModelScope.launch {
            authRepository.Investor(request).collect {
                //  _jewellerUserResponse.postValue(it)
                _investorUserResponse.value = it
            }
        }
    }

    fun getOTPResponse(request: OTPRequest) {
        viewModelScope.launch {
            authRepository.OTP(request).collect {
                //  _otpResponse.postValue(null)
                _otpResponse.value = it

            }
        }
    }

    fun getVerifiedOTPResponse(request: VerifyOtpRequest) {
        viewModelScope.launch {
            authRepository.OTPVerification(request).collect {
                _verifyOtpResponse.postValue(it)
            }
        }
    }

    fun getUserResponse(regId: String) {
        viewModelScope.launch {
            authRepository.getUserProfile(regId).collect {
                _userProfileResponse.postValue(it)
            }
        }
    }

    fun getLeadResponse(regId: String) {
        viewModelScope.launch {
            authRepository.getLeadList(regId).collect {
                _leadsResponse.postValue(it)
            }
        }
    }

    fun getJewelleryLeadResponse(regId: String) {
        viewModelScope.launch {
            authRepository.getJewelleryLeadList(regId).collect {
                _jewelleryLeadsResponse.postValue(it)
            }
        }
    }

    fun getTestimonialResponse() {
        viewModelScope.launch {
            authRepository.getTestimonialInfo().collect {
                _testimonialResponse.postValue(it)
            }
        }
    }

    fun getInvestorTestimonialResponse() {
        viewModelScope.launch {
            authRepository.getInvestorTestimonialInfo().collect {
                _investorTestimonialResponse.postValue(it)
            }
        }
    }

    fun getSponsoredResponse() {
        viewModelScope.launch {
            authRepository.getSponsoredList().collect {
                _sponosorResponse.postValue(it)
            }
        }
    }

    fun getJewellerResponse() {
        viewModelScope.launch {
            authRepository.getJewellersList().collect {
                _jewellerResponse.postValue(it)
            }
        }
    }

    /*  fun getToken(request: TokenRequest){
          viewModelScope.launch {
              authRepository.getCrmToken(request).collect {
                  _tokenResponse.postValue(it)
              }
          }
      }*/
/*
    fun fetchCrmToken(authorizationToken: String, request: TokenRequest) {
        viewModelScope.launch {
            authRepository.getCrmToken(authorizationToken, request).collect {
                _tokenResponse.value = it
            }
        }
    }*/

    fun fetchCrmToken(authorization: String) {
        viewModelScope.launch {
            authRepository.getCrmToken(authorization).collect {
                _tokenResponse.value = it
            }
        }
    }

    fun glitterContact(request: ContactRequest) {
        viewModelScope.launch {
            authRepository.glitterContact(request).collect {
                _contactResponse.postValue(it)
            }
        }
    }

    fun getPageResponse(pageName: String) {
        viewModelScope.launch {
            authRepository.getGlitterPage(pageName).collect {
                _glitterPageResponse.postValue(it)
            }
        }
    }

    fun getFAQSResponse() {
        viewModelScope.launch {
            authRepository.getFaqsList().collect {
                _faqResponse.postValue(it)
            }
        }
    }


    fun ApplicationOTPResonse(request: ApplicationOTPRequest) {
        viewModelScope.launch {
            authRepository.appOTPResponse(request).collect {
                _appOTPResponse.postValue(it)
            }
        }
    }

    fun JewelleryOTPResonse(request: JewelleryApplicationOTPRequest) {
        viewModelScope.launch {
            authRepository.jewelleryOTPResponse(request).collect {
                _jewelleryOTPResponse.postValue(it)
            }
        }
    }

    fun InvestOTPResonse(request: InvestApplicationOTPRequest) {
        viewModelScope.launch {
            authRepository.investOTPResponse(request).collect {
                _investOTPResponse.postValue(it)
            }
        }
    }

    fun uploadProfileImage(regId: RequestBody, profile_image: MultipartBody.Part) {
        viewModelScope.launch {
            authRepository.uploadProfileImage(regId, profile_image).collect {
                //  _profileImageResponse.postValue(it)
                _profileImageResponse.value = it
            }
        }
    }

    fun uploadProfileEmail(request: EmailRequest) {
        viewModelScope.launch {
            authRepository.uploadProfileEmailId(request).collect {
                //  _profileImageResponse.postValue(it)
                _profileEmailResponse.value = it
            }
        }
    }

    fun GlitterTokenResult(request: GlitterTokenRequest) {
        viewModelScope.launch {
            authRepository.glitterTokenUpdate(request).collect {
                _glitterTokenResponse.postValue(it)
            }
        }
    }

    fun glitterNotificationResult(regId: String) {
        viewModelScope.launch {
            authRepository.glitterNotification(regId).collect {
                _glitterNotificationResponse.postValue(it)
            }
        }
    }
    fun GlitterNotificationResult(request: GlitterNotificationRequest) {
        viewModelScope.launch {
            authRepository.glitterNotificationUpdate(request).collect {
                _glitterNotificationReaderResponse.postValue(it)
            }
        }
    }
    fun crmLoanData(request: CrmLoanRequest) {
        viewModelScope.launch {
            authRepository.crmLoanData(request).collect {
                _crmLeadData.postValue(it)
            }
        }
    }
    /*fun crmLoanData(request: CrmRequest) {
        viewModelScope.launch {
            authRepository.crmLoanData(request).collect {
                _crmLeadData.postValue(it)
            }
        }
    }*/

    fun getInvestorCategoryList() {
        viewModelScope.launch {
            authRepository.getInvestList().collect {
                _investCategoryResponse.postValue(it)
            }
        }
    }
    fun deleteUserAccount(request: DeleteAccountRequest) {
        viewModelScope.launch {
            authRepository.deleteUserAccount(request).collect {
                _glitterDeleteResponse.postValue(it)
            }
        }
    }

    fun updateVersionCode() {
        viewModelScope.launch {
            authRepository.updateVersionCode().collect {
                _glitterAppCodeResponse.postValue(it)
            }
        }
    }

    fun updateLoanEnquiry(request: crmEnquiryUpdateRequest) {
        viewModelScope.launch {
            authRepository.updateLoanEnquiry(request).collect {
                _glitterLoanUpdateResponse.postValue(it)
            }
        }
    }

    //Use to clear response history
    fun clear() {
        _otpResponse.value = null
        // _appOTPResponse.value=null
        _loanResponse.value = null
        _jewellerUserResponse.value = null
        _investorUserResponse.value = null
        _profileImageResponse.value = null
        _profileEmailResponse.value = null
        _glitterAppCodeResponse.value = null
        _glitterLoanUpdateResponse.value=null
    }

}