package com.goglitter.domain.response

import android.os.Parcel
import android.os.Parcelable

data class JewellerListResponse(
    val status: String? = null,
    val msg: String? = null,
    val result: List<JewellerBanner>? = null
)

data class JewellerBanner(
    val ID: String? = null,
    val Name: String? = null,
    val Image: String? = null,
    val BannerImage: String? = null,
    val OrderBy: String? = null,
    val status: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
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
        parcel.writeString(Image)
        parcel.writeString(BannerImage)
        parcel.writeString(OrderBy)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JewellerBanner> {
        override fun createFromParcel(parcel: Parcel): JewellerBanner {
            return JewellerBanner(parcel)
        }

        override fun newArray(size: Int): Array<JewellerBanner?> {
            return arrayOfNulls(size)
        }
    }
}


