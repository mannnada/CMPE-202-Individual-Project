package org.example;

import org.example.aggregator.APMLogAggregator;
import org.example.aggregator.ApplicationLogAggregator;
import org.example.aggregator.RequestLogAggregator;
import org.example.util.LogProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LogProcessorTest {
    private LogProcessor processor;

    @TempDir
    Path tempDir;

    private Path logFile;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary log file with sample data
        logFile = tempDir.resolve("test_log.txt");
        String sampleLog =
                "timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72\n" +
                        "timestamp=2024-02-24T16:22:20Z level=INFO message=\"Scheduled maintenance starting\" host=webserver1\n" +
                        "timestamp=2024-02-24T16:22:25Z request_method=POST request_url=\"/api/update\" response_status=202 response_time_ms=200 host=webserver1\n" +
                        "timestamp=2024-02-24T16:22:30Z metric=memory_usage_percent host=webserver1 value=85\n" +
                        "timestamp=2024-02-24T16:22:35Z level=ERROR message=\"Update process failed\" error_code=5012 host=webserver1\n" +
                        "timestamp=2024-02-24T16:22:40Z request_method=GET request_url=\"/api/status\" response_status=200 response_time_ms=100 host=webserver1\n" +
                        "Invalid log line that should be ignored\n";

        Files.writeString(logFile, sampleLog);

        // Create processor with the test file
        processor = new LogProcessor(logFile.toString());
    }

    @Test
    void testProcessLogFile() throws IOException {
        // Process the log file
        processor.processLogFile();

        // Get aggregators
        APMLogAggregator apmAggregator = processor.getApmAggregator();
        ApplicationLogAggregator appAggregator = processor.getApplicationAggregator();
        RequestLogAggregator reqAggregator = processor.getRequestAggregator();

        // Verify APM logs were processed correctly
        Map<String, Object> apmData = apmAggregator.getAggregatedData();
        assertTrue(apmData.containsKey("cpu_usage_percent"));
        assertTrue(apmData.containsKey("memory_usage_percent"));

        // Verify Application logs were processed correctly
        Map<String, Object> appData = appAggregator.getAggregatedData();
        assertEquals(1, appData.get("INFO"));
        assertEquals(1, appData.get("ERROR"));

        // Verify Request logs were processed correctly
        Map<String, Object> reqData = reqAggregator.getAggregatedData();
        assertTrue(reqData.containsKey("/api/update"));
        assertTrue(reqData.containsKey("/api/status"));
    }

    @Test
    void testProcessInvalidLogFile() {
        // Create processor with non-existent file
        LogProcessor invalidProcessor = new LogProcessor("non_existent_file.txt");

        // Processing should throw IOException
        assertThrows(IOException.class, invalidProcessor::processLogFile);
    }
}