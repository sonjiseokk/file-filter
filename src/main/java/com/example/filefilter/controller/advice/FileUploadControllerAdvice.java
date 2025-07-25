package com.example.filefilter.controller.advice;

import com.example.filefilter.controller.dto.ApiResponse;
import com.example.filefilter.exception.CustomExtensionLimitException;
import com.example.filefilter.exception.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class FileUploadControllerAdvice {

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<?> handleSecurityException(FileUploadException ex) {
        Map<Object, Object> field = new HashMap<>();

        field.put("fileName", ex.getFileName());
        field.put("fileExtension", ex.getFileExtension());
        field.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(field);
    }

    @ExceptionHandler(CustomExtensionLimitException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomExtensionLimitException(CustomExtensionLimitException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage()));
    }
}
