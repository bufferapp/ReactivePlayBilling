package org.buffer.android.reactiveplaybilling.model

import org.buffer.android.reactiveplaybilling.BillingResult

class QueryPurchasesError(val result: BillingResult) : Throwable()