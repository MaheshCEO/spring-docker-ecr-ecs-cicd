package com.batch.spring_batch_process.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch.spring_batch_process.dao.ChunkAudit;

@Repository
public interface ChunkAuditRepository extends JpaRepository<ChunkAudit, Long> {
    // You can define custom query methods if needed
	
	 Optional<ChunkAudit> findByStepNameAndChunkNumberAndStatus(String stepName, Integer chunkNumber, String status);
}
