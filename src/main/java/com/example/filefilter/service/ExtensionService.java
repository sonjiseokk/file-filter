package com.example.filefilter.service;

import com.example.filefilter.entity.BlockedExtension;
import com.example.filefilter.entity.ExtensionType;
import com.example.filefilter.entity.dto.ExtensionDto;
import com.example.filefilter.repository.BlockedExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExtensionService {

    private final BlockedExtensionRepository repository;

    @Transactional
    public void toggleDefaultExtension(String extension) {
        // low case
        extension = extension.toLowerCase();

        // Default 확장자명 조회
        BlockedExtension defaultExtension = repository.findByExtension(extension).get();

        // Update Delete status
        if (defaultExtension.isDeleted()) {
            defaultExtension.restore();
        } else {
            defaultExtension.softDelete();
        }
    }

    @Transactional
    public void customRegisterOrRestore(String extension) {
        // low case
        extension = extension.toLowerCase();

        // default 확장자명인 경우 예외 발생
        if (ExtensionType.isDefault(extension)) {
            throw new IllegalArgumentException("디폴트 확장자명은 등록할 수 없습니다.");
        }

        // 1. 커스텀 확장자명 조회
        Optional<BlockedExtension> existingOpt = repository.findByExtension(extension);

        // 2. 등록 된 적이 없는 확장자명의 경우
        if (existingOpt.isEmpty()) {
            // 생성 후 저장
            BlockedExtension newExtension = BlockedExtension.create(extension);
            repository.save(newExtension);
        }

        BlockedExtension existing = existingOpt.get();

        // 3. 한번 등록된 적이 있지만 현재는 삭제된 확장자명
        if (existing.isDeleted()) {
            // 복구 및 명시적 세이브
            existing.restore();
            repository.save(existing);
        } else {
            // 4. 이미 등록되어있고, 중복 시도인 경우
            throw new IllegalStateException("이미 등록된 확장자입니다.");
        }
    }

    public void delete(String extension) {
        BlockedExtension ext = repository.findByExtensionAndDeletedFalse(extension)
                .orElseThrow(() -> new IllegalArgumentException("저장되지 않는 확장자입니다."));

        ext.softDelete();
        repository.save(ext);
    }

    public List<ExtensionDto> getCustomExtensions() {
        List<BlockedExtension> customExtensions = repository.findCustoms(ExtensionType.DEFAULT.getExtensions());

        return customExtensions.stream()
                .map(e -> new ExtensionDto(e.getExtension().toLowerCase(), e.isDeleted()))
                .toList();
    }

    public List<ExtensionDto> getDefaultExtensions() {
        List<BlockedExtension> defaultExtensions = repository.findDefaults(ExtensionType.DEFAULT.getExtensions());

        return defaultExtensions.stream()
                .map(e -> new ExtensionDto(e.getExtension().toLowerCase(), e.isDeleted()))
                .toList();
    }

    public boolean isBlocked(String extension) {
        return repository.findByExtensionAndDeletedFalse(extension)
                .isPresent();
    }
}

