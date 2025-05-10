package org.example.model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents an Application Performance Metric log entry
 */
public class APMLogEntry extends LogEntry {
    private String metric;
    private double value;

    public APMLogEntry(LocalDateTime timestamp, String host, String metric, double value, Map<String, String> rawData) {
        super(timestamp, host, rawData);
        this.metric = metric;
        this.value = value;
    }

    public String getMetric() {
        return metric;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "APMLogEntry{" +
                "metric='" + metric + '\'' +
                ", value=" + value +
                "} " + super.toString();
    }
}