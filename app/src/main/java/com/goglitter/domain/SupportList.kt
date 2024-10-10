package com.goglitter.domain

import android.os.Parcel
import android.os.Parcelable

data class SupportList (
    val name:String?=null,
    val img:Int?=null
 ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<SupportList> {
        override fun createFromParcel(parcel: Parcel): SupportList {
            return SupportList(parcel)
        }

        override fun newArray(size: Int): Array<SupportList?> {
            return arrayOfNulls(size)
        }
    }
}