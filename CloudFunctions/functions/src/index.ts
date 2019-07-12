import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
admin.initializeApp();
// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript

export const getMerchantId = functions.database
.ref('Orders/{OrdersId}/currentRequestInfo/{currentRequestInfoId}')
.onCreate((snapshot, contex) =>  {
    const RequestId = contex.params.currentRequestInfoId
    const OrdersId = contex.params.OrdersId
    let currentMerchantId
    return admin.database().ref(`Orders/${OrdersId}`).child('merchantName')
    .once('value').then(result=>{
        console.log('result: '+result.val);
        currentMerchantId  = result.val()
        console.log(`New request with id ${RequestId} to merchant ${currentMerchantId}`)
        }
        )
        .catch(error=>{
            console.log('something goes wrong');
            
          });
    //const currentRequestInfoData = snapshot.val()
    // const notificationRef = admin.database().ref('Users/UsersId/Notifications/NotificaionsId')
    // notificationRef
    

})
