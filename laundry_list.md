Laundry List
============

- Remove Time.now() in order to make testing Time independent of calendar instance
- Too much logic in SunshineWatchFaceService
   - SunshineWatchFaceService knows how to interact the SunshineWatchFace with Sunshine Handheld App
     via RPC
   - It knows rendering, timers, layouts / positioning, colors, logic of what mode to use
     and bla bla bla...
