package org.buffer.android.reactiveplaybilling

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.buffer.android.reactiveplaybilling.model.ConnectionResult
import org.buffer.android.reactiveplaybilling.model.ConsumptionResponse
import org.buffer.android.reactiveplaybilling.model.ItemsForPurchaseResponse
import org.buffer.android.reactiveplaybilling.model.ItemsForSubscriptionResponse
import org.buffer.android.reactiveplaybilling.model.PurchaseResponse
import org.buffer.android.reactiveplaybilling.model.PurchasesUpdatedResponse
import org.buffer.android.reactiveplaybilling.model.QueryPurchasesResponse
import org.buffer.android.reactiveplaybilling.model.QuerySubscriptionsResponse
import org.buffer.android.reactiveplaybilling.model.SubscriptionResponse

open class ReactivePlayBilling constructor(context: Context) : PurchasesUpdatedListener {

    private val publishSubject = PublishSubject.create<PurchasesUpdatedResponse>()
    private var billingClient: BillingClient =
            BillingClient.newBuilder(context).setListener(this).build()

    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        if (responseCode == BillingClient.BillingResponse.OK) {
            publishSubject.onNext(PurchasesUpdatedResponse.PurchasesUpdatedSuccess(responseCode,
                    purchases))
        } else {
            publishSubject.onNext(PurchasesUpdatedResponse.PurchaseUpdatedFailure(responseCode))
        }
    }

    open fun connect(): Observable<ConnectionResult> {
        return Observable.create {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(@BillingClient.BillingResponse
                                                    responseCode: Int) {
                    if (responseCode == BillingClient.BillingResponse.OK) {
                        it.onNext(ConnectionResult.ConnectionSuccess(responseCode))
                    } else {
                        it.onNext(ConnectionResult.ConnectionFailure(responseCode))
                    }
                }

                override fun onBillingServiceDisconnected() {
                    it.onNext(ConnectionResult.ConnectionFailure())
                }
            })
        }
    }

    open fun disconnect(): Completable {
        return Completable.defer {
            billingClient.endConnection()
            Completable.complete()
        }
    }

    open fun observePurchaseUpdates(): Observable<PurchasesUpdatedResponse> {
        return publishSubject
    }

    open fun queryItemsForPurchase(skuList: List<String>): Observable<ItemsForPurchaseResponse> {
        return Observable.create {
            val params = SkuDetailsParams.newBuilder()
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
            billingClient.querySkuDetailsAsync(params.build()) { responseCode, p1 ->
                if (responseCode == BillingClient.BillingResponse.OK) {
                    it.onNext(ItemsForPurchaseResponse.ItemsForPurchaseSuccess(responseCode, p1))
                } else {
                    it.onNext(ItemsForPurchaseResponse.ItemsForPurchaseFailure(responseCode))
                }
            }
        }
    }

    fun querySubscriptionsForPurchase(skuList: List<String>):
            Observable<ItemsForSubscriptionResponse> {
        return Observable.create {
            val params = SkuDetailsParams.newBuilder()
            params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
            billingClient.querySkuDetailsAsync(params.build()) { responseCode, p1 ->
                if (responseCode == BillingClient.BillingResponse.OK) {
                    it.onNext(ItemsForSubscriptionResponse.ItemsForSubscriptionSuccess(responseCode,
                            p1))
                } else {
                    it.onNext(ItemsForSubscriptionResponse.ItemsForSubscriptionFailure(
                            responseCode))
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
                it.onNext(PurchaseResponse.PurchaseSuccess(responseCode))
            } else {
                it.onNext(PurchaseResponse.PurchaseFailure(responseCode))
            }
        }
    }

    open fun queryPurchases(): Single<List<Purchase>> {
        return Single.create {
            val queryResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP)

            if (queryResult.responseCode == BillingClient.BillingResponse.OK) {
                it.onSuccess(queryResult.purchasesList)
            } else {
                it.onError(Throwable("Failed to query purchases. Response code: ${queryResult.responseCode}"))
            }
        }
    }

    open fun queryPurchaseHistory(): Observable<QueryPurchasesResponse> {
        return Observable.create {
            billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP) {
                responseCode, result ->
                if (responseCode == BillingClient.BillingResponse.OK && result != null) {
                    it.onNext(QueryPurchasesResponse.QueryPurchasesSuccess(responseCode, result))
                } else {
                    it.onNext(QueryPurchasesResponse.QueryPurchasesFailure(responseCode))
                }
            }
        }
    }

    open fun querySubscriptionHistory(): Observable<QuerySubscriptionsResponse> {
        return Observable.create {
            billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS)
            { responseCode, result ->
                if (responseCode == BillingClient.BillingResponse.OK && result != null) {
                    it.onNext(QuerySubscriptionsResponse.QuerySubscriptionsSuccess(responseCode,
                            result))
                } else {
                    it.onNext(QuerySubscriptionsResponse.QuerySubscriptionsFailure(responseCode))
                }

            }
        }
    }

    open fun consumeItem(purchaseToken: String): Observable<ConsumptionResponse> {
        return Observable.create {
            billingClient.consumeAsync(purchaseToken) { responseCode, outToken ->
                if (responseCode == BillingClient.BillingResponse.OK) {
                    it.onNext(ConsumptionResponse.ConsumptionSuccess(responseCode, outToken))
                } else {
                    it.onNext(ConsumptionResponse.ConsumptionFailure(responseCode))
                }
            }
        }
    }

    open fun purchaseSubscription(skuId: String, activity: Activity):
            Observable<SubscriptionResponse> {
        return Observable.create {
            val flowParams = BillingFlowParams.newBuilder()
                    .setSku(skuId)
                    .setType(BillingClient.SkuType.SUBS)
                    .build()
            val responseCode = billingClient.launchBillingFlow(activity, flowParams)
            if (responseCode == BillingClient.BillingResponse.OK) {
                it.onNext(SubscriptionResponse.SubscriptionSuccess(responseCode))
            } else {
                it.onNext(SubscriptionResponse.SubscriptionFailure(responseCode))
            }
        }
    }

    open fun upgradeSubscription(oldSkuId: String, newSkuId: String, activity: Activity):
            Observable<SubscriptionResponse> {
        return Observable.create {
            val flowParams = BillingFlowParams.newBuilder()
                    .addOldSku(oldSkuId)
                    .setSku(newSkuId)
                    .setType(BillingClient.SkuType.SUBS)
                    .build()
            val responseCode = billingClient.launchBillingFlow(activity, flowParams)
            if (responseCode == BillingClient.BillingResponse.OK) {
                it.onNext(SubscriptionResponse.SubscriptionSuccess(responseCode))
            } else {
                it.onNext(SubscriptionResponse.SubscriptionFailure(responseCode))
            }
        }
    }

}