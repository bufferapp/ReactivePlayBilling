package org.buffer.android.reactiveplaybilling.model

import org.buffer.android.reactiveplaybilling.BillingResult

class SubscriptionError(val result: BillingResult) : Throwable()