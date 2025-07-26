package com.example.filefilter.service;

import com.example.filefilter.entity.BlockedExtension;
import com.example.filefilter.entity.BlockedExtensionHistory;
import com.example.filefilter.entity.ExtensionType;
import com.example.filefilter.entity.dto.BlockedExtensionHistoryDto;
import com.example.filefilter.entity.dto.ExtensionDto;
import com.example.filefilter.exception.CustomExtensionLimitException;
import com.example.filefilter.repository.BlockedExtensionHistoryRepository;
import com.example.filefilter.repository.BlockedExtensionRepository;
import com.example.filefilter.util.FileNameParser;
import com.example.filefilter.util.FileNameSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExtensionService {

    private final BlockedExtensionRepository repository;
    private final BlockedExtensionHistoryRepository historyRepository;

    @Value("${app.extension.custom-limit}")
    private int customLimit;

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
            // 삭제 히스토리 저장
            historyRepository.save(BlockedExtensionHistory.of(defaultExtension));
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

        // 유니코드 공백 제거
        String clean = FileNameSanitizer.clean(extension);

        // 앞뒤 공백 제거 후 문자열이 비어있거나 중간에 공백이 존재하면 예외
        if (clean.isBlank() || clean.contains(" ")) {
            throw new IllegalArgumentException("공백이 포함된 확장자는 등록할 수 없습니다.");
        }

        // 1. 커스텀 확장자명 조회
        Optional<BlockedExtension> existingOpt = repository.findByExtension(clean);

        // 2. 등록 된 적이 없는 확장자명의 경우
        if (existingOpt.isPresent()) {
            BlockedExtension existing = existingOpt.get();
            if (existing.isDeleted()) {
                // 복구 시 개수 제한 확인
                checkCustomExtensionLimit();
                existing.restore();
                repository.save(existing);
            } else {
                // 이미 등록된 경우
                throw new IllegalStateException("이미 등록된 확장자입니다.");
            }
        } else {
            // 3. 한번 등록된 적이 있지만 현재는 삭제된 확장자명
            // 신규 등록 시 개수 제한 확인
            checkCustomExtensionLimit();
            BlockedExtension newExtension = BlockedExtension.create(clean);
            repository.save(newExtension);
        }
    }

    @Transactional
    public void delete(String extension) {
        BlockedExtension ext = repository.findByExtensionAndDeletedFalse(extension)
                .orElseThrow(() -> new IllegalArgumentException("저장되지 않는 확장자입니다."));

        ext.softDelete();
        repository.save(ext);
        // 삭제 히스토리 저장
        historyRepository.save(BlockedExtensionHistory.of(ext));
    }

    public List<String> getCustomExtensions() {
        List<BlockedExtension> customExtensions = repository.findCustoms(ExtensionType.DEFAULT.getExtensions());

        return customExtensions.stream()
                .map(e -> e.getExtension().toLowerCase())
                .toList();
    }

    public List<ExtensionDto> getDefaultExtensions() {
        List<BlockedExtension> defaultExtensions = repository.findDefaults(ExtensionType.DEFAULT.getExtensions());

        return defaultExtensions.stream()
                .map(e -> new ExtensionDto(e.getExtension().toLowerCase(), !e.isDeleted()))
                .toList();
    }

    public boolean isBlocked(String extension) {
        return repository.findByExtensionAndDeletedFalse(extension).isPresent();
    }

    private void checkCustomExtensionLimit() {
        long currentCount = repository.countCustomExtensions(ExtensionType.DEFAULT.getExtensions());
        if (currentCount >= customLimit) {
            throw new CustomExtensionLimitException("커스텀 확장자는 최대 " + customLimit + "개까지만 등록할 수 있습니다.");
        }
    }

    public List<BlockedExtensionHistoryDto> getHistories() {
        // 시간상 페이징은 미구현
        List<BlockedExtensionHistory> entities = historyRepository.findAllWithExtension();

        return entities.stream()
                .map(e -> new BlockedExtensionHistoryDto(e.getBlockedExtension().getExtension(), e.getDeletedAt()))
                .toList();
    }
}

