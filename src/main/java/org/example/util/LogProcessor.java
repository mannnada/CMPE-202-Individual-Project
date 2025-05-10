package org.example.util;

import org.example.aggregator.APMLogAggregator;
import org.example.aggregator.ApplicationLogAggregator;
import org.example.aggregator.RequestLogAggregator;
import org.example.handler.APMLogHandler;
import org.example.handler.ApplicationLogHandler;
import org.example.handler.LogHandler;
import org.example.handler.RequestLogHandler;
import org.example.model.APMLogEntry;
import org.example.model.ApplicationLogEntry;
import org.example.model.LogEntry;
import org.example.model.RequestLogEntry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Main processor for log files
 */
public class LogProcessor {
    private final String inputFile;
    private final List<APMLogEntry> apmLogs = new ArrayList<>();
    private final List<ApplicationLogEntry> applicationLogs = new ArrayList<>();
    private final List<RequestLogEntry> requestLogs = new ArrayList<>();

    private final LogHandler handlerChain;

    /**
     * Create a log processor for the specified input file
     * @param inputFile Path to the input log file
     */
    public LogProcessor(String inputFile) {
        this.inputFile = inputFile;

        // Set up the Chain of Responsibility
        APMLogHandler apmHandler = new APMLogHandler();
        ApplicationLogHandler appHandler = new ApplicationLogHandler();
        RequestLogHandler reqHandler = new RequestLogHandler();

        // Link the handlers
        apmHandler.setNext(appHandler).setNext(reqHandler);

        this.handlerChain = apmHandler;
    }

    /**
     * Process the log file
     * @throws IOException If an I/O error occurs
     */
    public void processLogFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLogLine(line);
            }
        }
    }

    /**
     * Process a single log line
     * @param logLine The log line to process
     */
    private void processLogLine(String logLine) {
        Optional<LogEntry> logEntry = handlerChain.handle(logLine);

        logEntry.ifPresent(entry -> {
            if (entry instanceof APMLogEntry) {
                apmLogs.add((APMLogEntry) entry);
            } else if (entry instanceof ApplicationLogEntry) {
                applicationLogs.add((ApplicationLogEntry) entry);
            } else if (entry instanceof RequestLogEntry) {
                requestLogs.add((RequestLogEntry) entry);
            }
        });
    }

    /**
     * Get aggregated APM log data
     * @return Map containing the aggregated data
     */
    public APMLogAggregator getApmAggregator() {
        APMLogAggregator aggregator = new APMLogAggregator();
        aggregator.processLogs(apmLogs);
        return aggregator;
    }

    /**
     * Get aggregated Application log data
     * @return Map containing the aggregated data
     */
    public ApplicationLogAggregator getApplicationAggregator() {
        ApplicationLogAggregator aggregator = new ApplicationLogAggregator();
        aggregator.processLogs(applicationLogs);
        return aggregator;
    }

    /**
     * Get aggregated Request log data
     * @return Map containing the aggregated data
     */
    public RequestLogAggregator getRequestAggregator() {
        RequestLogAggregator aggregator = new RequestLogAggregator();
        aggregator.processLogs(requestLogs);
        return aggregator;
    }
}