package com.codecalibrate.domain.content;

import com.codecalibrate.config.GitHubExerciseContentProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

@Component
public class GitHubExerciseContentClient {

    private static final Pattern EXTERNAL_ID_PATTERN =
            Pattern.compile("[a-z0-9]+(?:-[a-z0-9]+)*");

    private final RestClient restClient;
    private final JsonMapper jsonMapper;
    private final GitHubExerciseContentProperties properties;

    public GitHubExerciseContentClient(
            RestClient.Builder restClientBuilder,
            JsonMapper jsonMapper,
            GitHubExerciseContentProperties properties
    ) {
        this.restClient = restClientBuilder.build();
        this.jsonMapper = jsonMapper;
        this.properties = properties;
    }

    public ExerciseContentDefinition getExerciseContent(String externalId) {
        if (!EXTERNAL_ID_PATTERN.matcher(externalId).matches()) {
            throw new IllegalArgumentException("Exercise ID is invalid.");
        }

        JsonNode githubResponse = restClient.get()
                .uri(buildContentUri(externalId))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.token())
                .header(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .retrieve()
                .body(JsonNode.class);

        String encoding = githubResponse == null
                ? null
                : githubResponse.path("encoding").asString();
        String encodedContent = githubResponse == null
                ? null
                : githubResponse.path("content").asString();

        if (!"base64".equals(encoding)
                || encodedContent == null
                || encodedContent.isBlank()) {
            throw new IllegalStateException(
                    "GitHub did not return a valid exercise-content file."
            );
        }

        try {
            byte[] contentBytes = Base64.getMimeDecoder().decode(encodedContent);

            return jsonMapper.readValue(
                    new String(contentBytes, StandardCharsets.UTF_8),
                    ExerciseContentDefinition.class
            );
        } catch (IllegalArgumentException | JacksonException exception) {
            throw new IllegalStateException(
                    "GitHub exercise content could not be decoded.",
                    exception
            );
        }
    }

    private URI buildContentUri(String externalId) {
        String apiUrl = properties.apiUrl().toString();

        if (apiUrl.endsWith("/")) {
            apiUrl = apiUrl.substring(0, apiUrl.length() - 1);
        }

        return URI.create(
                apiUrl
                        + "/repos/" + properties.repository()
                        + "/contents/" + properties.exerciseDirectory()
                        + "/" + externalId + ".json"
                        + "?ref=" + properties.revision()
        );
    }
}
