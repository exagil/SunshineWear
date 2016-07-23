SunshineWear
============

SunshineWatch - Sunshine watch is a digital watch which displays weather information
                by syncing it with the handheld mobile app

  - Timer - Timer is a clock running in the background which notifies the current time to
            the watch repeatedly

  - SunshineWatchFace - SunshineWatchFace is the view that the user sees when he uses the
                        SunshineWatch

  - WeatherSyncService - WeatherSyncService is a background service which notifies the weather
                         data synced from the handheld mobile app to the watch


------


  - Timer - Timer is a clock running in the background which notifies the current time to
            the watch repeatedly

      - TimeTicker - TimeTicker communicates the tick of a second to watch

  - SunshineWatchFace - SunshineWatchFace is the view that the user sees when he uses the
                        SunshineWatch

      - Canvas - Canvas is a drawing surface on which the the watch UI us drawn

  - WeatherSyncService - WeatherSyncService is a background service which notifies the weather
                           data synced from the handheld mobile app to the watch

      - SunshineClient - A client to communicate with the handheld app

------
