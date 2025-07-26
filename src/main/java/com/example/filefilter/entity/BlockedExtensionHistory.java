package com.example.filefilter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockedExtensionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_extension_id", nullable = false)
    private BlockedExtension blockedExtension;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime deletedAt;

    private BlockedExtensionHistory(BlockedExtension blockedExtension) {
        this.blockedExtension = blockedExtension;
    }

    public static BlockedExtensionHistory of(BlockedExtension blockedExtension) {
        return new BlockedExtensionHistory(blockedExtension);
    }
}
