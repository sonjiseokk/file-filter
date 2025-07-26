package com.example.filefilter.repository;

import com.example.filefilter.entity.BlockedExtensionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlockedExtensionHistoryRepository extends JpaRepository<BlockedExtensionHistory, Long> {
    @Query("SELECT h FROM BlockedExtensionHistory h JOIN FETCH h.blockedExtension")
    List<BlockedExtensionHistory> findAllWithExtension();
}
