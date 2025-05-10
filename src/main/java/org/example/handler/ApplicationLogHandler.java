package org.example.handler;

import org.example.parser.ApplicationLogParser;

/**
 * Handler for Application logs (Chain of Responsibility implementation)
 */
public class ApplicationLogHandler extends LogHandler {
    public ApplicationLogHandler() {
        super(new ApplicationLogParser());
    }
}