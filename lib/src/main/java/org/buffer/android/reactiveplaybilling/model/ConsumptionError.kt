package org.buffer.android.reactiveplaybilling.model

import org.buffer.android.reactiveplaybilling.BillingResult

class ConsumptionError(val result: BillingResult) : Throwable()