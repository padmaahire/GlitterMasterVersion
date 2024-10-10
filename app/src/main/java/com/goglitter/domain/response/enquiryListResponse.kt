package com.goglitter.domain.response

/*
data class enquiryListResponse(
    val ID: String? = null,
    val Name: String? = null,
    val date: String? = null,
    val phone: String? = null,
    val email: String? = null
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ID)
        parcel.writeString(Name)
        parcel.writeString(date)
        parcel.writeString(phone)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<enquiryListResponse> {
        override fun createFromParcel(parcel: Parcel): enquiryListResponse {
            return enquiryListResponse(parcel)
        }

        override fun newArray(size: Int): Array<enquiryListResponse?> {
            return arrayOfNulls(size)
        }
    }
}
*/


data class EnquiryListResponse (
    val status:String?=null,
    val msg:String?=null,
    val result:List<ApplicationLeads>?=null
)
data class ApplicationLeads(
    val appId:String?=null,
    val regId:String?=null,
    val appName:String?=null,
    val appLname:String?=null,
    val appMobile:String?=null,
    val appEmail:String?=null,
    val dateAdded:String?=null,
    val status:String?=null
)