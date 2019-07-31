import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp();
// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript

export const sendNotificationToUser = functions.database
.ref('Users/{UsersId}/Notifications/{NotificationsId}')
.onCreate(async (snapshot, contex) =>  {
     var payload: any
    const receiverId = contex.params.UsersId
    const notificationsId = contex.params.NotificationsId
    
    console.log(`userId is ${receiverId} notificationId is ${notificationsId}`);

      const notificationData = snapshot.val();

      const orderName = notificationData.orderName;
      const senderURL = notificationData.requestInfo.userImageURL;

      const senderName = notificationData.requestInfo.userName;
      const senderOffer = notificationData.requestInfo.offerPrice;

      const notificationType = notificationData.type
      const notificationPurpose = notificationData.purpose

      console.log(`order name is ${orderName} merchantURl is ${senderURL}`);
      

      const getDeviceTokensPromise = admin.database().ref(`Users/${receiverId}/userToken`).once('value');

      // The snapshot to the user's tokens.

      // The array containing all the user's tokens.

      const results = await Promise.all([getDeviceTokensPromise]);
      const tokensSnapshot = results[0];

      if (!tokensSnapshot.hasChildren()) {
         console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');

       

      if (notificationType === "toMerchant"){

        if (notificationPurpose === "acceptance"){

          //the Delegate accepted merchant's offer to deliver this package on that trip
           payload = {
              notification: {
                title: `delivery acceptance for ${orderName}`,
                body: `${senderName} accept your offer on ${orderName} for ${senderOffer} EGP `,
                sound: "default",
                icon: senderURL,
                priority : "high",
                click_action : ".UserInfo.Notifications"
        }
      }; 

        }else if (notificationPurpose === "request") {
            //new request on merchant's order.
           payload = {
              notification: {
               title: `New delivery request from ${senderName}`,
                body: `${senderName} want to deliver ${orderName} for ${senderOffer} EGP`,
                sound: "default",
                icon: senderURL,
                priority : "high",
                click_action : ".UserInfo.Notifications"
        }
      };
        }
        
      }else if (notificationType === "toDelegate"){

        if (notificationPurpose === "acceptance"){

          //merchant accept delegate's request to deliver his order.
           payload = {
              notification: {
                title: `delivery acceptance for ${orderName}`,
                body: `${senderName} accept your offer on ${orderName} for ${senderOffer} EGP `,
                sound: "default",
                icon: senderURL,
                priority : "high",
                click_action : ".UserInfo.Notifications"
        }
      }; 

        }else if (notificationPurpose === "request") {

          //new request on delegate's trip.
           payload = {
            notification: {
              title: `New delivery request from ${senderName}`,
              body: `${senderName} want to deliver ${orderName} for ${senderOffer} EGP in ${notificationData.tripDate}`,
              sound: "default",
              icon: senderURL,
              priority : "high",
              click_action : ".UserInfo.Notifications"
        }
      };

        }

      }

       



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
                await admin.database().ref(`Users/${receiverId}/userToken`).remove()
            tokensToRemove.push(tokensSnapshot.ref.child(tokensSnapshot.val()).remove());
          }
        }
      });
       return Promise.all(tokensToRemove);
    });

////////////////////////////////////////////////////////////////////////////////  


//     export const sendAcceptanceToDelegate = functions.database
// .ref('Users/{UsersId}/Notifications/{NotificationsId}')
// .onUpdate(async (change, contex) =>  {
//     const before = change.before.val()
//     const after = change.after.val()
//     if(before.requestInfo.status === after.requestInfo.status){
//       console.log("value did not change")
//       return null
//     }
//     const merchantId = contex.params.UsersId
//     const notificationsId = contex.params.NotificationsId
  
    
//     console.log(`merchantId is ${merchantId} notificationId is ${notificationsId}`);

//     const notificationData = after;
//     const ordersId = notificationData.orderId

//     let merchantName
//      await admin.database().ref(`Users/${merchantId}`).child('name').once('value').then(result=>{
//       merchantName = result.val();
//                   }
//                   )
//                   .catch(error=>{
//                       console.log('something goes wrong');
                      
//                     });

//       const orderName = notificationData.orderName;
//       const delegateURL = notificationData.requestInfo.userImageURL;

//       //const delegateName = notificationData.requestInfo.userName;
//       const delegateOffer = notificationData.requestInfo.offerPrice;

//       console.log(`order name is ${orderName} delegateURL is ${delegateURL}`);
      

//       const getDeviceTokensPromise = admin.database()
//       .ref(`Users/${notificationData.requestInfo.userID}/userToken`).once('value');

//       // The snapshot to the user's tokens.

//       // The array containing all the user's tokens.

//       const results = await Promise.all([getDeviceTokensPromise]);
//       const tokensSnapshot = results[0];

//       if (!tokensSnapshot.hasChildren()) {
//          console.log('There are no notification tokens to send to.');
//       }
//       console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');

//       //Notification details.
//       const payload = {
//         notification: {
//           title: `delivery acceptance for ${orderName}`,
//           body: `${merchantName} accept your offer on ${orderName} for ${delegateOffer} EGP `,
//           sound: "default",
//           icon: delegateURL,
//           priority : "high"
//           //click_action : ".UserInfo.Notifications"
//         }
//         , data: {
//           "orderId" : ordersId  
//         }
//       }; 



//       console.log(`token is ${tokensSnapshot.val()} payload is ${payload}`)

//       // Send notifications to all tokens.
//       const response = await admin.messaging().sendToDevice(tokensSnapshot.val(), payload);
//       // For each message check if there was an error.
//       const tokensToRemove:any[] = [];
//       response.results.forEach(async (result, index) => {
//         const error = result.error;
//         if (error) {
//           console.error('Failure sending notification to', tokensSnapshot.val()[index], error);
//           // Cleanup the tokens who are not registered anymore.
//           if (error.code === 'messaging/invalid-registration-token' ||
//               error.code === 'messaging/registration-token-not-registered') {
//                 await admin.database().ref(`Users/${merchantId}/userToken`).remove()
//             tokensToRemove.push(tokensSnapshot.ref.child(tokensSnapshot.val()).remove());
//           }
//         }
//       });
//        return Promise.all(tokensToRemove);
//     });

    


