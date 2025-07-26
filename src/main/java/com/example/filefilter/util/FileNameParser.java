package com.example.filefilter.util;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Arrays;

@Component
public class FileNameParser {
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        } else {
            int index = indexOfExtension(filename);
            return index == -1 ? "" : filename.substring(index + 1);
        }
    }

    public static String getFullExtension(String filename) {
        if (filename == null || filename.isBlank()) return "";

        String name = getName(filename);
        String[] parts = name.split("\\.");

        if (parts.length <= 1) return "";
        return String.join(".", Arrays.copyOfRange(parts, 1, parts.length));
    }


    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int extensionPos = filename.lastIndexOf(46);
            int lastSeparator = indexOfLastSeparator(filename);

            return lastSeparator > extensionPos ? -1 : extensionPos;
        }
    }

    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int lastUnixPos = filename.lastIndexOf(47);
            int lastWindowsPos = filename.lastIndexOf(92);
            return Math.max(lastUnixPos, lastWindowsPos);
        }
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        } else {
            failIfNullBytePresent(filename);
            int index = indexOfLastSeparator(filename);
            return filename.substring(index + 1);
        }
    }

    private static void failIfNullBytePresent(String path) {
        int len = path.length();

        for(int i = 0; i < len; ++i) {
            if (path.charAt(i) == 0) {
                throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
            }
        }
    }
}
