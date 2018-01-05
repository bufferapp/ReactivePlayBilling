package org.buffer.android.reactiveplaybilling

import com.android.billingclient.api.BillingClient.BillingResponse.*

object ResponseCodeMapper {

    fun mapBillingResponse(response: Int): BillingResult {
        return when (response) {
            BILLING_UNAVAILABLE -> {
                BillingResult.BILLING_UNAVAILABLE
            }
            DEVELOPER_ERROR -> {
                BillingResult.DEVELOPER_ERROR
            }
            ERROR -> {
                BillingResult.ERROR
            }
            FEATURE_NOT_SUPPORTED -> {
                BillingResult.FEATURE_NOT_SUPPORTED
            }
            ITEM_ALREADY_OWNED -> {
                BillingResult.ITEM_ALREADY_OWNED
            }
            ITEM_NOT_OWNED -> {
                BillingResult.ITEM_NOT_OWNED
            }
            ITEM_UNAVAILABLE -> {
                BillingResult.ITEM_UNAVAILABLE
            }
            OK -> {
                BillingResult.OK
            }
            SERVICE_DISCONNECTED -> {
                BillingResult.SERVICE_DISCONNECTED
            }
            SERVICE_UNAVAILABLE -> {
                BillingResult.SERVICE_UNAVAILABLE
            }
            USER_CANCELED -> {
                BillingResult.USER_CANCELED
            }
            else -> {
                BillingResult.ERROR
            }
        }
    }


}