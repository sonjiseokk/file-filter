package com.example.filefilter.service;

import com.example.filefilter.controller.dto.FileUploadResponse;
import com.example.filefilter.exception.FileUploadException;
import com.example.filefilter.util.FileNameParser;
import com.example.filefilter.util.FileNameSanitizer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileUploadService {
    private final ExtensionService extensionService;
    private final S3UploadService s3UploadService;

    public FileUploadResponse upload(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();

        // 유니코드 공백 제거 후 이름 파싱(ex. n ame.txt)
        String name = FileNameParser.getName(FileNameSanitizer.clean(originalName));
        String extension = FileNameParser.getExtension(originalName);
        // tar.gz 같은 확장자 고려
        String fullExtension = FileNameParser.getFullExtension(originalName);

        // 확장자 차단 목록 검사
        if (extensionService.isBlocked(extension)) {
            throw new FileUploadException(name, extension, "허용되지 않은 파일 형식입니다.");
        }

        // 확장자가 없이 업로드 되는 경우
        if (extension.isEmpty()) {
            if (extensionService.isBlocked(name)) {
                throw new FileUploadException(name, name, "허용되지 않은 파일 형식입니다.");
            }
        }

        if (!extension.equalsIgnoreCase(fullExtension)
                && extensionService.isBlocked(fullExtension)) {
            throw new FileUploadException(name, fullExtension, "허용되지 않은 파일 형식입니다.");
        }

        // S3에 파일 업로드
        s3UploadService.upload(file, name, extension);

        return new FileUploadResponse(name, extension);
    }
}
