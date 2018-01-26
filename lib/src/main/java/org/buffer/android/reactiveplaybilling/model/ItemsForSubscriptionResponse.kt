package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails

sealed class ItemsForSubscriptionResponse(@BillingClient.BillingResponse val result: Int?,
                                          val skuDetails: List<SkuDetails>? = null) {

    data class ItemsForSubscriptionSuccess(@BillingClient.BillingResponse
                                         private val billingResponse: Int,
                                         private val items: List<SkuDetails>)
        : ItemsForSubscriptionResponse(billingResponse, items)

    data class ItemsForSubscriptionFailure(@BillingClient.BillingResponse
                                         private val billingResponse: Int? = null)
        : ItemsForSubscriptionResponse(billingResponse)

}