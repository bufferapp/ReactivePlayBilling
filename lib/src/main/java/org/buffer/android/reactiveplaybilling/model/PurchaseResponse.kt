package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

class PurchaseResponse(@BillingClient.BillingResponse val result: Int)