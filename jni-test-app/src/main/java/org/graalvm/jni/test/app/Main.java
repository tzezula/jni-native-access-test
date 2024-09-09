package org.graalvm.jni.test.app;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.graalvm.jni.test.loader.Loader;

public final class Main {

    public static void main(String[] args) throws Exception {
        String libProject = System.getProperty("jni.library.project");
        if (libProject == null) {
            throw new IllegalArgumentException( "jni.library.project must point to maven native library NAR project.");
        }
        Path target = Path.of(libProject, "target");
        if (!Files.isDirectory(target)) {
            throw new IllegalArgumentException( target + " directory does not exist.");
        }
        try (Stream<Path> stream = Files.walk(target)) {
            Path sharedLibrary = stream.filter(Main::isSharedLibrary).findAny().orElseThrow(() -> new NoSuchElementException("No shared library found in " + target));
            Loader.load(sharedLibrary);
            NativeLibrary.init();
        }
    }

    private static boolean isSharedLibrary(Path path) {
        String name = path.getFileName().toString();
        String os = System.getProperty("os.name");
        if (os.equalsIgnoreCase("linux")) {
            return name.endsWith(".so");
        } else if (os.equalsIgnoreCase("mac os x") || os.equalsIgnoreCase("darwin")) {
            return name.endsWith(".dylib");
        } else {
            throw new UnsupportedOperationException("Not supported os " + os);
        }
    }
}