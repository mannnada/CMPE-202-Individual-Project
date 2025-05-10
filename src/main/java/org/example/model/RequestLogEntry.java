package org.example.model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents a Request log entry with HTTP request details
 */
public class RequestLogEntry extends LogEntry {
    private String requestMethod;
    private String requestUrl;
    private int responseStatus;
    private int responseTimeMs;

    public RequestLogEntry(LocalDateTime timestamp, String host, String requestMethod, String requestUrl,
                           int responseStatus, int responseTimeMs, Map<String, String> rawData) {
        super(timestamp, host, rawData);
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
        this.responseStatus = responseStatus;
        this.responseTimeMs = responseTimeMs;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public int getResponseTimeMs() {
        return responseTimeMs;
    }

    /**
     * Returns the status code category (2XX, 4XX, 5XX)
     */
    public String getStatusCodeCategory() {
        if (responseStatus >= 200 && responseStatus < 300) {
            return "2XX";
        } else if (responseStatus >= 400 && responseStatus < 500) {
            return "4XX";
        } else if (responseStatus >= 500 && responseStatus < 600) {
            return "5XX";
        } else {
            return "Other";
        }
    }

    @Override
    public String toString() {
        return "RequestLogEntry{" +
                "requestMethod='" + requestMethod + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", responseStatus=" + responseStatus +
                ", responseTimeMs=" + responseTimeMs +
                "} " + super.toString();
    }
}