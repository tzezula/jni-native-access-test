package org.graalvm.jni.test.loader;

import java.nio.file.Path;

public final class Loader {

    private Loader() {
    }

    public static void load(Path path) {
        System.load(path.toString());
    }
}