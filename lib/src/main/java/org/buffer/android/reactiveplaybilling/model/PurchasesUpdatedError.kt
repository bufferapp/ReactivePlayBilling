package org.buffer.android.reactiveplaybilling.model

import org.buffer.android.reactiveplaybilling.BillingResult

class PurchasesUpdatedError(val result: BillingResult) : Throwable()