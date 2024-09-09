# jni-native-access-test
Tests JDK-24 enable native access for JNI.

The test application is composed of three modules:

1. `jni-test-lib`: A C++ project that builds a simple shared library implementing the native method `org.graalvm.jni.test.app.NativeLibrary#init0`.
2. `jni-test-loader`: A Java project that defines the `loader` module, providing the `org.graalvm.jni.test.loader.Loader#load(Path)` method, which wraps the `System#load(String)` method.
3. `jni-test-app`: A Java project that provides a simple application with the `org.graalvm.jni.test.app.NativeLibrary` class containing the `init0` native method. It also includes a Java main class that uses `org.graalvm.jni.test.loader.Loader#load(Path)` to load the native library.

## Usage
```
export JAVA_HOME=/path/to/jdk24+13
jni-native-access-test> mvn install
jni-native-access-test> cd jni-test-app
jni-test-app> mvn exec:exec
```

## Output
For JDK versions older than JDK 24+13, the expected output is simply `Hello`, printed by the C++ shared library.
For JDK 24+13 and newer, the output is:

```
WARNING: A restricted method in java.lang.System has been called
WARNING: java.lang.System::load has been called by org.graalvm.jni.test.loader.Loader in module loader (file:/Users/tom/.m2/repository/org/graalvm/jni/test/jni-test-loader/1.0-SNAPSHOT/jni-test-loader-1.0-SNAPSHOT.jar)
WARNING: Use --enable-native-access=loader to avoid a warning for callers in this module
WARNING: Restricted methods will be blocked in a future release unless native access is enabled

WARNING: A native method in org.graalvm.jni.test.app.NativeLibrary has been bound
WARNING: org.graalvm.jni.test.app.NativeLibrary::init0 is declared in module app (file:/Users/tom/Downloads/jni/jni-test-app/target/classes/)
WARNING: Use --enable-native-access=app to avoid a warning for native methods declared in this module
WARNING: Restricted methods will be blocked in a future release unless native access is enabled

Hello
```

This demonstrates that not only does the call to `System#load(String)` made by the `loader` module require the `--enable-native-access` flag, but the presence of a class with a native method in the `app` module also necessitates the use of `--enable-native-access`.