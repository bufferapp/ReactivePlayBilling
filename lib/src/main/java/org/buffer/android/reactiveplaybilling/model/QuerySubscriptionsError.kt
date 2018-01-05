package org.buffer.android.reactiveplaybilling.model

import org.buffer.android.reactiveplaybilling.BillingResult

class QuerySubscriptionsError(val result: BillingResult) : Throwable()