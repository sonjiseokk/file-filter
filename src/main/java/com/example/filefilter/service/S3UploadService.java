package com.example.filefilter.service;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public void upload(MultipartFile file, String name, String extension) throws IOException {
        String storedFileName = System.currentTimeMillis() + "_" + name + "." + extension;

        // 파일 업로드
        s3Template.upload(bucket, storedFileName, file.getInputStream());
    }
}
