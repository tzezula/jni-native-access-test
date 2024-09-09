package org.graalvm.jni.test.app;

final class NativeLibrary {

    static void init() {
        init0();
    }

    private static native void init0();
}