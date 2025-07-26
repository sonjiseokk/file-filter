package com.example.filefilter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file, String name, String extension) throws IOException {
        String storedFileName = System.currentTimeMillis() + "_" + name;

        PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                .bucket(bucket)
                .key(storedFileName)
                .contentType(file.getContentType());

        if (isExecutableInBrowser(extension)) {
            // 실행 불가능한 파일은 attachment로 다운로드 되도록 설정
            requestBuilder.contentDisposition("attachment; filename=\"" + storedFileName + "\"");
        }

        PutObjectRequest putObjectRequest = requestBuilder.build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return storedFileName;
    }


    private boolean isExecutableInBrowser(String ext) {
        return List.of("html", "htm", "svg", "js", "json", "xml", "css", "txt", "md")
                .contains(ext.toLowerCase());
    }
}
