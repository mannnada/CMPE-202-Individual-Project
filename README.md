# CMPE-202-Individual-Project
# Log Parser Application

## Overview

This Log Parser application is a command-line tool that parses, classifies, and aggregates different types of log entries from a text file. It processes Application Performance Metrics (APM), Application, and Request logs, calculating useful statistics for each type and outputting them to JSON files.

## Problem Statement

Modern applications generate diverse logs containing valuable information. Analyzing these logs manually is time-consuming and error-prone. This application solves the problem by:

1. Automatically parsing log entries with different formats
2. Categorizing them by type
3. Calculating relevant statistics for each log type
4. Generating structured, readable JSON output for further analysis

The application is designed to be extensible for supporting additional log types and formats in the future.

## Design Patterns Used

### 1. Factory Pattern

**Purpose**: Create appropriate log entry objects based on log line content.

**Implementation**: `LogEntryFactory` examines log lines and creates the appropriate log entry object based on the present attributes.

**Benefits**:
- Centralizes the complex creation logic in one place
- Decouples log parsing from processing logic
- Simplifies adding new log types in the future

### 2. Strategy Pattern

**Purpose**: Define different processing algorithms for different log types.

**Implementation**: `LogProcessor` interface with specialized implementations (`APMLogProcessor`, `ApplicationLogProcessor`, `RequestLogProcessor`).

**Benefits**:
- Separates processing logic for each log type
- Enables adding new log types without modifying existing code
- Makes it easy to test each processor in isolation

### 3. Command Pattern

**Purpose**: Encapsulate aggregation operations.

**Implementation**: Each `LogProcessor` implementation contains its own aggregation logic.

## Features

- Parses three types of log entries:
  - **APM Logs**: Tracks performance metrics (CPU, memory, disk usage, etc.)
  - **Application Logs**: Records application events (info, error, warning, etc.)
  - **Request Logs**: Captures HTTP request details

- Calculates various aggregations:
  - For APM logs: minimum, median, average, and maximum values
  - For Application logs: counts by severity level
  - For Request logs: response time percentiles and status code counts

- Outputs structured JSON files:
  - `apm.json` for APM metrics
  - `application.json` for log level counts
  - `request.json` for response time statistics and status code counts

- Handles invalid log entries gracefully by ignoring them

LogParserApplication/
├── .idea/                   # IntelliJ IDEA configuration files
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── org/
│   │   │   │   ├── example/
│   │   │   │   │   ├── Main.java              # Entry point of the application
│   │   │   │   │   ├── model/                 # Domain model classes
│   │   │   │   │   │   ├── LogEntry.java      # Base class for all log entries
│   │   │   │   │   │   ├── APMLogEntry.java   # APM log entry model
│   │   │   │   │   │   ├── ApplicationLogEntry.java  # Application log entry model
│   │   │   │   │   │   ├── RequestLogEntry.java      # Request log entry model
│   │   │   │   │   ├── parser/                # Strategy pattern implementation
│   │   │   │   │   │   ├── LogParser.java     # Parser interface
│   │   │   │   │   │   ├── APMLogParser.java  # APM log parser
│   │   │   │   │   │   ├── ApplicationLogParser.java # Application log parser
│   │   │   │   │   │   ├── RequestLogParser.java     # Request log parser
│   │   │   │   │   │   ├── LogParserFactory.java     # Factory for parsers
│   │   │   │   │   ├── handler/               # Chain of Responsibility implementation
│   │   │   │   │   │   ├── LogHandler.java    # Base handler class
│   │   │   │   │   │   ├── APMLogHandler.java # APM log handler
│   │   │   │   │   │   ├── ApplicationLogHandler.java # Application log handler
│   │   │   │   │   │   ├── RequestLogHandler.java     # Request log handler
│   │   │   │   │   ├── aggregator/           # Log aggregation logic
│   │   │   │   │   │   ├── LogAggregator.java # Aggregator interface
│   │   │   │   │   │   ├── APMLogAggregator.java     # APM log aggregator
│   │   │   │   │   │   ├── ApplicationLogAggregator.java # Application log aggregator
│   │   │   │   │   │   ├── RequestLogAggregator.java     # Request log aggregator
│   │   │   │   │   ├── output/               # Output generation
│   │   │   │   │   │   ├── JsonOutputWriter.java     # JSON file writer
│   │   │   │   │   ├── util/                 # Utility classes
│   │   │   │   │   │   ├── LogProcessor.java # Main log processing logic
│   │   │   │   │   │   ├── Utils.java        # Utility methods
│   │   ├── resources/
│   │   │   ├── input.txt                     # Sample log file
│   ├── test/
│   │   ├── java/
│   │   │   ├── org/
│   │   │   │   ├── example/
│   │   │   │   │   ├── LogProcessorTest.java # Tests for LogProcessor
│   │   │   │   │   ├── parser/               # Parser tests
│   │   │   │   │   │   ├── APMLogParserTest.java
│   │   │   │   │   │   ├── ApplicationLogParserTest.java
│   │   │   │   │   │   ├── RequestLogParserTest.java
│   │   │   │   │   ├── aggregator/           # Aggregator tests
│   │   │   │   │   │   ├── APMLogAggregatorTest.java
│   │   │   │   │   │   ├── ApplicationLogAggregatorTest.java
│   │   │   │   │   │   ├── RequestLogAggregatorTest.java
├── target/                  # Compiled output
├── .gitignore               # Git ignore file
├── pom.xml                  # Maven configuration file
├── README.md                # This file
```

## Getting Started

### Prerequisites

- Java JDK 11 or higher
- Maven

### Building the Application

```bash
mvn clean package
```

### Running the Application

```bash
java -jar target/log-parser-app-1.0-SNAPSHOT-jar-with-dependencies.jar --file input.txt
```

Replace `input.txt` with the path to your log file.

### Sample Log File Format

```
timestamp=2024-02-24T16:22:15Z metric=cpu_usage_percent host=webserver1 value=72
timestamp=2024-02-24T16:22:20Z level=INFO message="Scheduled maintenance starting" host=webserver1
timestamp=2024-02-24T16:22:25Z request_method=POST request_url="/api/update" response_status=202 response_time_ms=200 host=webserver1
```

### Running Tests

```bash
mvn test
```

## Sample Output

### apm.json
```json
{
    "cpu_usage_percent": {
        "minimum": 65.0,
        "median": 68.5,
        "average": 68.5,
        "max": 72.0
    },
    "memory_usage_percent": {
        "minimum": 85.0,
        "median": 87.5,
        "average": 87.5,
        "max": 90.0
    }
}
```

### application.json
```json
{
    "INFO": 3,
    "ERROR": 2,
    "DEBUG": 1,
    "WARNING": 1
}
```

### request.json
```json
{
    "/api/status": {
        "response_times": {
            "min": 100,
            "50_percentile": 150,
            "90_percentile": 300,
            "95_percentile": 300,
            "99_percentile": 300,
            "max": 300
        },
        "status_codes": {
            "2XX": 3,
            "4XX": 0,
            "5XX": 1
        }
    }
}
```

## Future Enhancements

- Support for more log formats
- Additional statistical calculations
- Live log monitoring
- Graphical visualizations
- Integration with log management systems

## Conclusion

This Log Parser application demonstrates the effective use of design patterns to create a flexible, extensible solution for log processing. By following object-oriented design principles, the application can be easily modified to support new log types and formats in the future.
