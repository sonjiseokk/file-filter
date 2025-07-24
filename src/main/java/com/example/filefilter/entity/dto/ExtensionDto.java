package com.example.filefilter.entity.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExtensionDto {
    private final String extension;
    private final boolean isDefault;
}
