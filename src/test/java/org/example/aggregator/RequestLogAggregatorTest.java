package org.example.aggregator;

import org.example.model.RequestLogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RequestLogAggregatorTest {
    private RequestLogAggregator aggregator;
    private LocalDateTime timestamp = LocalDateTime.parse("2024-02-24T16:22:15Z");

    @BeforeEach
    void setUp() {
        aggregator = new RequestLogAggregator();
    }

    @Test
    void testProcessLog_SingleRoute() {
        // Create Request log entry
        RequestLogEntry entry = new RequestLogEntry(timestamp, "host1", "GET", "/api/status", 200, 150, new HashMap<>());

        // Process the log
        aggregator.processLog(entry);

        // Get aggregated data
        Map<String, Object> result = aggregator.getAggregatedData();

        // Verify results
        assertTrue(result.containsKey("/api/status"));

        @SuppressWarnings("unchecked")
        Map<String, Object> routeData = (Map<String, Object>) result.get("/api/status");

        assertTrue(routeData.containsKey("response_times"));
        assertTrue(routeData.containsKey("status_codes"));

        @SuppressWarnings("unchecked")
        Map<String, Object> responseTimes = (Map<String, Object>) routeData.get("response_times");

        assertEquals(150, responseTimes.get("min"));
        assertEquals(150, responseTimes.get("50_percentile"));
        assertEquals(150, responseTimes.get("max"));

        @SuppressWarnings("unchecked")
        Map<String, Object> statusCodes = (Map<String, Object>) routeData.get("status_codes");

        assertEquals(1, statusCodes.get("2XX"));
        assertEquals(0, statusCodes.get("4XX"));
        assertEquals(0, statusCodes.get("5XX"));
    }

    @Test
    void testProcessLog_MultipleRoutes() {
        // Create Request log entries for /api/status
        RequestLogEntry entry1 = new RequestLogEntry(timestamp, "host1", "GET", "/api/status", 200, 100, new HashMap<>());
        RequestLogEntry entry2 = new RequestLogEntry(timestamp, "host1", "GET", "/api/status", 200, 150, new HashMap<>());
        RequestLogEntry entry3 = new RequestLogEntry(timestamp, "host1", "GET", "/api/status", 500, 300, new HashMap<>());

        // Create Request log entries for /api/update
        RequestLogEntry entry4 = new RequestLogEntry(timestamp, "host1", "POST", "/api/update", 201, 200, new HashMap<>());
        RequestLogEntry entry5 = new RequestLogEntry(timestamp, "host1", "POST", "/api/update", 400, 50, new HashMap<>());

        // Process all logs
        aggregator.processLog(entry1);
        aggregator.processLog(entry2);
        aggregator.processLog(entry3);
        aggregator.processLog(entry4);
        aggregator.processLog(entry5);

        // Get aggregated data
        Map<String, Object> result = aggregator.getAggregatedData();

        // Verify /api/status results
        assertTrue(result.containsKey("/api/status"));

        @SuppressWarnings("unchecked")
        Map<String, Object> statusRouteData = (Map<String, Object>) result.get("/api/status");

        @SuppressWarnings("unchecked")
        Map<String, Object> statusResponseTimes = (Map<String, Object>) statusRouteData.get("response_times");

        assertEquals(100, statusResponseTimes.get("min"));
        assertEquals(150, statusResponseTimes.get("50_percentile"));
        assertEquals(300, statusResponseTimes.get("90_percentile"));
        assertEquals(300, statusResponseTimes.get("95_percentile"));
        assertEquals(300, statusResponseTimes.get("99_percentile"));
        assertEquals(300, statusResponseTimes.get("max"));

        @SuppressWarnings("unchecked")
        Map<String, Object> statusStatusCodes = (Map<String, Object>) statusRouteData.get("status_codes");

        assertEquals(2, statusStatusCodes.get("2XX"));
        assertEquals(0, statusStatusCodes.get("4XX"));
        assertEquals(1, statusStatusCodes.get("5XX"));

        // Verify /api/update results
        assertTrue(result.containsKey("/api/update"));

        @SuppressWarnings("unchecked")
        Map<String, Object> updateRouteData = (Map<String, Object>) result.get("/api/update");

        @SuppressWarnings("unchecked")
        Map<String, Object> updateResponseTimes = (Map<String, Object>) updateRouteData.get("response_times");

        assertEquals(50, updateResponseTimes.get("min"));
        assertEquals(125, updateResponseTimes.get("50_percentile")); // Midpoint between 50 and 200
        assertEquals(200, updateResponseTimes.get("max"));

        @SuppressWarnings("unchecked")
        Map<String, Object> updateStatusCodes = (Map<String, Object>) updateRouteData.get("status_codes");

        assertEquals(1, updateStatusCodes.get("2XX"));
        assertEquals(1, updateStatusCodes.get("4XX"));
        assertEquals(0, updateStatusCodes.get("5XX"));
    }

    @Test
    void testGetAggregatedData_EmptyData() {
        // Get aggregated data without processing any logs
        Map<String, Object> result = aggregator.getAggregatedData();

        // Verify result is empty
        assertTrue(result.isEmpty());
    }
}