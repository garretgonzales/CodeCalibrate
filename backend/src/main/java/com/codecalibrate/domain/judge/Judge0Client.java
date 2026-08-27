package com.codecalibrate.domain.judge;

import com.codecalibrate.config.Judge0Properties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

import java.net.URI;
import java.util.Map;

@Component
public class Judge0Client {

    private final RestClient restClient;
    private final Judge0Properties properties;
// make browser request wait for final verdict or fail (5sec)
    private static final int MAX_STATUS_CHECKS = 10;
    private static final long STATUS_CHECK_DELAY_MILLIS = 500;

    public Judge0Client(
            RestClient.Builder restClientBuilder,
            Judge0Properties properties
    ) {
        this.restClient = restClientBuilder.build();
        this.properties = properties;
    }

    public String createSubmission(
            String sourceCode,
            String standardInput,
            String expectedOutput,
            int timeLimitSeconds,
            int memoryLimitKilobytes
    ) {
        SubmissionToken response = restClient.post()
                .uri(buildSubmissionUri())
                .header("X-RapidAPI-Host", properties.rapidApiHost())
                .header("X-RapidAPI-Key", properties.rapidApiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "source_code", sourceCode,
                        "language_id", properties.javaLanguageId(),
                        "stdin", standardInput,
                        "expected_output", expectedOutput,
                        "cpu_time_limit", timeLimitSeconds,
                        "memory_limit", memoryLimitKilobytes
                ))
                .retrieve()
                .body(SubmissionToken.class);

        if (response == null || response.token() == null || response.token().isBlank()) {
            throw new IllegalStateException("Judge0 did not return a submission token.");
        }

        return response.token();
    }


    public Judge0SubmissionStatus getSubmissionStatus(String token) {
        JsonNode response = restClient.get()
                .uri(buildSubmissionStatusUri(token))
                .header("X-RapidAPI-Host", properties.rapidApiHost())
                .header("X-RapidAPI-Key", properties.rapidApiKey())
                .retrieve()
                .body(JsonNode.class);

        int statusId = response == null
                ? -1
                : response.path("status_id").asInt(-1);

        if (statusId < 1) {
            throw new IllegalStateException("Judge0 did not return a valid submission status.");
        }

        return new Judge0SubmissionStatus(statusId);
    }
    
    // browser request rule
    public Judge0SubmissionStatus awaitSubmissionStatus(String token) {
        for (int attempt = 0; attempt < MAX_STATUS_CHECKS; attempt++) {
            Judge0SubmissionStatus status = getSubmissionStatus(token);

            if (status.isFinished()) {
                return status;
            }

            if (attempt < MAX_STATUS_CHECKS - 1) {
                pauseBeforeNextStatusCheck();
            }
        }

        throw new IllegalStateException(
                "Judge0 did not finish the submission within the allowed wait time."
        );
    }


    // browser rule helper
    private void pauseBeforeNextStatusCheck() {
        try {
            Thread.sleep(STATUS_CHECK_DELAY_MILLIS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "Judge0 submission polling was interrupted.",
                    exception
            );
        }
    }

    private URI buildSubmissionUri() {
        String apiUrl = properties.apiUrl().toString();

        if (apiUrl.endsWith("/")) {
            apiUrl = apiUrl.substring(0, apiUrl.length() - 1);
        }

        return URI.create(apiUrl + "/submissions?base64_encoded=false&wait=false");
    }

    private record SubmissionToken(String token) {
    }

    private URI buildSubmissionStatusUri(String token) {
        String apiUrl = properties.apiUrl().toString();

        if (apiUrl.endsWith("/")) {
            apiUrl = apiUrl.substring(0, apiUrl.length() - 1);
        }

        return URI.create(
                apiUrl
                        + "/submissions/" + token
                        + "?base64_encoded=false&fields=status_id"
        );
    }
}