package com.example.filefilter.exception;

import lombok.Getter;

@Getter
public class FileUploadException extends RuntimeException {
    private final String fileName;
    private final String fileExtension;

    public FileUploadException(String fileName, String fileExtension, String message) {
        super(message);
        this.fileName = fileName;
        this.fileExtension = fileExtension;
    }
}
