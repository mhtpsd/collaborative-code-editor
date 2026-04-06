package com.mohitprasad.codeeditor.service;

import com.mohitprasad.codeeditor.model.dto.ExecutionRequest;
import com.mohitprasad.codeeditor.model.dto.ExecutionResponse;

import java.util.UUID;

public interface CodeExecutionService {
    ExecutionResponse submitExecution(ExecutionRequest request);
    ExecutionResponse getResult(UUID executionId);
    String getStatus(UUID executionId);
}
