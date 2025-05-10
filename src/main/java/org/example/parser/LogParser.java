package org.example.parser;

import org.example.model.LogEntry;

import java.util.Optional;

/**
 * Interface for log parsers implementing the Strategy pattern
 */
public interface LogParser {
    /**
     * Parse a log line and return the appropriate LogEntry object if the line matches this parser's format
     * @param logLine the log line to parse
     * @return Optional containing the parsed LogEntry if successful, empty Optional otherwise
     */
    Optional<LogEntry> parse(String logLine);

    /**
     * Check if this parser can handle the given log line
     * @param logLine the log line to check
     * @return true if this parser can handle the log line, false otherwise
     */
    boolean canParse(String logLine);
}