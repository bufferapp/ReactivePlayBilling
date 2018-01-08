package org.buffer.android.reactiveplaybilling.model

import com.android.billingclient.api.BillingClient

class SubscriptionResponse(@BillingClient.BillingResponse val result: Int)