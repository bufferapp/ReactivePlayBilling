package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

sealed class PurchaseResponse(@BillingClient.BillingResponse val result: Int?) {

    data class PurchaseSuccess(@BillingClient.BillingResponse private val billingResponse: Int)
        : PurchaseResponse(billingResponse)

    data class PurchaseFailure(@BillingClient.BillingResponse
                                 private val billingResponse: Int? = null)
        : PurchaseResponse(billingResponse)

}