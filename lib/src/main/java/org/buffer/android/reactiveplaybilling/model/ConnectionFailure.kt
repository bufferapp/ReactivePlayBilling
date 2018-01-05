package org.buffer.android.reactiveplaybilling.model

import org.buffer.android.reactiveplaybilling.BillingResult

class ConnectionFailure(val result: BillingResult? = null) : Throwable()