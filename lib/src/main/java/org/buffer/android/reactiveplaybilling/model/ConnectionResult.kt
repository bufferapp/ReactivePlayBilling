package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

sealed class ConnectionResult(@BillingClient.BillingResponse val result: Int?) {

    data class ConnectionSuccess(@BillingClient.BillingResponse private val billingResponse: Int)
        : ConnectionResult(billingResponse)

    data class ConnectionFailure(@BillingClient.BillingResponse
                                 private val billingResponse: Int? = null)
        : ConnectionResult(billingResponse)

}