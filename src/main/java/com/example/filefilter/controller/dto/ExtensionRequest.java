package com.example.filefilter.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExtensionRequest {

    @NotBlank(message = "확장자는 비어 있을 수 없습니다.")
    @Size(max = 20, message = "확장자는 최대 20자까지 입력할 수 있습니다.")
    private String extension;
}
