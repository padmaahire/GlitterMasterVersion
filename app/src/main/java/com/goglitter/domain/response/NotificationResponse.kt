package com.goglitter.domain.response

import android.os.Parcel
import android.os.Parcelable

data class NotificationResponse (
val status: String? = null,
val msg: String? = null,
val result: List<GlitterNotificationList>? = null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(GlitterNotificationList)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeString(msg)
        parcel.writeTypedList(result)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationResponse> {
        override fun createFromParcel(parcel: Parcel): NotificationResponse {
            return NotificationResponse(parcel)
        }

        override fun newArray(size: Int): Array<NotificationResponse?> {
            return arrayOfNulls(size)
        }
    }
}

data class GlitterNotificationList (
    val notificationID: String? = null,
    val title: String? = null,
    val message: String? = null,
    val dateAdded: String? = null,
    var is_read: Int?= null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(notificationID)
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeString(dateAdded)
        parcel.writeValue(is_read)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GlitterNotificationList> {
        override fun createFromParcel(parcel: Parcel): GlitterNotificationList {
            return GlitterNotificationList(parcel)
        }

        override fun newArray(size: Int): Array<GlitterNotificationList?> {
            return arrayOfNulls(size)
        }
    }
}


