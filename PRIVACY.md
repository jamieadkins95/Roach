If you have questions about deleting or correcting your personal data please contact us at jamieadkins95+gwent@gmail.com.

Jamie Adkins (“Developer") operates the mobile application "Roach for Gwent" ("Application"). It is our policy to respect your privacy regarding any information we may collect while operating Roach.

## Personal Data

Among the types of Personal Data that this Application collects, by itself or through third parties, there are: email address, password and User ID.
This information is used for authentication purposes only, to allow the user private access to content they have created.
The Personal Data may be freely provided by the User, or collected automatically when using this Application.

## The use of the collected Data

The Data concerning the User is collected to allow the Developer to provide its services, as well as for the following purposes: Registration and authentication, User Created Data.
The Personal Data used for each purpose is outlined in the specific sections of this document.

## Registration and Authentication

By registering or authenticating, Users allow this Application to identify them and give them access to dedicated services.
Depending on what is described below, third parties may provide registration and authentication services. In this case, this Application will be able to access some Data, stored by these third party services, for registration or identification purposes.
Google OAuth (Google Inc.)
Google OAuth is a registration and authentication service provided by Google Inc. and is connected to the Google network.
Personal Data collected: various types of Data as specified in the privacy policy of the service.
Place of processing: US
Direct registration (This Application)
The User registers by filling out the registration form and providing the Personal Data directly to this Application.
Personal Data collected: email address, password and User ID.

## User Created Data

When a user creates content using this Application, the data is stored according to the following model:
```
{
  "users": {
    "unique-user-id-non-personally-identifiable": {
      "decks": {
        "unique-deck-id": {
          "faction":"scoiatael",
          "id":"-KZ-QzT4olx_Su-kvBUK",	  
          "name":"me deck",	  
          "openToPublic":false,	  
       	  "cards": {
            "example-card-id": 1,	    
            "example-card-id2": 0
          }
        }
      }     
    }    
  }  
}
```
This data is then protected by the following rules:
```
{
  "rules": {  
    "users": {    
    	"$uid": {
      	// Only this user can read and write to their own bucket.
	      ".write": "auth != null && auth.uid == $uid",
      	".read": "auth != null && auth.uid == $uid"
    	}
    }    
  }  
}
```
User Created Data is stored without any personably indentifiable information and can only be accessed by the user that created it.

## Firebase

Roach uses various Firebase services to provide functionality to this Application. These are:

### Analytics

Firebase will collect anonymous usage statistics to help future development of this Application. This information can include, anonymous information about your device (e.g Android version), and Application usage time.

Analytics is strictly opt-in, and will only be enabled if the user enables it. It can be disabled at anytime through the Application settings.

### Authentication

As described in the Registration and Authentication section, users can register and authenticae with a Roach account. This authentication procedure is provided by Firebase.

### Database

Any user created data is stored in a Firebase Realtime Database according to the model described above.

### Crash Reporting

Roach will automatically report Application crashes to Firebase. The reports are completely anonymous. By using Roach, you are agreeing to the use of this feature.

## Additional information about Data collection and processing

### Legal action

The User's Personal Data may be used for legal purposes by the Data Controller, in Court or in the stages leading to possible legal action arising from improper use of this Application or the related services.
The User declares to be aware that the Data Controller may be required to reveal personal data upon request of public authorities.

### Additional information about User's Personal Data

In addition to the information contained in this privacy policy, this Application may provide the User with additional and contextual information concerning particular services or the collection and processing of Personal Data upon request.

### System logs and maintenance

For operation and maintenance purposes, this Application and any third party services may collect files that record interaction with this Application (System logs) or use for this purpose other Personal Data (such as IP Address).

### Information not contained in this policy

More details concerning the collection or processing of Personal Data may be requested from the Data Controller at any time. Please see the contact information at the beginning of this document.

### The rights of Users

Users have the right, at any time, to know whether their Personal Data has been stored and can consult the Data Controller to learn about their contents and origin, to verify their accuracy or to ask for them to be supplemented, cancelled, updated or corrected, or for their transformation into anonymous format or to block any data held in violation of the law, as well as to oppose their treatment for any and all legitimate reasons. Requests should be sent to the Data Controller at the contact information set out above.
This Application does not support “Do Not Track” requests.
To determine whether any of the third party services it uses honor the “Do Not Track” requests, please read their privacy policies.

### Changes to this privacy policy

The Data Controller reserves the right to make changes to this privacy policy at any time by giving notice to its Users on this page. It is strongly recommended to check this page often. If a User objects to any of the changes to the Policy, the User must cease using this Application and can request that the Data Controller remove the Personal Data. Unless stated otherwise, the then-current privacy policy applies to all Personal Data the Data Controller has about Users.
