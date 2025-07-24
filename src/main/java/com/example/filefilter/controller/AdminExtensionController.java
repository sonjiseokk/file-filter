package com.example.filefilter.controller;

import com.example.filefilter.controller.dto.ExtensionListResponse;
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
    public ResponseEntity<?> addBlockExtension(@RequestBody String extension) {
        extensionService.customRegisterOrRestore(extension);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/custom/{extension}")
    public ResponseEntity<?> deleteBlockExtension(@PathVariable String extension) {
        extensionService.delete(extension);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/default/{extension}")
    public ResponseEntity<?> updateDefaultExtension(@PathVariable String extension) {
        extensionService.toggleDefaultExtension(extension);

        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<?> listBlockExtensions() {
        List<ExtensionDto> defaultExtensions = extensionService.getDefaultExtensions();
        List<ExtensionDto> customExtensions = extensionService.getCustomExtensions();

        ExtensionListResponse response = new ExtensionListResponse(defaultExtensions, customExtensions);
        return ResponseEntity.ok(response);
    }
}
