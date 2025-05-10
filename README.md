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

**Benefits**:
- Decouples aggregation operations from invokers
- Enables different aggregation strategies for different log types
- Makes it easy to add new aggregation calculations

## Consequences of Using These Patterns

### Advantages

1. **Extensibility**: New log types can be added without modifying existing code
2. **Separation of Concerns**: Each class has a clear, single responsibility
3. **Testability**: Components can be tested independently
4. **Maintainability**: Changes to one log type don't affect others
5. **Flexibility**: Different processing strategies can be applied to different log types

### Disadvantages

1. **Increased Complexity**: More classes and interfaces to manage
2. **Initial Development Overhead**: More time to set up the architecture
3. **Potential Performance Impact**: Additional object creation and method calls

## Class Diagram

```
+-------------------+         +-------------------+
| LogParserApp      |         | LogEntryFactory   |
+-------------------+         +-------------------+
| - logProcessors   |         | + createLogEntry()|
| - outputDir       |<>-------+ - patterns        |
+-------------------+         +-------------------+
| + main()          |                 ^
| + parseFile()     |                 |
| + writeOutput()   |        +--------+--------+
+-------------------+        |                 |
         |                   |                 |
         |             +-----+------+    +-----+------+
         |             | APMLogEntry|    |AppLogEntry |
         |             +------------+    +------------+
         |             | - timestamp|    | - timestamp|
         |             | - metric   |    | - level    |
         |             | - host     |    | - message  |
         |             | - value    |    | - host     |
         |             +------------+    +------------+
         |                   ^                ^
         |                   |                |
         |                   |                |
         |                   |        +-------+------+
         |                   |        |RequestLogEntry|
         |                   |        +--------------+
         v                   |        | - timestamp  |
+-------------------+        |        | - method     |
| LogProcessor      |<-------+        | - url        |
+-------------------+                 | - status     |
| + process()       |                 | - responseTime|
+-------------------+                 | - host       |
         ^                            +--------------+
         |
+--------+--------+
|                 |
+--------+        |        +--------+
|APMLogProcessor  |        |AppLogProcessor|
+----------------+         +---------------+
| - metrics      |         | - counts      |
+----------------+         +---------------+
| + process()    |         | + process()   |
| + aggregate()  |         | + aggregate() |
+----------------+         +---------------+
         |                        |
         |                        |
         |                +-------+------+
         |                |RequestLogProc|
         |                +--------------+
         |                | - routes     |
         |                +--------------+
         |                | + process()  |
         |                | + aggregate()|
         |                +--------------+
```

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

## Project Structure

```
log-parser-app/
│
├── pom.xml                           # Maven configuration file
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── logparser/
│   │   │           ├── LogType.java              # Enum of log types
│   │   │           ├── LogEntry.java             # Base interface for log entries
│   │   │           ├── APMLogEntry.java          # APM log entry implementation
│   │   │           ├── ApplicationLogEntry.java  # Application log entry implementation
│   │   │           ├── RequestLogEntry.java      # Request log entry implementation
│   │   │           ├── LogEntryFactory.java      # Factory for creating log entries
│   │   │           ├── LogProcessor.java         # Interface for log processors
│   │   │           ├── APMLogProcessor.java      # APM log processor implementation
│   │   │           ├── ApplicationLogProcessor.java # Application log processor implementation
│   │   │           ├── RequestLogProcessor.java  # Request log processor implementation
│   │   │           └── LogParserApp.java         # Main application class
│   │   │
│   │   └── resources/                # For configuration files (if needed)
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── logparser/
│                   └── LogParserAppTest.java  # JUnit tests
│
├── sample/
│   └── input.txt                     # Sample input file for testing
│
└── output/                           # Directory for output files
    ├── apm.json                      # Output file for APM logs
    ├── application.json              # Output file for Application logs
    └── request.json                  # Output file for Request logs
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
