package com.goglitter.ui.notification

import com.goglitter.domain.response.GlitterNotificationList

interface OnDialogDismissListener {
    fun onDialogDismissed(updatedList: List<GlitterNotificationList>)
}