package com.example.filefilter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BlockedExtension {
    @Id
    @Column(unique = true, nullable = false, length = 20)
    private String extension;

    // 소프트 삭제
    private boolean deleted = false;

    @Enumerated(EnumType.STRING)
    private ExtensionType contentType;

    private BlockedExtension(String extension, ExtensionType contentType) {
        this.extension = extension;
        this.contentType = contentType;
        this.deleted = false;
    }

    // Helper method

    public static BlockedExtension create(String extension) {
        return new BlockedExtension(
                extension,
                ExtensionType.fromExtension(extension)
        );
    }

    public void softDelete() {
        deleted = true;
    }

    public void restore() {
        deleted = false;
    }
}
