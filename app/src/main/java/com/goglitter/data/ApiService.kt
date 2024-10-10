package com.tirgei.retrofitauthorization.data


import com.goglitter.domain.CRMPayloadRequest
import com.goglitter.domain.response.LeadResponse
import com.goglitter.utils.Constants
import retrofit2.Call
import retrofit2.http.*

/**
 * Interface for defining REST request functions
 */
interface ApiService {
    @POST(Constants.CRM_LEAD)
    fun getCRMLead(@Body Request: CRMPayloadRequest):Call<List<LeadResponse>>
}