package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase

sealed class PurchasesUpdatedResponse(@BillingClient.BillingResponse val result: Int?,
                                      val purchases: List<Purchase>? = null) {

    data class PurchasesUpdatedSuccess(@BillingClient.BillingResponse
                                       private val billingResponse: Int,
                                       private val items: List<Purchase>?)
        : PurchasesUpdatedResponse(billingResponse, items)

    data class PurchaseUpdatedFailure(@BillingClient.BillingResponse
                               private val billingResponse: Int? = null)
        : PurchasesUpdatedResponse(billingResponse)

}