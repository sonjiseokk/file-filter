package com.example.filefilter.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FileUploadResponse {
    private final String name;
    private final String extension;

    @JsonProperty("created_at")
    private final LocalDateTime createdAt;

    public FileUploadResponse(String name, String extension) {
        this.name = name;
        this.extension = extension;
        this.createdAt = LocalDateTime.now();
    }
}
