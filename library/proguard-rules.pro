-keep public class *$$SlimBinder { public <init>(); }

-keep class com.metova.slim.*
-keepclasseswithmembernames class * { @com.metova.slim.* <methods>; }
-keepclasseswithmembernames class * { @com.metova.slim.* <fields>; }
