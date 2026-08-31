package com.codecalibrate.domain.judge;

import com.codecalibrate.config.Judge0Properties;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import com.codecalibrate.domain.Judge0UnavailableException;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;


public class Judge0ClientTest {

    @Test
    void shouldCreateJavaSubmissionWithRapidApiHeaders() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder)
                .build();

        Judge0Properties properties = new Judge0Properties(
                URI.create("https://judge0-ce.p.rapidapi.com"),
                "judge0-ce.p.rapidapi.com",
                "test-key",
                91
        );

        Judge0Client client = new Judge0Client(restClientBuilder, properties);

        mockServer.expect(requestTo(
                        "https://judge0-ce.p.rapidapi.com/" +
                                "submissions?base64_encoded=false&wait=false"
                ))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(
                        "X-RapidAPI-Host",
                        "judge0-ce.p.rapidapi.com"
                ))
                .andExpect(header("X-RapidAPI-Key", "test-key"))
                .andExpect(content().json("""
                        {
                          "source_code": "public class Main {}",
                          "language_id": 91,
                          "stdin": "",
                          "expected_output": "25\\n",
                          "cpu_time_limit": 2,
                          "memory_limit": 128000
                        }
                        """))
                .andRespond(withSuccess(
                        """
                        {
                          "token": "judge0-submission-token"
                        }
                        """,
                        MediaType.APPLICATION_JSON
                ));

        String token = client.createSubmission(
                "public class Main {}",
                "",
                "25\n",
                2,
                128000
        );

        assertThat(token).isEqualTo("judge0-submission-token");

        mockServer.verify();
    }

    @Test
    void shouldRetrieveOnlySubmissionStatus() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder)
                .build();

        Judge0Properties properties = new Judge0Properties(
                URI.create("https://judge0-ce.p.rapidapi.com"),
                "judge0-ce.p.rapidapi.com",
                "test-key",
                91
        );

        Judge0Client client = new Judge0Client(restClientBuilder, properties);

        mockServer.expect(requestTo(
                        "https://judge0-ce.p.rapidapi.com/" +
                                "submissions/judge0-submission-token" +
                                "?base64_encoded=false&fields=status_id"
                ))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(
                        "X-RapidAPI-Host",
                        "judge0-ce.p.rapidapi.com"
                ))
                .andExpect(header("X-RapidAPI-Key", "test-key"))
                .andRespond(withSuccess(
                        """
                        {
                          "status_id": 3
                        }
                        """,
                        MediaType.APPLICATION_JSON
                ));

        Judge0SubmissionStatus status =
                client.getSubmissionStatus("judge0-submission-token");

        assertThat(status.statusId()).isEqualTo(3);
        assertThat(status.isFinished()).isTrue();
        assertThat(status.isAccepted()).isTrue();

        mockServer.verify();
    }

    @Test
    void shouldPollUntilSubmissionIsFinished() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder)
                .build();

        Judge0Properties properties = new Judge0Properties(
                URI.create("https://judge0-ce.p.rapidapi.com"),
                "judge0-ce.p.rapidapi.com",
                "test-key",
                91
        );

        Judge0Client client = new Judge0Client(restClientBuilder, properties);

        String statusUrl = "https://judge0-ce.p.rapidapi.com/" +
                "submissions/judge0-submission-token" +
                "?base64_encoded=false&fields=status_id";

        mockServer.expect(requestTo(statusUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        """
                        {
                          "status_id": 1
                        }
                        """,
                        MediaType.APPLICATION_JSON
                ));

        mockServer.expect(requestTo(statusUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        """
                        {
                          "status_id": 2
                        }
                        """,
                        MediaType.APPLICATION_JSON
                ));

        mockServer.expect(requestTo(statusUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        """
                        {
                          "status_id": 3
                        }
                        """,
                        MediaType.APPLICATION_JSON
                ));

        Judge0SubmissionStatus status =
                client.awaitSubmissionStatus("judge0-submission-token");

        assertThat(status.statusId()).isEqualTo(3);
        assertThat(status.isFinished()).isTrue();
        assertThat(status.isAccepted()).isTrue();

        mockServer.verify();
    }

    @Test
    void shouldTranslateProviderFailureWhenCreatingSubmission() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer mockServer =
                MockRestServiceServer.bindTo(restClientBuilder).build();

        Judge0Properties properties = new Judge0Properties(
                URI.create("https://judge0-ce.p.rapidapi.com"),
                "judge0-ce.p.rapidapi.com",
                "test-key",
                91
        );

        Judge0Client client = new Judge0Client(restClientBuilder, properties);

        mockServer.expect(requestTo(
                          "https://judge0-ce.p.rapidapi.com/" +
                          "submissions?base64_encoded=false&wait=false"
                  ))
                  .andExpect(method(HttpMethod.POST))
                  .andRespond(withStatus(HttpStatus.FORBIDDEN)
                          .contentType(MediaType.APPLICATION_JSON)
                          .body("""
                            {
                              "message": "You are not subscribed to this API."
                            }
                            """));

        assertThatThrownBy(() -> client.createSubmission(
                "public class Main {}",
                "",
                "25\n",
                2,
                128000
        ))
                .isInstanceOf(Judge0UnavailableException.class)
                .hasMessage(
                        "Exercise validation is temporarily unavailable. Please try again."
                )
                .hasMessageNotContaining("subscribed");

        mockServer.verify();
    }

    /*Testing for Judge0 submission polling- prove provider response body  */

    @Test
    void shouldTranslateProviderFailureWhenRetrievingSubmissionStatus() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer mockServer =
                MockRestServiceServer.bindTo(restClientBuilder).build();

        Judge0Properties properties = new Judge0Properties(
                URI.create("https://judge0-ce.p.rapidapi.com"),
                "judge0-ce.p.rapidapi.com",
                "test-key",
                91
        );

        Judge0Client client = new Judge0Client(restClientBuilder, properties);

        mockServer.expect(requestTo(
                          "https://judge0-ce.p.rapidapi.com/" +
                          "submissions/judge0-submission-token" +
                          "?base64_encoded=false&fields=status_id"
                  ))
                  .andExpect(method(HttpMethod.GET))
                  .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE)
                          .contentType(MediaType.APPLICATION_JSON)
                          .body("""
                            {
                              "message": "Service temporarily unavailable."
                            }
                            """));

        assertThatThrownBy(() ->
                client.getSubmissionStatus("judge0-submission-token")
        )
                .isInstanceOf(Judge0UnavailableException.class)
                .hasMessage(
                        "Exercise validation is temporarily unavailable. Please try again."
                )
                .hasMessageNotContaining("Service temporarily unavailable.");

        mockServer.verify();
    }
}