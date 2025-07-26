package com.example.filefilter.entity.dto;

import com.example.filefilter.entity.BlockedExtension;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class BlockedExtensionHistoryDto {
    private final String extension;
    private final LocalDateTime deletedAt;
}
