# BBMSL PayAPI Demo App

## Introduction
BBMSL Limited Online Payment Gateway provides an easy-to-implement payment platform for e-commerce merchants, supporting Visa, Master, Alipay, Wechat Pay, Apple Pay and Google Pay.

This demo is a mock e-commerce application to demonstrate how to implement the BBMSL PayAPI.

## System requirement
- Build SDK verison: 30
- SDK version: 26+
- Android Studio version: 4.0+
- Gradle Version: 6.1.1+

## Getting the demo app runnning
The demo app can then be set up by the following step:
1. Get a test merchant account from BBMSL. Contact your account integration manager for details.
2. Generate a key pair.
3. Upload public key in merchant portal.
4. Update <code>res/xml/merchant.xml</code>.
5. Build and run the application.

Note: private key stored in <code>merchant.xml</code> is for demonstration purpose only. In production, the private key should be stored securely in server.

## About the demo app
The demo app demonstrates 3 sample integration modes supported by the BBMSL PayAPI. Main sample logics are included in the package <code>com.bbmsl.payapidemo.ui</code>.

- Hosted Checkout - class <code>FragmentHostedCheckout</code>
- Direct API integration - class <code>FragmentDirectSale</code>
- Tokenization - class <code>FragmentTokenization</code>

The above modes simulates shopping cart page of an e-commerce app. The checkout process calls the checkout API and receives a checkout URL. The checkout URL will then be pass to FragmentWebview to show the checkout pages. 

### Sample implementation of the WebView
The class <code>FragmentWebView</code> is a sample implementation to show the BBMSL checkout web pages. It receives and load the URL passed from fragment argument.
When the transaction ended, either success, failed or cancelled, BBMSL checkout page would call the CallbackURL. The merchant should implement their own callback handling action.

### Logout user in WebView
The class <code>FragmentSettings</code> demonstrates how to clear local cookies to logout user for GooglePay and WechatPay.

### API Calls
The package <code>com.bbmsl.payapidemo.network</code> contains the methods and dataobjects for API calls using Retrofit.

## Documentation
Documentation about BBMSL Online Payment Gateway: 
https://docs.bbmsl.com/

## Credit
Retrofit: 
https://square.github.io/retrofit/
