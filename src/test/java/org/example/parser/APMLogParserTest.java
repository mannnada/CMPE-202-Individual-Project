package org.example.parser;

import com.logparser.model.APMLogEntry;
import com.logparser.model.LogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class APMLogParserTest {
    private APMLogParser parser;

    @BeforeEach
    void setUp() {
        parser = new APMLogParser();
    }

    @Test
    void testCanParse_ValidAPMLog() {
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72";
        assertTrue(parser.canParse(logLine));
    }

    @Test
    void testCanParse_NotAPMLog() {
        String logLine = "timestamp=2024-02-24T16:22:20Z level=INFO message=\"Scheduled maintenance starting\" host=webserver1";
        assertFalse(parser.canParse(logLine));
    }

    @Test
    void testParse_ValidAPMLog() {
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72";
        Optional<LogEntry> result = parser.parse(logLine);

        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof APMLogEntry);

        APMLogEntry apmLog = (APMLogEntry) result.get();
        assertEquals("cpu_usage_percent", apmLog.getMetric());
        assertEquals(72.0, apmLog.getValue());
        assertEquals("webserver1", apmLog.getHost());
    }

    @Test
    void testParse_InvalidFormat() {
        String logLine = "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1";  // missing value
        Optional<LogEntry> result = parser.parse(logLine);

        assertFalse(result.isPresent());
    }
}