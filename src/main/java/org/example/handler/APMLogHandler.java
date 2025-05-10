package org.example.handler;

import org.example.parser.APMLogParser;

/**
 * Handler for APM logs (Chain of Responsibility implementation)
 */
public class APMLogHandler extends LogHandler {
    public APMLogHandler() {
        super(new APMLogParser());
    }
}