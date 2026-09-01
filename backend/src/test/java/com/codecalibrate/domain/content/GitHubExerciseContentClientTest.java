package com.codecalibrate.domain.content;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.codecalibrate.config.GitHubExerciseContentProperties;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

public class GitHubExerciseContentClientTest {

  @Test
  void shouldFetchAndDecodePinnedExerciseDefinition() {
    RestClient.Builder restClientBuilder = RestClient.builder();
    MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();

    GitHubExerciseContentProperties properties =
        new GitHubExerciseContentProperties(
            URI.create("https://api.github.com"),
            "garretgonzales/codecalibrate-exercise-content",
            "db6b7da0ce92f563d80143a7aa08f94ebd246a0b",
            "exercises/java",
            "test-token");

    GitHubExerciseContentClient client =
        new GitHubExerciseContentClient(
            restClientBuilder, JsonMapper.builder().build(), properties);

    mockServer
        .expect(
            requestTo(
                "https://api.github.com/repos/garretgonzales/"
                    + "codecalibrate-exercise-content/contents/"
                    + "exercises/java/java-variables-001.json?ref="
                    + "db6b7da0ce92f563d80143a7aa08f94ebd246a0b"))
        .andExpect(method(HttpMethod.GET))
        .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer test-token"))
        .andExpect(header("X-GitHub-Api-Version", "2022-11-28"))
        .andRespond(withSuccess(githubFileResponse(), MediaType.APPLICATION_JSON));

    ExerciseContentDefinition definition = client.getExerciseContent("java-variables-001");

    assertThat(definition.id()).isEqualTo("java-variables-001");
    assertThat(definition.language()).isEqualTo("java");
    assertThat(definition.starterCode()).contains("public class Main");
    assertThat(definition.execution().className()).isEqualTo("Main");
    assertThat(definition.execution().tests())
        .singleElement()
        .satisfies(testCase -> assertThat(testCase.expectedStdout()).isEqualTo("25\n"));

    mockServer.verify();
  }

  private String githubFileResponse() {
    String exerciseJson =
        """
                {
                  "id": "java-variables-001",
                  "version": 1,
                  "language": "java",
                  "title": "Print an Age Variable",
                  "description": "Write a Java program that declares an int variable named age, assigns it the value 25, and prints it.",
                  "difficulty": "Beginner",
                  "starterCode": "public class Main {\\n    public static void main(String[] args) {\\n        // Write your code here\\n    }\\n}\\n",
                                    "references": [
                                      {
                                        "label": "Java variables",
                                        "description": "Review Java variable types, naming, and initialization.",
                                        "url": "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/variables.html"
                                      }
                                    ],
                  "execution": {
                    "className": "Main",
                    "timeLimitSeconds": 2,
                    "memoryLimitKilobytes": 128000,
                    "tests": [
                      {
                        "stdin": "",
                        "expectedStdout": "25\\n"
                      }
                    ]
                  }
                }
                """;

    String encodedContent =
        Base64.getEncoder().encodeToString(exerciseJson.getBytes(StandardCharsets.UTF_8));

    return """
                {
                  "type": "file",
                  "encoding": "base64",
                  "content": "%s"
                }
                """
        .formatted(encodedContent);
  }
}
