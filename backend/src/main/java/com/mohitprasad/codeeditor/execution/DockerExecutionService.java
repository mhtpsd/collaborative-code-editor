package com.mohitprasad.codeeditor.execution;

import com.mohitprasad.codeeditor.model.entity.ExecutionResult;
import com.mohitprasad.codeeditor.model.enums.ExecutionStatus;
import com.mohitprasad.codeeditor.repository.ExecutionResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class DockerExecutionService {

    private static final long TIMEOUT_SECONDS = 10;
    private static final long MEMORY_LIMIT_MB = 256;

    private final ExecutionResultRepository executionResultRepository;

    public void execute(ExecutionResult result, String code, String language) {
        long startTime = System.currentTimeMillis();
        Path tempDir = null;

        try {
            LanguageRuntime runtime = LanguageRuntime.fromString(language);
            tempDir = Files.createTempDirectory("code-exec-");

            Path codeFile = tempDir.resolve(runtime.getFilename());
            Files.writeString(codeFile, code);

            String dockerCmd = buildDockerCommand(runtime, tempDir.toAbsolutePath().toString());

            ProcessBuilder pb = new ProcessBuilder("sh", "-c", dockerCmd);
            pb.redirectErrorStream(false);
            Process process = pb.start();

            boolean finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);

            if (!finished) {
                process.destroyForcibly();
                result.setStatus(ExecutionStatus.TIMEOUT);
                result.setError("Execution timed out after " + TIMEOUT_SECONDS + " seconds");
                result.setExitCode(-1);
            } else {
                String stdout = readStream(process.getInputStream());
                String stderr = readStream(process.getErrorStream());
                int exitCode = process.exitValue();

                result.setOutput(stdout);
                result.setError(stderr.isEmpty() ? null : stderr);
                result.setExitCode(exitCode);
                result.setStatus(exitCode == 0 ? ExecutionStatus.COMPLETED : ExecutionStatus.FAILED);
            }

        } catch (Exception e) {
            log.error("Execution failed for result: {}", result.getId(), e);
            result.setStatus(ExecutionStatus.FAILED);
            result.setError("Internal execution error: " + e.getMessage());
            result.setExitCode(-1);
        } finally {
            result.setExecutionTimeMs(System.currentTimeMillis() - startTime);
            result.setCompletedAt(Instant.now());
            executionResultRepository.save(result);

            if (tempDir != null) {
                cleanupTempDir(tempDir);
            }
        }
    }

    private String buildDockerCommand(LanguageRuntime runtime, String hostDir) {
        return String.format(
            "docker run --rm " +
            "--network none " +
            "--memory=%dm " +
            "--memory-swap=%dm " +
            "--cpus=0.5 " +
            "--read-only " +
            "--tmpfs /tmp:size=64m " +
            "-v %s:/code:ro " +
            "-w /code " +
            "%s " +
            "sh -c '%s'",
            MEMORY_LIMIT_MB, MEMORY_LIMIT_MB,
            hostDir,
            runtime.getDockerImage(),
            runtime.getRunCommand()
        );
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null && lineCount < 1000) {
                sb.append(line).append("\n");
                lineCount++;
            }
        }
        return sb.toString();
    }

    private void cleanupTempDir(Path dir) {
        try {
            Files.walk(dir)
                 .sorted(java.util.Comparator.reverseOrder())
                 .forEach(path -> {
                     try { Files.delete(path); } catch (IOException ignored) {}
                 });
        } catch (IOException e) {
            log.warn("Failed to cleanup temp directory: {}", dir, e);
        }
    }
}
