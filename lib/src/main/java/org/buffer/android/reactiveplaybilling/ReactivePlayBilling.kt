package org.buffer.android.reactiveplaybilling

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.buffer.android.reactiveplaybilling.model.*

open class ReactivePlayBilling constructor(context: Context) : PurchasesUpdatedListener {

    private val publishSubject = PublishSubject.create<List<Purchase>>()
    private var billingClient: BillingClient =
            BillingClient.newBuilder(context).setListener(this).build()

    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
            publishSubject.onNext(purchases)
        } else {
            publishSubject.onError(PurchasesUpdatedError(responseCode))
        }
    }

    open fun connect(): Observable<ConnectionResult> {
        return Observable.create {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(@BillingClient.BillingResponse
                                                    responseCode: Int) {
                    if (responseCode == BillingClient.BillingResponse.OK) {
                        it.onNext(ConnectionResult(responseCode))
                    } else {
                        it.onError(ConnectionFailure(responseCode))
                    }
                }

                override fun onBillingServiceDisconnected() {
                    it.onError(ConnectionFailure())
                }
            })
        }
    }

    open fun observePurchaseUpdates(): Observable<List<Purchase>> {
        return publishSubject
    }

    open fun queryItemsForPurchase(skuList: List<String>): Observable<List<SkuDetails>> {
        return Observable.create {
            val params = SkuDetailsParams.newBuilder()
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
            billingClient.querySkuDetailsAsync(params.build()) { responseCode, p1 ->
                if (responseCode == BillingClient.BillingResponse.OK) {
                    it.onNext(p1)
                } else {
                    it.onError(ItemsForPurchaseQueryError(responseCode))
                }
            }
        }
    }

    fun querySubscriptionsForPurchase(skuList: List<String>): Observable<List<SkuDetails>> {
        return Observable.create {
            val params = SkuDetailsParams.newBuilder()
            params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
            billingClient.querySkuDetailsAsync(params.build()) { responseCode, p1 ->
                if (responseCode == BillingClient.BillingResponse.OK) {
                    it.onNext(p1)
                } else {
                    it.onError(ItemsForSubscriptionQueryError(responseCode))
                }
            }
        }
    }

    open fun purchaseItem(skuId: String, activity: Activity): Observable<PurchaseResponse> {
        return Observable.create {
            val flowParams = BillingFlowParams.newBuilder()
                    .setSku(skuId)
                    .setType(BillingClient.SkuType.INAPP)
                    .build()
            val responseCode = billingClient.launchBillingFlow(activity, flowParams)
            if (responseCode == BillingClient.BillingResponse.OK) {
                it.onNext(PurchaseResponse(responseCode))
            } else {
                it.onError(PurchaseError(responseCode))
            }
        }
    }

    open fun queryPurchaseHistory(): Observable<List<Purchase>> {
        return Observable.create {
            billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP) {
                responseCode, result ->
                if (responseCode == BillingClient.BillingResponse.OK && result != null) {
                    it.onNext(result)
                } else {
                    it.onError(QueryPurchasesError(responseCode))
                }
            }
        }
    }

    open fun querySubscriptionHistory(): Observable<List<Purchase>> {
        return Observable.create {
            billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS)
            { responseCode, result ->
                if (responseCode == BillingClient.BillingResponse.OK && result != null) {
                    it.onNext(result)
                } else {
                    it.onError(QuerySubscriptionsError(responseCode))
                }

            }
        }
    }

    open fun consumeItem(purchaseToken: String): Observable<ConsumptionResponse> {
        return Observable.create {
            billingClient.consumeAsync(purchaseToken) { responseCode, outToken ->
                if (responseCode == BillingClient.BillingResponse.OK) {
                    it.onNext(ConsumptionResponse(responseCode, outToken))
                } else {
                    it.onError(ConsumptionError(responseCode))
                }
            }
        }
    }

    open fun purchaseSubscription(skuId: String, activity: Activity): Observable<SubscriptionResponse> {
        return Observable.create {
            val flowParams = BillingFlowParams.newBuilder()
                    .setSku(skuId)
                    .setType(BillingClient.SkuType.SUBS)
                    .build()
            val responseCode = billingClient.launchBillingFlow(activity, flowParams)
            if (responseCode == BillingClient.BillingResponse.OK) {
                it.onNext(SubscriptionResponse(responseCode))
            } else {
                it.onError(SubscriptionError(responseCode))
            }
        }
    }

}