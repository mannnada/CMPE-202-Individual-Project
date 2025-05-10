package org.example.aggregator;

import org.example.model.RequestLogEntry;

import java.util.*;

/**
 * Aggregator for Request log entries
 */
public class RequestLogAggregator implements LogAggregator<RequestLogEntry> {
    // Map of API route to response times
    private final Map<String, List<Integer>> responseTimesByRoute = new HashMap<>();

    // Map of API route to status code counts
    private final Map<String, Map<String, Integer>> statusCodesByRoute = new HashMap<>();

    @Override
    public void processLog(RequestLogEntry logEntry) {
        String route = logEntry.getRequestUrl();
        int responseTime = logEntry.getResponseTimeMs();
        String statusCategory = logEntry.getStatusCodeCategory();

        // Add response time to the list for this route
        responseTimesByRoute.computeIfAbsent(route, k -> new ArrayList<>()).add(responseTime);

        // Increment count for this status category for this route
        Map<String, Integer> routeStatusCounts = statusCodesByRoute.computeIfAbsent(route, k -> new HashMap<>());
        routeStatusCounts.put(statusCategory, routeStatusCounts.getOrDefault(statusCategory, 0) + 1);
    }

    @Override
    public Map<String, Object> getAggregatedData() {
        Map<String, Object> result = new LinkedHashMap<>();

        // Calculate aggregations for each route
        for (String route : responseTimesByRoute.keySet()) {
            Map<String, Object> routeData = new LinkedHashMap<>();

            // Calculate response time statistics
            List<Integer> responseTimes = responseTimesByRoute.get(route);
            Collections.sort(responseTimes);

            Map<String, Object> responseTimeStats = calculateResponseTimeStats(responseTimes);
            routeData.put("response_times", responseTimeStats);

            // Get status code counts
            Map<String, Integer> statusCounts = statusCodesByRoute.getOrDefault(route, new HashMap<>());
            Map<String, Object> statusData = new LinkedHashMap<>();

            // Ensure all status categories exist in the output
            statusData.put("2XX", statusCounts.getOrDefault("2XX", 0));
            statusData.put("4XX", statusCounts.getOrDefault("4XX", 0));
            statusData.put("5XX", statusCounts.getOrDefault("5XX", 0));

            routeData.put("status_codes", statusData);

            // Add route data to result
            result.put(route, routeData);
        }

        return result;
    }

    /**
     * Calculate response time statistics (min, percentiles, max)
     * @param responseTimes Sorted list of response times
     * @return Map containing the statistics
     */
    private Map<String, Object> calculateResponseTimeStats(List<Integer> responseTimes) {
        Map<String, Object> stats = new LinkedHashMap<>();

        if (responseTimes.isEmpty()) {
            stats.put("min", 0);
            stats.put("50_percentile", 0);
            stats.put("90_percentile", 0);
            stats.put("95_percentile", 0);
            stats.put("99_percentile", 0);
            stats.put("max", 0);
            return stats;
        }

        int min = responseTimes.get(0);
        int max = responseTimes.get(responseTimes.size() - 1);
        int p50 = getPercentile(responseTimes, 50);
        int p90 = getPercentile(responseTimes, 90);
        int p95 = getPercentile(responseTimes, 95);
        int p99 = getPercentile(responseTimes, 99);

        stats.put("min", min);
        stats.put("50_percentile", p50);
        stats.put("90_percentile", p90);
        stats.put("95_percentile", p95);
        stats.put("99_percentile", p99);
        stats.put("max", max);

        return stats;
    }

    /**
     * Calculate the specified percentile from a sorted list
     * @param sortedValues Sorted list of values
     * @param percentile Percentile to calculate (0-100)
     * @return The percentile value
     */
    private int getPercentile(List<Integer> sortedValues, int percentile) {
        if (sortedValues.isEmpty()) {
            return 0;
        }

        int index = (int) Math.ceil(percentile / 100.0 * sortedValues.size()) - 1;
        index = Math.max(0, Math.min(sortedValues.size() - 1, index));
        return sortedValues.get(index);
    }
}