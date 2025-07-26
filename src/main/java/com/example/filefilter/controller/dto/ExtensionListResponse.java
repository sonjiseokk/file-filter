package com.example.filefilter.controller.dto;

import com.example.filefilter.entity.dto.ExtensionDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ExtensionListResponse {
    private final List<ExtensionDto> defaultExtensions;
    private final List<String> customExtensions;
}
