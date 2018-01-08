package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

class PurchaseError(@BillingClient.BillingResponse val result: Int) : Throwable()