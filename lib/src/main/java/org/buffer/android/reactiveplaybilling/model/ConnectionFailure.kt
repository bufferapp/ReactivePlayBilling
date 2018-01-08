package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

class ConnectionFailure(@BillingClient.BillingResponse val result: Int? = null) : Throwable()