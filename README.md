# adb-control
Fixed version of Marian Schedenig's original adb-control utility.

Original version was designed to control an Android device using ADB (if debugging is enabled) when it has got a broken screen. (The only excusing argument. Because it's slow AS HELL).

Fixed version includes some improvements like cleanup on exit and removed config file.

Requirements are the same: [Platform Tools](https://developer.android.com/studio/releases/platform-tools) and [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html)

`build.sh` contains commands to build project right in CLI.

`start.sh` runs application.

Keys:

- Home = Home

- Esc = Back

- End = Power

- Mouse drag = Swipe

- Click = Tap


You can use this utility to turn on Wi-Fi and install something like Airdroid to make a backup of your damaged phone.
