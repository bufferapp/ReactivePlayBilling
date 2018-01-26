# Reactive Play Billing

This project acts us a simple wrapper for the [Play Billing Library](https://developer.android.com/google/play/billing/billing_library.html) from Google for Android. This allows you to interact with the library in a reactive manner and use it within your reacrive streams.

# Functionality

Reactive Play Billing currently supports most of the operations that you will find within the library itself.

## Connecting to Play Billing

You can connect to Play Billing by using the [connect()](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/RxBilling.kt#L26) method and subscribing to changes in the connection status.

    reactiveBilling.connect()
            .subscribe({
                    // Play billing connection successful
            }, {
                    // Play billing connection failed / disconnected
            })

## Observing purchase changes

You can observe purchase change by using the [observePurchaseUpdates()](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/RxBilling.kt#L45) method. This will be called when the user purchases and item / subscription to provide you with the status of the change. You **must** subbscribe to this method if you are carrying our purchases.

    reactiveBilling.observePurchaseUpdates()
            .subscribe({
                    // Purchase complete, handle result
            }, {
                    // Purchase failed, handle result
            })

## Querying in-app items for purchase

You can query items that are available for purchase using the [queryItemsForPurchase()](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/RxBilling.kt#L49) method. When calling, you need to pass in a list of SKU ids that you wish to retrieve the details for. If successful this will return you a list of [SkuDetail](https://developer.android.com/reference/com/android/billingclient/api/SkuDetails.html) instances. Otherwise, a [QueryItemsForPurchaseError](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/model/ItemsForPurchaseResponse.kt) will be returned.

    reactiveBilling.queryItemsForPurchase(skuList)
            .subscribe({
                    // Handle items returned
            }, {
                    // Handle item retrieval failure
            })
            
## Querying subscriptions for purchase

You can query subscriptions that are available for purchase using the [querySubscriptionsForPurchase()](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/RxBilling.kt#L63) method. When calling, you need to pass in a list of SKU ids that you wish to retrieve the details for. If successful this will return you a list of [SkuDetail](https://developer.android.com/reference/com/android/billingclient/api/SkuDetails.html) instances. Otherwise, a [QueryItemsForSubscriptionError](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/model/ItemsForSubscriptionQueryError.kt) will be returned.

    reactiveBilling.querySubscriptionsForPurchase(skuList)
            .subscribe({
                    // Handle items returned
            }, {
                    // Handle item retrieval failure
            })
            
## Purchasing an in-app Item

You can purchase in-app items by calling the [purchaseItem()](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/RxBilling.kt#L78) method. When calling this you need to pass in the SKU of the item which you wish to perform the purchase request on, followed by a reference to the current activity - this is required for activity result events.

A succesfull request will return an instance of the [PurchaseResponse](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/model/PurchaseResponse.kt) class. An unsuccessful request will return an [ItemsForPurchaseQueryError](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/model/ItemsForPurchaseResponse.kt).

    reactiveBilling.purchaseItem(sku, activity)
            .subscribe({
                    // Handle purchase success
            }, {
                    // Handle purchase failure
            })
            
            
## Purchasing a subscription

You can purchase subcsriptions by calling the [purchaseSubscription()](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/RxBilling.kt#L132) method. When calling this you need to pass in the SKU of the subscription which you wish to perform the purchase request on, followed by a reference to the current activity - this is required for activity result events.

A succesfull request will return an instance of the [SubscriptionResponse](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/model/SubscriptionResponse.kt) class. An unsuccessful request will return an [SubscriptionError](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/model/SubscriptionError.kt).

    reactiveBilling.purchaseSubscription(sku, activity)
            .subscribe({
                    // Handle subscription success
            }, {
                    // Handle subscription failure
            })
            
## Querying purchases

You can query previous purchases for the current user by using the [queryPurchaseHistory()](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/ReactivePlayBilling.kt#L92) observable. This will return you a list of Purchase instances upona successful request, but a [QueryPurchasesError](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/model/QueryPurchasesResponse.kt) if the request fails.

    reactiveBilling.queryPurchaseHistory()
            .subscribe({
                    // Handle purchase history
            }, {
                    // Handle failure
            })
            
## Querying subscriptions

You can query subscriptions for the current user by using the [querySubscriptionHistory()](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/ReactivePlayBilling.kt#L104) observable. This will return you a list of Purchase instances upona successful request, but a [QuerySubscriptionsError](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/model/QuerySubscriptionsError.kt) if the request fails.

    reactiveBilling.querySubscriptionHistory()
            .subscribe({
                    // Handle purchase history
            }, {
                    // Handle failure
            })
            
 ## Consuming a purchase

You can consume an item that has been purchases by using [consumeItem()](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/ReactivePlayBilling.kt#L118). When calling this you need to pass in a reference to the purchase token for the desired item. A successful call will return a [ConsumptionResponse](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/model/ConsumptionResponse.kt) instance and upon a failure a [ConsumptionError](https://github.com/bufferapp/ReactivePlayBilling/blob/master/lib/src/main/java/org/buffer/android/reactiveplaybilling/model/ConsumptionError.kt) will be returned.

    reactiveBilling.consumePurchase()
            .subscribe({
                    // Handle consumption success
            }, {
                    // Handle consumption failure
            })

 # Installing
 
 Until we get this onto maven central (we will do once the library has had some more usage for a v1 release) you will need to use Jitpack. This can be done like so:
 
    allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
        }
    }
    dependencies {
        compile 'com.github.bufferapp:ReactivePlayBilling:-SNAPSHOT'
    }
