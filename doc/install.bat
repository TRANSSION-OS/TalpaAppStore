adb root
adb remount
adb shell pm clear com.aurora.market
adb shell rm -rf /system/priv-app/TalpaGC/*
adb shell rm -rf /system/priv-app/TalpaGC/*
adb shell rm -rf /data/data/tran.com.android.taplagame
adb push ..\gamecenter\build\outputs\apk\gamecenter-release.apk  /system/priv-app/TalpaGC/gamecenter-release.apk
adb reboot