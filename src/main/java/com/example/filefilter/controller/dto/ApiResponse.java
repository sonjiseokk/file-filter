package com.example.filefilter.controller.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final String message;
    private final ErrorInfo error;

    // 성공 응답 생성 메서드들
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "요청이 성공적으로 처리되었습니다.", null);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, null, "요청이 성공적으로 처리되었습니다.", null);
    }

    // 에러 응답 생성 메서드들
    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false, null, null, new ErrorInfo(message, null));
    }

    public static ApiResponse<Void> error(String message, Object details) {
        return new ApiResponse<>(false, null, null, new ErrorInfo(message, details));
    }

    @Getter
    @RequiredArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorInfo {
        private final String message;
        private final Object details;
    }
}