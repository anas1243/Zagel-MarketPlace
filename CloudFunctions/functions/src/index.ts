import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
admin.initializeApp();
// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript

export const getMerchantId = functions.database
.ref('Orders/{OrdersId}/merchantId')
.onCreate((snapshot, contex) =>  {
    const OrdersId = contex.params.OrdersId
    console.log(`New Order with merchant id ${OrdersId}`)
    return snapshot.ref.toString()


})
