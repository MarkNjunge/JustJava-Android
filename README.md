<p align="center">
	<img
		width="100"
		alt="Logo"
		src="/images/just_java_logo.png">
</p>

# JustJava
[![Build Status](https://app.bitrise.io/app/c373b1aa540acc1c/status.svg?token=u-KpJIBnS_0TQUtBtYNEJQ&branch=master)](https://app.bitrise.io/app/c373b1aa540acc1c)
[![Playstore](https://img.shields.io/badge/Download-Playstore-brightgreen.svg)](https://play.google.com/store/apps/details?id=com.marknkamau.justjava)
[![latest build](https://img.shields.io/badge/Download-Latest%20build-brightgreen.svg)](https://barbet.marknjunge.com/justjava)

A sample food delivery application for a coffee shop.

The backend API is written using Nest.js. See [justjava-api](https://github.com/MarkNjunge/justjava-api).

## Features

- 100% Kotlin.
- MVVM architecture.
- Retrofit with Coroutines
- Room for local data storage.
- Google Sign In or password Authentication.
- Firebase messaging for notifications.
- Sentry for error logging.
- Koin for dependency injection.
- M-Pesa payments.

## Prerequisites

### Firebase Project

Used for messaging.

1. Create an project on [Firebase](https://console.firebase.google.com/).
2. Add an application `com.marknkamau.justjava.debug`.

### GCP project

Used for maps when adding a delivery address.

1. Go to the [GCP console](https://console.cloud.google.com) and switch to the project created by Firebase (or any other).
2. Enable _Maps SDK for Android_ and _Places API_ in [API Library](https://console.cloud.google.com/apis/library).
3. Take note of the API keys (AIza...) in [credentials](https://console.cloud.google.com/apis/credentials) or create one yourself.

**NB:** If you use your own API endpoint, you will need to change the `googleClientId` in [`/app/build.gradle`](/app/build.gradle#L24)

### Sentry (optional)

Used for error logging.
It is only used in `release` builds.

1. Create a project on [Sentry](https://sentry.io/)
2. Take note of the project's DSN. _[Where can I find my DSN?](https://forum.sentry.io/t/where-can-i-find-my-dsn/4877)_
3. Create a `sentry.properties` file. See [Proguard](https://docs.sentry.io/clients/java/integrations/#proguard) instructions.

## Installation

1. Clone the repository.
2. Create a `keys.properties` file based on`keys.properties.sample`.
3. Add the `play-services.json` file from firebase to `./app`.
4. Open and build in Android Studio.

(optional) To overwrite the API url in debug mode, add `debugApiBaseUrl="http://10.0.2.2:3000"` to
local.properties. **Note:** If your url is not https, you will need to add it to
[network_security_config.xml](./core/src/main/res/xml/network_security_config.xml)

## Screenshots

![App](/images/branding.png)

## Download

<a href='https://play.google.com/store/apps/details?id=com.marknkamau.justjava'>
​    <img alt='Get it on Google Play' 
​         src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'
​         height="116" width="300"/>
</a>
