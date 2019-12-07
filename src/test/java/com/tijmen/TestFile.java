package com.tijmen;

import java.io.InputStream;
import java.net.URL;

public final class TestFile {
    private TestFile() {
    }

    public static InputStream getAsStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    public static URL getAsUrl(String path) {
        return Thread.currentThread().getContextClassLoader().getResource(path);
    }
}
