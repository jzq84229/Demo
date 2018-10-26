# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\Android-sdk\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#// 注：v.x.x.x根据实际版本号修改，例如v5.0.0.1b
#-libraryjars libs/Yuntx_IMLib_vx.x.x.jar(如果是Android Studio 此行忽略)
-keep class com.yuntongxun.ecsdk.** {*; }