package com.mohitprasad.codeeditor.repository;

import com.mohitprasad.codeeditor.model.entity.ExecutionResult;
import com.mohitprasad.codeeditor.model.enums.ExecutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExecutionResultRepository extends JpaRepository<ExecutionResult, UUID> {
    List<ExecutionResult> findByRoomIdOrderByCreatedAtDesc(UUID roomId);
    List<ExecutionResult> findByStatus(ExecutionStatus status);
}
