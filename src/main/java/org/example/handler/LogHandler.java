package org.example.handler;

import org.example.model.LogEntry;
import org.example.parser.LogParser;

import java.util.Optional;

/**
 * Abstract base class for log handlers (Chain of Responsibility pattern)
 */
public abstract class LogHandler {
    private LogHandler nextHandler;
    private final LogParser parser;

    public LogHandler(LogParser parser) {
        this.parser = parser;
    }

    /**
     * Set the next handler in the chain
     * @param nextHandler The next handler
     * @return The next handler (for method chaining)
     */
    public LogHandler setNext(LogHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    /**
     * Process the log line and pass it to the next handler if this handler can't handle it
     * @param logLine The log line to process
     * @return Optional containing the LogEntry if handled, empty Optional otherwise
     */
    public Optional<LogEntry> handle(String logLine) {
        if (parser.canParse(logLine)) {
            return parser.parse(logLine);
        } else if (nextHandler != null) {
            return nextHandler.handle(logLine);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get the parser associated with this handler
     * @return The log parser
     */
    public LogParser getParser() {
        return parser;
    }
}