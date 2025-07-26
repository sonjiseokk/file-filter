package com.example.filefilter.controller;

import com.example.filefilter.controller.dto.ApiResponse;
import com.example.filefilter.controller.dto.ExtensionListResponse;
import com.example.filefilter.controller.dto.ExtensionRequest;
import com.example.filefilter.entity.dto.ExtensionDto;
import com.example.filefilter.service.ExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/extensions")
@RequiredArgsConstructor
public class AdminExtensionController {
    private final ExtensionService extensionService;

    @PostMapping("/custom")
    public ResponseEntity<ApiResponse<?>> addBlockExtension(@RequestBody ExtensionRequest request) {
        extensionService.customRegisterOrRestore(request.getExtension());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("커스텀 차단 확장자가 성공적으로 등록되었습니다."));
    }

    @DeleteMapping("/custom/{extension}")
    public ResponseEntity<ApiResponse<?>> deleteBlockExtension(@PathVariable String extension) {
        extensionService.delete(extension);

        return ResponseEntity.ok(
                ApiResponse.success("커스텀 차단 확장자를 성공적으로 삭제했습니다.")
        );
    }

    @PatchMapping("/default/{extension}")
    public ResponseEntity<ApiResponse<?>> updateDefaultExtension(@PathVariable String extension) {
        extensionService.toggleDefaultExtension(extension);

        return ResponseEntity.ok(
                ApiResponse.success("기본 차단 확장자를 성공적으로 변경했습니다.")
        );
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<?>> listBlockExtensions() {
        List<ExtensionDto> defaultExtensions = extensionService.getDefaultExtensions();
        List<String> customExtensions = extensionService.getCustomExtensions();

        ExtensionListResponse response = new ExtensionListResponse(defaultExtensions, customExtensions);

        return ResponseEntity.ok(
                ApiResponse.success(response, "모든 차단 확장자 목록을 성공적으로 조회했습니다.")
        );
    }
}
