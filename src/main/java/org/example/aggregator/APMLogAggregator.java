package org.example.aggregator;

import org.example.model.APMLogEntry;

import java.util.*;

/**
 * Aggregator for APM log entries
 */
public class APMLogAggregator implements LogAggregator<APMLogEntry> {
    // Map of metric name to list of values
    private final Map<String, List<Double>> metricValues = new HashMap<>();

    @Override
    public void processLog(APMLogEntry logEntry) {
        String metric = logEntry.getMetric();
        double value = logEntry.getValue();

        // Add value to the list for this metric
        metricValues.computeIfAbsent(metric, k -> new ArrayList<>()).add(value);
    }

    @Override
    public Map<String, Object> getAggregatedData() {
        Map<String, Object> result = new LinkedHashMap<>();

        // Calculate aggregations for each metric
        for (Map.Entry<String, List<Double>> entry : metricValues.entrySet()) {
            String metric = entry.getKey();
            List<Double> values = entry.getValue();

            // Sort values for calculating median
            Collections.sort(values);

            // Calculate metrics
            double min = values.isEmpty() ? 0 : values.get(0);
            double max = values.isEmpty() ? 0 : values.get(values.size() - 1);
            double sum = values.stream().mapToDouble(Double::doubleValue).sum();
            double avg = values.isEmpty() ? 0 : sum / values.size();
            double median;

            if (values.isEmpty()) {
                median = 0;
            } else if (values.size() % 2 == 0) {
                median = (values.get(values.size() / 2 - 1) + values.get(values.size() / 2)) / 2.0;
            } else {
                median = values.get(values.size() / 2);
            }

            // Create result map for this metric
            Map<String, Object> metricResult = new LinkedHashMap<>();
            metricResult.put("minimum", min);
            metricResult.put("median", median);
            metricResult.put("average", avg);
            metricResult.put("max", max);

            // Add to overall result
            result.put(metric, metricResult);
        }

        return result;
    }
}