package org.example.handler;

import org.example.parser.RequestLogParser;

/**
 * Handler for Request logs (Chain of Responsibility implementation)
 */
public class RequestLogHandler extends LogHandler {
    public RequestLogHandler() {
        super(new RequestLogParser());
    }
}