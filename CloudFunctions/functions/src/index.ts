import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp();
// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript

export const sendNotificationToMerchant = functions.database
.ref('Users/{UsersId}/Notifications/{NotificationsId}')
.onCreate(async (snapshot, contex) =>  {
    const merchantId = contex.params.UsersId
    const notificationsId = contex.params.NotificationsId
    
    console.log(`merchantId is ${merchantId} notificationId is ${notificationsId}`);

    const notificationData = snapshot.val();

      const orderName = notificationData.orderName;
      const DelegateURL = notificationData.requestInfo.userImageURL;

      const delegateName = notificationData.requestInfo.userName;
      const delegateOffer = notificationData.requestInfo.offerPrice;

      console.log(`order name is ${orderName} merchantURl is ${DelegateURL}`);
      

      const getDeviceTokensPromise = admin.database().ref(`Users/${merchantId}/userToken`).once('value');

      // The snapshot to the user's tokens.

      // The array containing all the user's tokens.

      const results = await Promise.all([getDeviceTokensPromise]);
      const tokensSnapshot = results[0];

      if (!tokensSnapshot.hasChildren()) {
         console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');

      //Notification details.
      const payload = {
        notification: {
          title: `New delivery request from ${delegateName}`,
          body: `${delegateName} want to deliver ${orderName} for ${delegateOffer} `,
          sound: "default",
          icon: DelegateURL,
          priority : "high",
          click_action : ".UserInfo.Notifications"
        }
      }; 



      console.log(`token is ${tokensSnapshot.val()} payload is ${payload}`)

      // Send notifications to all tokens.
      const response = await admin.messaging().sendToDevice(tokensSnapshot.val(), payload);
      // For each message check if there was an error.
      const tokensToRemove:any[] = [];
      response.results.forEach(async (result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokensSnapshot.val()[index], error);
          // Cleanup the tokens who are not registered anymore.
          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
                await admin.database().ref(`Users/${merchantId}/userToken`).remove()
            tokensToRemove.push(tokensSnapshot.ref.child(tokensSnapshot.val()).remove());
          }
        }
      });
       return Promise.all(tokensToRemove);
    });

    //   // Send notifications to all tokens.
    //   return await admin.messaging().sendToDevice(tokens, payload, options).catch(response =>{
    //     console.log('response'+ response);
    // });

    //     const delegateName = snapshot.ref.child('userName');
    //     const delegateID = snapshot.ref.child('userID');
    //     const offerPrice = snapshot.ref.child('offerPrice');


    //     console.log(`delegate name is ${delegateName} delegate id is ${delegateID}`)
    
      
    //      admin.database().ref(`Orders/${OrdersId}`).child('merchantId').once('value').then(result=>{
    //         merchantId = result.val();
    //             }
    //             )
    //             .catch(error=>{
    //                 console.log('something goes wrong');
                    
    //               });;

    //   const getCurrentMerchant = admin.database()
    //   .ref(`Users/${merchantId}`).once('value');

    //   const getCurrentOrder = admin.database()
    //   .ref(`Orders/${OrdersId}`).once('value');

      
    //   let currentMerchant;

    //   let currentOrder;

    //   let orderName, orderURL, merchantToken;

      
 
    //   const results = await Promise.all([getCurrentMerchant, getCurrentOrder]);
       
    //   currentMerchant = results[0];
    //   currentOrder = results[1];
      
    //   orderName = currentOrder.val().packageName;
    //   orderURL = currentOrder.val().packageImageURL;
    //   merchantToken = currentMerchant.child('userToken').val();

    //   console.log(`order name ${orderName} its image 
    //   ${orderURL} delegate name ${delegateName} offer price ${offerPrice}`);
    //   console.log(`current merchant ${currentMerchant} token is ${merchantToken}`);

    //      // Notification details.
    //   const payload = {
    //     notification: {
    //       title: `New delivery request from ${delegateName}`,
    //       body: `${delegateName} want to deliver ${orderName} for ${offerPrice} `,
    //       icon: orderURL
    //     }
    //   };

    //   let options = { priority: "high" };
    
  
    // // Send notifications to all tokens.
    // return await admin.messaging().sendToDevice(merchantToken, payload, options).catch(response =>{
    //     console.log('response'+ response);
    // });
    


    // return admin.database().ref(`Orders/${OrdersId}`).child('merchantName')
    // .once('value').then(result=>{
    //     console.log('result: '+result.val);
    //     currentMerchantId  = result.val()
    //     currentMerchantId.
    //     console.log(`New request with id ${RequestId} to merchant ${currentMerchantId}`)
    //     }
    //     )
    //     .catch(error=>{
    //         console.log('something goes wrong');
            
    //       });
    //const currentRequestInfoData = snapshot.val()
    // const notificationRef = admin.database().ref('Users/UsersId/Notifications/NotificaionsId')
    // notificationRef
    


