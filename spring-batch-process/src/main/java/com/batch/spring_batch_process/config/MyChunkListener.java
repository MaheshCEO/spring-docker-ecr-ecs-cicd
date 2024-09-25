package com.batch.spring_batch_process.config;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.batch.spring_batch_process.dao.ChunkAudit;
import com.batch.spring_batch_process.repository.ChunkAuditRepository;

public class MyChunkListener implements ChunkListener {

	
	private final ChunkAuditRepository chunkAuditRepository;

    @Autowired
    public MyChunkListener(ChunkAuditRepository chunkAuditRepository) {
        this.chunkAuditRepository = chunkAuditRepository;
    }
	
    @Override
    public void beforeChunk(ChunkContext context) {
    	
    	String stepName = context.getStepContext().getStepName();
        int chunkNumber = context.getStepContext().getStepExecution().getExecutionContext().getInt("chunkNumber", 0);
        LocalDateTime startTime = LocalDateTime.now();

        // Create a new audit entry for the chunk
        ChunkAudit audit = new ChunkAudit();
        audit.setStepName(stepName);
        audit.setChunkNumber(chunkNumber);
        audit.setStartTime(startTime);
        audit.setStatus("STARTED");

        chunkAuditRepository.save(audit);  // Save to database

        System.out.println("Before processing chunk: " + stepName + " (Chunk Number: " + chunkNumber + ")");

        // Logic before processing a chunk THIS EXAMPLE FOR ROLLBACK WHEN ANY ERRORS 
    	int a =5;
    	int b =10;
    	if (a==b) {
            throw new RuntimeException("Triggering manual rollback after chunk");
        }
        System.out.println("Before processing chunk: " + context.getStepContext().getStepName());
    }

    @Override
    public void afterChunk(ChunkContext context) {
        // Logic after processing a chunk
        System.out.println("After processing chunk: " + context.getStepContext().getStepName());
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        // Logic after a chunk fails
    	
    	 String stepName = context.getStepContext().getStepName();
         int chunkNumber = context.getStepContext().getStepExecution().getExecutionContext().getInt("chunkNumber", 0);
         String errorMessage = "Error occurred during chunk processing";

         // Update the audit entry with error message and status
         Optional<ChunkAudit> audit = chunkAuditRepository.findByStepNameAndChunkNumberAndStatus(stepName, chunkNumber, "STARTED");
   
//         if (audit != null) {
//        	 audit.ofNullable(value)
//             audit.setErrorMessage(errorMessage);
//             audit.setStatus("FAILED");
//             chunkAuditRepository.save(audit);
//         }
        System.out.println("Error occurred during chunk: " + context.getStepContext().getStepName());
    }
}
