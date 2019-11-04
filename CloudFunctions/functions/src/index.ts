import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp();
// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript

export const sendNotificationToUser = functions.database
.ref('Users/{UsersId}/Notifications/{NotificationsId}')
.onCreate(async (snapshot, contex) =>  {
     let payload: any
     let condition: any
    const receiverId = contex.params.UsersId
    const notificationsId = contex.params.NotificationsId
    
    console.log(`userId is ${receiverId} notificationId is ${notificationsId}`);

      const notificationData = snapshot.val();

      const orderName = notificationData.orderName;
      const orderId = notificationData.orderId;
      const senderURL = notificationData.courierInfo.userImageURL;

      const senderName = notificationData.courierInfo.userName;
      //const senderOffer = notificationData.courierInfo.offerPrice;
      const senderGroup = notificationData.courierInfo.userGroup;

      const whichBranch = notificationData.whichBranch;

      const notificationType = notificationData.type
      const notificationPurpose = notificationData.purpose

      console.log(`order name is ${orderName} merchantURl is ${senderURL}`);


      if (notificationPurpose === "OnWay" && notificationType === "ToMerchant"){
        payload = {
          notification: {
            title: `${orderName} طلب توصيل`,
            body: `${senderName} send a request to deliver ${orderName}`,
            sound: "default",
            icon: senderURL,
            priority : "high",
            click_action : ".OrdersPackage.OrderDetails"
          }
          , data: {
            "orderId" : orderId,
            "WhichActivity" : "cloudFunctions",
            "WhichBranch" : whichBranch
          }
        }; 
      }else if (notificationPurpose === "Picked" && notificationType === "ToMerchant"){
        payload = {
          notification: {
            title: `${orderName} استيلام الشحنة`,
            body: `${senderName} قام باستيلام الشحنة ${orderName} من التاجر`,
            sound: "default",
            icon: senderURL,
            priority : "high",
            click_action : ".OrdersPackage.OrderDetails"
          }
          , data: {
            "orderId" : orderId,
            "WhichActivity" : "cloudFunctions",
            "WhichBranch" : whichBranch
          }
        }; 
      }else if (notificationPurpose === "Delivered" && notificationType === "ToMerchant"){
        payload = {
          notification: {
            title: `${orderName} تسليم الشحنة`,
            body: `${senderName} قام بتسليم الشحنة ${orderName} للعميل النهائى`,
            sound: "default",
            icon: senderURL,
            priority : "high",
            click_action : ".OrdersPackage.OrderDetails"
          }
          , data: {
            "orderId" : orderId,
            "WhichActivity" : "cloudFunctions",
            "WhichBranch" : whichBranch
          }
        }; 
      }

      

       switch(senderGroup){
          case "AlexStaticBirds":
          case "AlexFreeBirds":
              condition = "'AlexPM' in topics"
              await admin.messaging().sendToCondition(condition, payload);
           break;
          case "CairoStaticBirds":
          case "CairoFreeBirds":
            condition = "'CairoPM' in topics"
            await admin.messaging().sendToCondition(condition, payload);
           break;
       }
      
       

      const getDeviceTokensPromise = admin.database().ref(`Users/${receiverId}/userToken`).once('value');

      // The snapshot to the user's tokens.

      // The array containing all the user's tokens.

      const results = await Promise.all([getDeviceTokensPromise]);
      const tokensSnapshot = results[0];

      if (!tokensSnapshot.hasChildren()) {
         console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');

       

      if (notificationType === "ToMerchant"){

        if (notificationPurpose === "OnWay"){

          //the Delegate is on his way to the merchant
           payload = {
              notification: {
                title: `  ${orderName} طلب توصيل`,
                body: `المندوب ${senderName} في الطريق اليك الان لاستلام ${orderName}`,
                sound: "default",
                icon: senderURL,
                priority : "high",
                click_action : ".OrdersPackage.OrderDetails"
              }
              , data: {
                "orderId" : orderId,
                "WhichActivity" : "cloudFunctions",
                "WhichBranch" : whichBranch
              }
            }; 

        }else if (notificationPurpose === "Picked") {
            //new request on merchant's order.
           payload = {
              notification: {
               title: `تأكيد استلام شحنتك ${orderName}`,
                body: `لقد تم استلام شحنتك ${orderName} من فضلك اضغط علي تم اللستيلام من التفاصيل`,
                sound: "default",
                icon: senderURL,
                priority : "high",
                click_action : ".OrdersPackage.OrderDetails"
        }
        , data: {
          "orderId" : orderId,
          "WhichActivity" : "cloudFunctions",
          "WhichBranch" : whichBranch
        }
      }; 
        }else if (notificationPurpose === "Delivered") {
          //new request on merchant's order.
         payload = {
          notification: {
            title: `تأكيد تسليم شحنتك ${orderName}`,
             body: `لقد تم تسليم شحنتك ${orderName} من فضلك اضغط علي تم الاستلام من التفاصيل`,
             sound: "default",
             icon: senderURL,
             priority : "high",
             click_action : ".OrdersPackage.OrderDetails"
     }
        , data: {
          "orderId" : orderId,
          "WhichActivity" : "cloudFunctions",
          "WhichBranch" : whichBranch
        }
      }; 
      }
        
      }else if (notificationType === "ToDelegate"){
        if (notificationPurpose === "Picked") {
            //new request on merchant's order.
           payload = {
              notification: {
               title: `تأكيد استلام الشحنة ${orderName}`,
                body: `لقد تم تأكيد استيلام الشحنة ${orderName} من قبل التاجر. من فضلك اسرع في توصيلها`,
                sound: "default",
                icon: senderURL,
                priority : "high",
                click_action : ".OrdersPackage.OrderDetails"
        }
        , data: {
          "orderId" : orderId,
          "WhichActivity" : "cloudFunctions",
          "WhichBranch" : whichBranch
        }
      }; 
        }else if (notificationPurpose === "Delivered") {
          //new request on merchant's order.
         payload = {
          notification: {
            title: `تأكيد تسليم شحنتك ${orderName}`,
            body: `لقد تم تأكيد تسليم الشحنة ${orderName} من قبل التاجر. نشكرك علي حسن الاداء`,
            sound: "default",
             icon: senderURL,
             priority : "high",
             click_action : ".OrdersPackage.OrderDetails"
     }
        , data: {
          "orderId" : orderId,
          "WhichActivity" : "cloudFunctions",
          "WhichBranch" : whichBranch
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


//     
    //------------------------------------------------------------------------------------------------------


    export const sendToBirdsInAlex = functions.database
.ref('AlexOrders/{AlexOrdersId}')
.onCreate(async (snapshot, contex) =>  {
     
    const orderId = contex.params.AlexOrdersId
    
    console.log(`orderId is ${orderId}`);

      const orderData = snapshot.val();

      const orderName = orderData.packageName;
      //const senderId = orderData.merchantId;
      const senderName = orderData.merchantName;
      const orderURL = orderData.packageImageURL;
      const whichBranch = orderData.whichBranch;
      //const orderSource = orderData.locationInfoForPackage.sAdminArea;
      const orderSubAdminSource = orderData.locationInfoForPackage.sSubAdmin;
      //const orderDestination = orderData.locationInfoForPackage.dAdminArea;
      const orderSubAdminDestination = orderData.locationInfoForPackage.dSubAdmin;

      let payload: any
      let condition: any

      

                  payload = {
                    notification: {
                      title: `${senderName} has a new order hurryup `,
                      body: `${senderName} want to deliver ${orderName} from ${orderSubAdminSource} to ${orderSubAdminDestination} `,
                      sound: "default",
                      icon: orderURL,
                      priority : "high",
                      click_action : ".OrdersPackage.OrderDetails"
              }, data: {
                "orderId" : orderId,
                "WhichActivity" : "cloudFunctions",
                "WhichBranch" : whichBranch
              }
            }; 
              condition = "'AlexFreeBirds' in topics || 'AlexPM' in topics"

                    // Send notifications to all birds in alex plus alex PMs.
            
      return await admin.messaging().sendToCondition(condition, payload);
              
        
         
});

export const sendToBirdsInCairo = functions.database
.ref('CairoOrders/{CairoOrdersId}')
.onCreate(async (snapshot, contex) =>  {
     
    const orderId = contex.params.CairoOrdersId
    
    console.log(`orderId is ${orderId}`);

      const orderData = snapshot.val();

      const orderName = orderData.packageName;
      //const senderId = orderData.merchantId;
      const senderName = orderData.merchantName;
      const orderURL = orderData.packageImageURL;
      const whichBranch = orderData.whichBranch;
      //const orderSource = orderData.locationInfoForPackage.sAdminArea;
      const orderSubAdminSource = orderData.locationInfoForPackage.sSubAdmin;
      //const orderDestination = orderData.locationInfoForPackage.dAdminArea;
      const orderSubAdminDestination = orderData.locationInfoForPackage.dSubAdmin;

      let payload: any
      let condition: any

      

                  payload = {
                    notification: {
                      title: `${senderName} has a new order hurryup `,
                      body: `${senderName} want to deliver ${orderName} from ${orderSubAdminSource} to ${orderSubAdminDestination} `,
                      sound: "default",
                      icon: orderURL,
                      priority : "high",
                      click_action : ".OrdersPackage.OrderDetails"
              }, data: {
                "orderId" : orderId,
                "WhichActivity" : "cloudFunctions",
                "WhichBranch" : whichBranch
              }
            }; 
              condition = "'CairoFreeBirds' in topics || 'CairoPM' in topics"

                    // Send notifications to all birds in cairo plus cairo PMs.
      
      return await admin.messaging().sendToCondition(condition, payload);
              
        
         
});

export const sendToBirdsFromAlexToCairo = functions.database
.ref('AlexToCairoOrders/{AlexToCairoOrdersId}')
.onCreate(async (snapshot, contex) =>  {
     
    const orderId = contex.params.AlexToCairoOrdersId
    
    console.log(`orderId is ${orderId}`);

      const orderData = snapshot.val();

      const orderName = orderData.packageName;
      //const senderId = orderData.merchantId;
      const senderName = orderData.merchantName;
      const orderURL = orderData.packageImageURL;
      const whichBranch = orderData.whichBranch;
      //const orderSource = orderData.locationInfoForPackage.sAdminArea;
      //const orderSubAdminSource = orderData.locationInfoForPackage.sAdminArea;
      //const orderDestination = orderData.locationInfoForPackage.dAdminArea;
      //const orderSubAdminDestination = orderData.locationInfoForPackage.sAdminArea;

      let payload: any
      let condition: any

                payload = {
                  notification: {
                    title: `${senderName} has a new order hurryup `,
                    body: `please bring ${orderName} to the headquartes as soon as possible to send it to cairo`,
                    sound: "default",
                    icon: orderURL,
                    priority : "high",
                    click_action : ".OrdersPackage.OrderDetails"
            }, data: {
              "orderId" : orderId,
              "WhichActivity" : "cloudFunctions",
              "WhichBranch" : whichBranch
            }
          }; 
            condition = "'AlexStaticBirds' in topics || 'AlexPM' in topics"

                  // Send notifications to all birds in alex plus alex PMs.
      
      return await admin.messaging().sendToCondition(condition, payload);
              
        
         
});

export const sendToBirdsFromCairoToAlex = functions.database
.ref('CairoToAlexOrders/{CairoToAlexOrdersId}')
.onCreate(async (snapshot, contex) =>  {
     
    const orderId = contex.params.CairoToAlexOrdersId
    
    console.log(`orderId is ${orderId}`);

      const orderData = snapshot.val();

      const orderName = orderData.packageName;
      //const senderId = orderData.merchantId;
      const senderName = orderData.merchantName;
      const orderURL = orderData.packageImageURL;
      const whichBranch = orderData.whichBranch;
      //const orderSource = orderData.locationInfoForPackage.sAdminArea;
      //const orderSubAdminSource = orderData.locationInfoForPackage.sAdminArea;
      //const orderDestination = orderData.locationInfoForPackage.dAdminArea;
      //const orderSubAdminDestination = orderData.locationInfoForPackage.sAdminArea;

      let payload: any
      let condition: any

     
                payload = {
                  notification: {
                    title: `${senderName} has a new order hurryup `,
                    body: `please bring ${orderName} to the headquartes as soon as possible to send it to Alex`,
                    sound: "default",
                    icon: orderURL,
                    priority : "high",
                    click_action : ".OrdersPackage.OrderDetails"
            }, data: {
              "orderId" : orderId,
              "WhichActivity" : "cloudFunctions",
              "WhichBranch" : whichBranch
            }
          }; 
            condition = "'CairoStaticBirds' in topics || 'CairoPM' in topics"

                  // Send notifications to all birds in cairo plus cairo PMs.
      
      return await admin.messaging().sendToCondition(condition, payload);
              
        
         
});


    
// export const sendSatusAcknowledge = functions.database
// .ref('Orders/{OrdersId}')
// .onUpdate(async (change, contex) =>  {
//       const before = change.before.val()
//       const after = change.after.val()
//       if(before.delegatePicked === after.delegatePicked && before.merchantDelivered === after.merchantDelivered
//         && before.merchantPicked === after.merchantPicked && before.delegateDelivered === after.delegateDelivered){
//         console.log("value did not change")
//         return null
//     }
//       const orderId = contex.params.OrdersId
  
    
//       console.log(`order id is ${orderId}`);

//       const orderData = after;

//       const merchantName = orderData.merchantName;
//       const delegateName = orderData.acceptedDelegateName;

//       const merchantId = orderData.merchantId;
//       const delegateId = orderData.acceptedDelegateID;

//       const orderName = orderData.pAlexPMackageName;

//       const orderURL = orderData.packageImageURL;

//       console.log(`order name is ${orderName} orderURL is ${orderURL}`);

//       let getDeviceTokensPromise: any;
//       let payload: any;

//       if (before.delegatePicked !== after.delegatePicked || before.delegateDelivered !== after.delegateDelivered){
//             getDeviceTokensPromise = admin.database()
//       .ref(`Users/${merchantId}/userToken`).once('value');

//       }else if (before.merchantDelivered !== after.merchantDelivered || before.merchantPicked !== after.merchantPicked){
//         getDeviceTokensPromise = admin.database()
//       .ref(`Users/${delegateId}/userToken`).once('value');

//       }
 

//       // The snapshot to the user's tokens.

//       // The array containing all the user's tokens.

//       const results = await Promise.all([getDeviceTokensPromise]);
//       const tokensSnapshot = results[0];

//       if (!tokensSnapshot.hasChildren()) {
//          console.log('There are no notification tokens to send to.');
//       }
//       console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
      
//       if (before.delegatePicked !== after.delegatePicked){
//         //Notification details.
//        payload = {
//         notification: {
//           title: `${orderName} Picking acknowledge`,
//           body: `${delegateName} marked your order ${orderName} as Picked `,
//           sound: "default",
//           icon: orderURL,
//           priority : "high",
//           click_action : ".OrdersPackage.OrderDetails"
//         }
//         , data: {
//           "orderId" : orderId,
//           "WhichActivity" : "cloudFunctions"  
//         }
//       }; 

//   }else if (before.merchantDelivered !== after.merchantDelivered){
//     //Notification details.
//     payload = {
//       notification: {
//         title: `${orderName} Delivering acknowledge`,
//         body: `${merchantName} marked order named ${orderName} as Delivered `,
//         sound: "default",
//         icon: orderURL,
//         priority : "high",
//         click_action : ".OrdersPackage.OrderDetails"
//       }
//       , data: {
//         "orderId" : orderId,
//         "WhichActivity" : "cloudFunctions"
//       }
//     }; 

//   } else if (before.merchantPicked !== after.merchantPicked){
//     //Notification details.
//    payload = {
//     notification: {
//       title: `${orderName} Picking acknowledge`,
//       body: `${merchantName} marked order named ${orderName} as Picked `,
//       sound: "default",
//       icon: orderURL,
//       priority : "high",
//       click_action : ".OrdersPackage.OrderDetails"
//     }
//     , data: {
//       "orderId" : orderId,
//       "WhichActivity" : "cloudFunctions"
//     }
//   }; 

// }else if (before.delegateDelivered !== after.delegateDelivered){
// //Notification details.
// payload = {
//   notification: {
//     title: `${orderName} Delivering acknowledge`,
//     body: `${delegateName} marked your order ${orderName} as Delivered `,
//     sound: "default",
//     icon: orderURL,
//     priority : "high",
//     click_action : ".OrdersPackage.OrderDetails"
//   }
//   , data: {
//     "orderId" : orderId,
//     "WhichActivity" : "cloudFunctions" 
//   }
// }; 

// }


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



