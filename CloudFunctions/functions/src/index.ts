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


    export const sendSatusAcknowledge = functions.database
.ref('Orders/{OrdersId}')
.onUpdate(async (change, contex) =>  {
      const before = change.before.val()
      const after = change.after.val()
      if(before.delegatePicked === after.delegatePicked && before.merchantDelivered === after.merchantDelivered
        && before.merchantPicked === after.merchantPicked && before.delegateDelivered === after.delegateDelivered){
        console.log("value did not change")
        return null
    }
      const orderId = contex.params.OrdersId
  
    
      console.log(`order id is ${orderId}`);

      const orderData = after;

      const merchantName = orderData.merchantName;
      const delegateName = orderData.acceptedDelegateName;

      const merchantId = orderData.merchantId;
      const delegateId = orderData.acceptedDelegateID;

      const orderName = orderData.packageName;

      const orderURL = orderData.packageImageURL;

      console.log(`order name is ${orderName} orderURL is ${orderURL}`);

      let getDeviceTokensPromise: any;
      let payload: any;

      if (before.delegatePicked !== after.delegatePicked || before.delegateDelivered !== after.delegateDelivered){
            getDeviceTokensPromise = admin.database()
      .ref(`Users/${merchantId}/userToken`).once('value');

      }else if (before.merchantDelivered !== after.merchantDelivered || before.merchantPicked !== after.merchantPicked){
        getDeviceTokensPromise = admin.database()
      .ref(`Users/${delegateId}/userToken`).once('value');

      }
 

      // The snapshot to the user's tokens.

      // The array containing all the user's tokens.

      const results = await Promise.all([getDeviceTokensPromise]);
      const tokensSnapshot = results[0];

      if (!tokensSnapshot.hasChildren()) {
         console.log('There are no notification tokens to send to.');
      }
      console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
      
      if (before.delegatePicked !== after.delegatePicked){
        //Notification details.
       payload = {
        notification: {
          title: `${orderName} Picking acknowledge`,
          body: `${delegateName} marked your order ${orderName} as Picked `,
          sound: "default",
          icon: orderURL,
          priority : "high",
          click_action : ".OrdersPackage.OrderDetails"
        }
        , data: {
          "orderId" : orderId,
          "WhichActivity" : "cloudFunctions"  
        }
      }; 

  }else if (before.merchantDelivered !== after.merchantDelivered){
    //Notification details.
    payload = {
      notification: {
        title: `${orderName} Delivering acknowledge`,
        body: `${merchantName} marked order named ${orderName} as Delivered `,
        sound: "default",
        icon: orderURL,
        priority : "high",
        click_action : ".OrdersPackage.OrderDetails"
      }
      , data: {
        "orderId" : orderId,
        "WhichActivity" : "cloudFunctions"
      }
    }; 

  } else if (before.merchantPicked !== after.merchantPicked){
    //Notification details.
   payload = {
    notification: {
      title: `${orderName} Picking acknowledge`,
      body: `${merchantName} marked order named ${orderName} as Picked `,
      sound: "default",
      icon: orderURL,
      priority : "high",
      click_action : ".OrdersPackage.OrderDetails"
    }
    , data: {
      "orderId" : orderId,
      "WhichActivity" : "cloudFunctions"
    }
  }; 

}else if (before.delegateDelivered !== after.delegateDelivered){
//Notification details.
payload = {
  notification: {
    title: `${orderName} Delivering acknowledge`,
    body: `${delegateName} marked your order ${orderName} as Delivered `,
    sound: "default",
    icon: orderURL,
    priority : "high",
    click_action : ".OrdersPackage.OrderDetails"
  }
  , data: {
    "orderId" : orderId,
    "WhichActivity" : "cloudFunctions" 
  }
}; 

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
                await admin.database().ref(`Users/${merchantId}/userToken`).remove()
            tokensToRemove.push(tokensSnapshot.ref.child(tokensSnapshot.val()).remove());
          }
        }
      });
       return Promise.all(tokensToRemove);
    });


    //------------------------------------------------------------------------------------------------------


    export const sendToBirdsInAlex = functions.database
.ref('AlexOrders/{OrdersId}')
.onCreate(async (snapshot, contex) =>  {
     
    const orderId = contex.params.OrdersId
    
    console.log(`orderId is ${orderId}`);

      const orderData = snapshot.val();

      const orderName = orderData.packageName;
      //const senderId = orderData.merchantId;
      const senderName = orderData.merchantName;
      const orderURL = orderData.packageImageURL;
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
                "WhichActivity" : "cloudFunctions" 
              }}
              condition = "'AlexFreeBirds' in topics || 'AlexPM' in topics"

                    // Send notifications to all birds in alex plus alex PMs.
            
      return await admin.messaging().sendToCondition(condition, payload);
              
        
         
});

export const sendToBirdsInCairo = functions.database
.ref('CairoOrders/{OrdersId}')
.onCreate(async (snapshot, contex) =>  {
     
    const orderId = contex.params.OrdersId
    
    console.log(`orderId is ${orderId}`);

      const orderData = snapshot.val();

      const orderName = orderData.packageName;
      //const senderId = orderData.merchantId;
      const senderName = orderData.merchantName;
      const orderURL = orderData.packageImageURL;
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
                "WhichActivity" : "cloudFunctions" 
              }}
              condition = "'CairoFreeBirds' in topics || 'CairoPM' in topics"

                    // Send notifications to all birds in cairo plus cairo PMs.
      
      return await admin.messaging().sendToCondition(condition, payload);
              
        
         
});

export const sendToBirdsFromAlexToCairo = functions.database
.ref('AlexToCairoOrders/{OrdersId}')
.onCreate(async (snapshot, contex) =>  {
     
    const orderId = contex.params.OrdersId
    
    console.log(`orderId is ${orderId}`);

      const orderData = snapshot.val();

      const orderName = orderData.packageName;
      //const senderId = orderData.merchantId;
      const senderName = orderData.merchantName;
      const orderURL = orderData.packageImageURL;
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
              "WhichActivity" : "cloudFunctions" 
            }}
            condition = "'AlexStaticBirds' in topics || 'AlexPM' in topics"

                  // Send notifications to all birds in alex plus alex PMs.
      
      return await admin.messaging().sendToCondition(condition, payload);
              
        
         
});

export const sendToBirdsFromCairoToAlex = functions.database
.ref('CairoToAlexOrders/{OrdersId}')
.onCreate(async (snapshot, contex) =>  {
     
    const orderId = contex.params.OrdersId
    
    console.log(`orderId is ${orderId}`);

      const orderData = snapshot.val();

      const orderName = orderData.packageName;
      //const senderId = orderData.merchantId;
      const senderName = orderData.merchantName;
      const orderURL = orderData.packageImageURL;
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
              "WhichActivity" : "cloudFunctions" 
            }}
            condition = "'CairoStaticBirds' in topics || 'CairoPM' in topics"

                  // Send notifications to all birds in cairo plus cairo PMs.
      
      return await admin.messaging().sendToCondition(condition, payload);
              
        
         
});


    


