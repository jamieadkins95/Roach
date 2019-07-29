package com.jamieadkins.gwent.base

import android.app.Activity
import android.content.Intent
import android.net.Uri

class FeatureNavigator(private val activity: Activity) {

    fun openIntent(intent: Intent) = activity.startActivity(intent.setPackage(activity.packageName))

    fun openCardDetails(cardId: String) = openIntent(getIntentForClassName(CARD_DETAIL_ACTIVITY, Uri.parse(CARD_DETAILS.format(cardId))))

    fun getIntentForClassName(activityName: String, uri: Uri? = null): Intent {
        return Intent().apply {
            setClassName(activity.applicationContext.packageName, activityName)
            data = uri
        }
    }

    companion object {
        private const val CARD_DETAIL_ACTIVITY = "com.jamieadkins.gwent.card.detail.CardDetailsActivity"

        private const val CARD_DETAILS = "roach://card?cardId=%s"
    }

}