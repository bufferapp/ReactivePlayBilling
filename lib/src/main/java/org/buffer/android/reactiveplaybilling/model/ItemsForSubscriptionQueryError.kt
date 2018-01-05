package org.buffer.android.reactiveplaybilling.model

import org.buffer.android.reactiveplaybilling.BillingResult

class ItemsForSubscriptionQueryError(val result: BillingResult) : Throwable()