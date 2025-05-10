package org.example;

import org.example.aggregator.APMLogAggregator;
import org.example.aggregator.ApplicationLogAggregator;
import org.example.aggregator.RequestLogAggregator;
import org.example.output.JsonOutputWriter;
import org.example.util.LogProcessor;

import java.io.IOException;

/**
 * Main class for the log parser application
 */
public class Main {
    public static void main(String[] args) {
        // Parse command line arguments
        String inputFile = parseArguments(args);
        if (inputFile == null) {
            System.out.println("Usage: java -jar log-parser.jar --file <filename.txt>");
            return;
        }

        try {
            // Process the log file
            LogProcessor processor = new LogProcessor(inputFile);
            processor.processLogFile();

            // Get aggregators for each log type
            APMLogAggregator apmAggregator = processor.getApmAggregator();
            ApplicationLogAggregator appAggregator = processor.getApplicationAggregator();
            RequestLogAggregator reqAggregator = processor.getRequestAggregator();

            // Write output files
            JsonOutputWriter.writeToFile(apmAggregator.getAggregatedData(), "apm.json");
            JsonOutputWriter.writeToFile(appAggregator.getAggregatedData(), "application.json");
            JsonOutputWriter.writeToFile(reqAggregator.getAggregatedData(), "request.json");

            System.out.println("Log processing completed successfully.");
        } catch (IOException e) {
            System.err.println("Error processing log file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parse command line arguments
     * @param args Command line arguments
     * @return The input file path, or null if not specified
     */
    private static String parseArguments(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("--file")) {
                return args[i + 1];
            }
        }
        return null;
    }
}