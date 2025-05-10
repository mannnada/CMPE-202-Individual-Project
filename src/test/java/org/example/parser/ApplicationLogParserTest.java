package org.example.parser;

import com.logparser.model.ApplicationLogEntry;
import com.logparser.model.LogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationLogParserTest {
    private ApplicationLogParser parser;

    @BeforeEach
    void setUp() {
        parser = new ApplicationLogParser();
    }

    @Test
    void testCanParse_ValidApplicationLog() {
        String logLine = "timestamp=2024-02-24T16:22:20Z level=INFO message=\"Scheduled maintenance starting\" host=webserver1";
        assertTrue(parser.canParse(logLine));
    }

    @Test
    void testCanParse_NotApplicationLog() {
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72";
        assertFalse(parser.canParse(logLine));
    }

    @Test
    void testParse_ValidApplicationLog() {
        String logLine = "timestamp=2024-02-24T16:22:20Z level=INFO message=\"Scheduled maintenance starting\" host=webserver1";
        Optional<LogEntry> result = parser.parse(logLine);

        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof ApplicationLogEntry);

        ApplicationLogEntry appLog = (ApplicationLogEntry) result.get();
        assertEquals("INFO", appLog.getLevel());
        assertEquals("Scheduled maintenance starting", appLog.getMessage());
        assertEquals("webserver1", appLog.getHost());
    }

    @Test
    void testParse_InvalidFormat() {
        String logLine = "timestamp=2024-02-24T16:22:20Z level=INFO host=webserver1";  // missing message
        Optional<LogEntry> result = parser.parse(logLine);

        assertFalse(result.isPresent());
    }
}