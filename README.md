SunshineWear
============

Synchronizes weather information from OpenWeatherMap on Android Phones, Tablets and Android Wear

Android Wear Screenshots
------------------------
![Android Wear - Round](http://res.cloudinary.com/chi6rag/image/upload/c_scale,w_400/v1469354923/SunshineWear/wear_round.png)
![Android Wear - Square](http://res.cloudinary.com/chi6rag/image/upload/c_scale,w_400/v1469354922/SunshineWear/wear_square.png)

Build Instructions (Mac OS X)
-----------------------------
- Clone the repository
- Install [Java](https://java.com/en/download/help/download_options.xml)
- Install [Android Studio](http://developer.android.com/sdk/index.html) with Android SDK Tools
- Follow [these instructions](https://developer.android.com/training/wearables/apps/creating.html) to run the app

Features
--------
- Implemented the watch face using CanvasWatchFaceService
- Syncing data from handheld using Data Layer API
- Listening to sync requests on mobile using Wearable Listener Service
- Listening to sync responses on wearable using DataListener
- Rendering UI to look slick both on Round and Square Android Wear devices
- Timer build with native Handler
- Abstracting logic of display of common UI elements like time and weather info in view models
