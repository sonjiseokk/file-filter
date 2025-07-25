package com.example.filefilter.controller;

import com.example.filefilter.controller.dto.ApiResponse;
import com.example.filefilter.controller.dto.FileUploadResponse;
import com.example.filefilter.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileUploadController {

    private final FileUploadService uploadService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<?>> uploadFile(@RequestParam MultipartFile file) {
        try {
            // 파일 업로드
            FileUploadResponse response = uploadService.upload(file);

            return ResponseEntity.ok(
                    ApiResponse.success(response, "파일을 성공적으로 업로드하였습니다.")
            );
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("파일 업로드 중 오류가 발생했습니다."));
        }
    }
}
