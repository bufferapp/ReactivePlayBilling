package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

class ConsumptionError(@BillingClient.BillingResponse val result: Int) : Throwable()