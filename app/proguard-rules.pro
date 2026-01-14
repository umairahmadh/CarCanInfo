# Add project specific ProGuard rules here.
-keep class com.carcaninfo.** { *; }
-keepclassmembers class com.carcaninfo.** { *; }

# Keep CAN adapter related classes
-keep class * implements com.carcaninfo.can.CanAdapter { *; }

# Keep data models
-keep class com.carcaninfo.model.** { *; }

# Optimize
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
