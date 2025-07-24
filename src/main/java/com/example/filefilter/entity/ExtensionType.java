package com.example.filefilter.entity;

import lombok.Getter;

import java.util.List;

@Getter
public enum ExtensionType {
    DEFAULT(List.of("bat", "cmd", "com", "cpl", "exe", "scr", "js")),
    DOCUMENT(List.of("doc", "docx", "pdf", "hwp", "hwpx", "ppt", "pptx", "txt")),
    RECORD(List.of("m4a", "wav", "mp3")),
    IMAGE(List.of("png", "jpeg", "jpg")),
    CHART(List.of()),
    ETC(List.of()),
    FILE(List.of("csv", "xlsx", "xls")),
    TEXT(List.of());

    private final List<String> extensions;

    ExtensionType(List<String> extensions) {
        this.extensions = extensions;
    }

    public static ExtensionType fromExtension(String ext) {
        for (ExtensionType type : values()) {
            if (type.extensions.contains(ext.toLowerCase())) {
                return type;
            }
        }
        return ETC;
    }

    public static boolean isDefault(String extension) {
        if (DEFAULT.extensions.contains(extension.toLowerCase())) {
            return true;
        }
        return false;
    }
}

