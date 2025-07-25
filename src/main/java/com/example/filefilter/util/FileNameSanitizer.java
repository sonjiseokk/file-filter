package com.example.filefilter.util;

import java.text.Normalizer;

public class FileNameSanitizer {
    public static String clean(String input) {
        if (input == null) return null;

        return Normalizer.normalize(input, Normalizer.Form.NFKC)
                .replaceAll("[\\p{Cntrl}\\u200B\\uFEFF\\u2060]", "")  // 유니코드 우회 제거
                .replaceAll("[/\\\\]", "")  // 경로 구분자 제거
                .replaceAll("^[.]+", "")  // . 또는 ..으로 시작 방지
                .trim();  // 공백 제거
    }
}

