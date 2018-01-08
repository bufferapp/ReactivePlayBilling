package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

class QueryPurchasesError(@BillingClient.BillingResponse val result: Int) : Throwable()