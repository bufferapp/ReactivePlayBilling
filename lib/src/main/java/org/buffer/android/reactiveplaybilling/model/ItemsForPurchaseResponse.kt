package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails

sealed class ItemsForPurchaseResponse(@BillingClient.BillingResponse val result: Int?,
                                      val skus: List<SkuDetails>? = null) {

    data class ItemsForPurchaseSuccess(@BillingClient.BillingResponse
                                       private val billingResponse: Int,
                                       private val items: List<SkuDetails>)
        : ItemsForPurchaseResponse(billingResponse, items)

    data class ItemsForPurchaseFailure(@BillingClient.BillingResponse
                                       private val billingResponse: Int? = null)
        : ItemsForPurchaseResponse(billingResponse)

}