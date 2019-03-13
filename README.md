# OtpSecure Android SDK

This Android library allows you to quickly and easily validate otps sent through OtpSecure Service using Java in Android.

## Setup

### Prerequisites

What things you need to install the software:

 - JDK 1.8
 - Android SDK
 - Gradle
 - Maven
 
### Installing 
This library is published in Maven Central, so just add the dependency into your project in function
of the dependency management tool used.

#### Gradle

` implementation 'com.ecertic.otpsecure:otpsecure-android:1.0.0'`

Note: We recommend that you don't use compile 'com.ecertic.otpsecure:otpsecure-android:+, as future 
versions of the SDK may not maintain full backwards compatibility. When such a change occurs, 
a major version number change will accompany it.

#### Maven

```
<dependency>
    <groupId>com.ecertic.otpsecure</groupId>
    <artifactId>otpsecure-android</artifactId>
    <version>1.0.0</version>
</dependency>
```


### Building

A step by step series of examples that tell you have to get a development env running.

Download this repository and add the 
[Android Library Module](https://github.com/ecertic/otpsecure-android/tree/master/library) 
to your project.

```
gradle clean build
```

### Running the tests

Explain how to run the automated tests for this system

```
gradle test
```

## Usage

In order to use the library, developers must use `OtpSecure.java` which exposes methods needed to 
retrieve operation info by token and to validate tokens against otps.

> Asynchronous methods involve to instance a callback proper class 
(for example OperationInfoCallback ) where `onSuccess()` is returned when  
`httpResponseStatusCode > = 200 && httpResponseStatusCode < 300` and `onError()` is returned when 
`httpResponseStatusCode > 300`. Please take a look into `*Callback.java` to check which classes 
handle the callback methods.

Following sections allow developers to figure how to use OtpSecure library out.
  
### Using `OtpSecure.retrieveOperationInfoByToken`

You can use retrieve operationInfo by token as shown in the following snippet. Also, sample-app 
contains examples of how to use it.


```java

 private void retrieveOperationInfoByToken() {
        if (mTheToken == null) {
            mDialogHandler.showMsg("Invalid OperationInfo");
            return;
        }
        mProgressDialogController.startProgress();
        new OtpSecure(mContext).retrieveOperationInfoByToken(
                mTheToken,
                null,
                new OperationInfoCallback() {
                    public void onSuccess(OperationInfo operationInfo) {
                        mOtpValidateViewController.setView(operationInfo.getHtml());
                        mProgressDialogController.finishProgress();
                    }

                    public void onError(Exception error) {
                        mDialogHandler.showMsg(error.getLocalizedMessage());
                        mProgressDialogController.finishProgress();
                    }
                });
    }
 
```

**Note** retrieveOperationInfoByToken method is also implemented in a synchronous way like 
`OtpSecure.retrieveOperationInfoByTokenSynchronous`

#### endpoint /token

This endpoint is responsible for retrieving operation info. Responses for this endpoint are:

 1. If (token == null || token.isEmpty()) then return **HttpResponse.400**
 2. If (token && !token.find()) then return **HttpResponse.404**
 3. If (token && !token.signed && !token.isExpired()) then return **HttpResponse.200**

    ```javascript

    {
        'html': 'html to be shown',
        'status': 'Optional checks to be accepted before validating otp',
        'uuid': 'operation id'
    }

    ```

 
 4. Else  **HttpResponse.200**

    ```javascript

    {
        'html': 'This operation has alredy ended',
        'status': 'Operation status',
        'uuid': 'operation id'
    }

    ```

### Using `OtpSecure.validateToken`

You can validate a token against otp as shown in the following snippet. Sample-app contains examples
 of how to use it.

```java

 private void validateToken() {
        if (mTheToken == null) {
            mDialogHandler.showMsg("Invalid Token");
            return;
        }
        mProgressDialogController.startProgress();
        new OtpSecure(mContext).validateToken(
                mTheToken,
                mOtpValidateViewController.getInputOtp(),
                null,
                new ValidationCallback() {
                    public void onSuccess(Validation validation) {
                        mDialogHandler.showMsg(validation.getMsg());
                        mProgressDialogController.finishProgress();
                    }

                    public void onError(Exception error) {
                        mDialogHandler.showMsg(error.getLocalizedMessage());
                        mProgressDialogController.finishProgress();
                    }
                });
    }
 
```
**Note** validateToken method is also implemented in a synchronous way as 
`OtpSecure.validateTokenSynchronous`

#### endpoint /validate

This endpoint is responsible for validating tokens against otp. Responses for this endpoint are:

 1. If (token == null || token.isEmpty() || otp == null || otp.isEmpty()) then 
 return **HttpResponse.400**
 
 2. If (token && otp && !token.find()) then return **HttpResponse.404**
   
 3. If (token && otp && token.isSigned()) then return **HttpResponse.200**

    ```javascript

    {
      'status' : 'SIGNED',
      'msg' : 'OPERATION ALREADY SIGNED'
    }

    ```
 
 4. If (token && otp & token.isExpired()) then return **HttpResponse.200**
 
    ```javascript

    {
      'status' : 'EXPIRED',
      'msg' : 'OPERATION EXPIRED'
    }

    ```

 5. If (token && otp & !otp.isValid()) then return **HttpResponse.200**

    ```javascript

    {
       'status' : 'OTP_NOK',
       'msg' : 'INVALID OTP'
    }

     ```
 
 6. Else  **HttpResponse.200**

    ```javascript

    {
       'status' : 'OTP_OK',
       'msg' : 'VALID OTP'
    }

    ```

## Sample app

This repository includes one sample app which depends on a hardcoded operationInfo in 
`MainActivity.java`

```java
public class MainActivity extends AppCompatActivity {

    private static final String OPERATION_TOKEN = "<INSERT_OPERATION_TOKEN_HERE>";

``` 

**Note** that in order to get an operation token for demo/testing purposes, please send an email to 
`rtapias@ecertic.com` and we will contact you as soon as possible. 

To build and run the example apps, clone the repository and open the project. Running "sample" will 
run the Example application

## Acknowledges

This repo is a small and lightweight custom version of a 
[stripe-android project](https://github.com/stripe/stripe-android) published in github under MIT 
license. 

## License

This project is licensed under the MIT License - see the LICENSE.md file for details
 