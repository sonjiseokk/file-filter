package com.example.filefilter.repository;

import com.example.filefilter.entity.BlockedExtensionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedExtensionHistoryRepository extends JpaRepository<BlockedExtensionHistory, Long> {
}
