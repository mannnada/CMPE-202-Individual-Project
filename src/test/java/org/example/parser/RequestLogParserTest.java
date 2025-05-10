package org.example.parser;

import org.example.model.LogEntry;
import org.example.model.RequestLogEntry;
import org.example.junit.jupiter.api.BeforeEach;
import org.example.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RequestLogParserTest {
    private RequestLogParser parser;

    @BeforeEach
    void setUp() {
        parser = new RequestLogParser();
    }

    @Test
    void testCanParse_ValidRequestLog() {
        String logLine = "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=202 response_time_ms=200 host=webserver1";
        assertTrue(parser.canParse(logLine));
    }

    @Test
    void testCanParse_NotRequestLog() {
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72";
        assertFalse(parser.canParse(logLine));
    }

    @Test
    void testParse_ValidRequestLog() {
        String logLine = "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=202 response_time_ms=200 host=webserver1";
        Optional<LogEntry> result = parser.parse(logLine);

        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof RequestLogEntry);

        RequestLogEntry reqLog = (RequestLogEntry) result.get();
        assertEquals("POST", reqLog.getRequestMethod());
        assertEquals("/api/update", reqLog.getRequestUrl());
        assertEquals(202, reqLog.getResponseStatus());
        assertEquals(200, reqLog.getResponseTimeMs());
        assertEquals("webserver1", reqLog.getHost());
        assertEquals("2XX", reqLog.getStatusCodeCategory());
    }

    @Test
    void testParse_InvalidFormat() {
        String logLine = "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" host=webserver1";  // missing response fields
        Optional<LogEntry> result = parser.parse(logLine);

        assertFalse(result.isPresent());
    }

    @Test
    void testStatusCodeCategory() {
        String logLine2xx = "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=200 response_time_ms=200 host=webserver1";
        String logLine4xx = "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=404 response_time_ms=200 host=webserver1";
        String logLine5xx = "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=500 response_time_ms=200 host=webserver1";

        RequestLogEntry entry2xx = (RequestLogEntry) parser.parse(logLine2xx).get();
        RequestLogEntry entry4xx = (RequestLogEntry) parser.parse(logLine4xx).get();
        RequestLogEntry entry5xx = (RequestLogEntry) parser.parse(logLine5xx).get();

        assertEquals("2XX", entry2xx.getStatusCodeCategory());
        assertEquals("4XX", entry4xx.getStatusCodeCategory());
        assertEquals("5XX", entry5xx.getStatusCodeCategory());
    }
}