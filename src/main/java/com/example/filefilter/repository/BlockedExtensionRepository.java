package com.example.filefilter.repository;

import com.example.filefilter.entity.BlockedExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlockedExtensionRepository extends JpaRepository<BlockedExtension, Long> {
    Optional<BlockedExtension> findByExtensionAndDeletedFalse(String extension);

    Optional<BlockedExtension> findByExtension(String extension);

    @Query("SELECT b FROM BlockedExtension b WHERE b.extension IN :extensions")
    List<BlockedExtension> findDefaults(@Param("extensions") List<String> extensions);

    @Query("SELECT b FROM BlockedExtension b WHERE b.extension NOT IN :extensions")
    List<BlockedExtension> findCustoms(List<String> extensions);

    @Query("SELECT count(b) FROM BlockedExtension b WHERE b.deleted = false AND b.extension NOT IN :defaultExtensions")
    long countCustomExtensions(@Param("defaultExtensions") List<String> defaultExtensions);

}
