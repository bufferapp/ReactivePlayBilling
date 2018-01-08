package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

class ConsumptionResponse(@BillingClient.BillingResponse val result: Int, val outToken: String)