package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

sealed class SubscriptionResponse(@BillingClient.BillingResponse val result: Int?) {

    data class SubscriptionSuccess(@BillingClient.BillingResponse private val billingResponse: Int)
        : SubscriptionResponse(billingResponse)

    data class SubscriptionFailure(@BillingClient.BillingResponse
                                   private val billingResponse: Int? = null)
        : SubscriptionResponse(billingResponse)

}