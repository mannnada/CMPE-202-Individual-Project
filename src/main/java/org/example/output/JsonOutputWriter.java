package org.example.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Utility class for writing JSON output files
 */
public class JsonOutputWriter {

    /**
     * Write a map of data to a JSON file
     * @param data Map containing the data to write
     * @param outputFile Path to the output file
     * @throws IOException If an I/O error occurs
     */
    public static void writeToFile(Map<String, Object> data, String outputFile) throws IOException {
        // Create empty JSON object if data is null or empty
        if (data == null || data.isEmpty()) {
            data = Map.of();
        }

        // Create parent directories if they don't exist
        File file = new File(outputFile);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        // Write data to file using basic JSON conversion
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(convertMapToJson(data));
        }
        System.out.println("Written output to: " + file.getAbsolutePath());
    }

    /**
     * Convert a Map to a JSON string
     */
    private static String convertMapToJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                sb.append(",\n");
            }
            first = false;

            sb.append("  \"").append(entry.getKey()).append("\": ");
            appendValue(sb, entry.getValue(), 1);
        }

        sb.append("\n}");
        return sb.toString();
    }

    /**
     * Append a value to the JSON string builder
     */
    private static void appendValue(StringBuilder sb, Object value, int indentLevel) {
        String indent = "  ".repeat(indentLevel);

        if (value == null) {
            sb.append("null");
        } else if (value instanceof String) {
            sb.append("\"").append(escapeString((String) value)).append("\"");
        } else if (value instanceof Number || value instanceof Boolean) {
            sb.append(value);
        } else if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> nestedMap = (Map<String, Object>) value;

            if (nestedMap.isEmpty()) {
                sb.append("{}");
                return;
            }

            sb.append("{\n");

            boolean first = true;
            for (Map.Entry<String, Object> entry : nestedMap.entrySet()) {
                if (!first) {
                    sb.append(",\n");
                }
                first = false;

                sb.append(indent).append("  \"").append(entry.getKey()).append("\": ");
                appendValue(sb, entry.getValue(), indentLevel + 1);
            }

            sb.append("\n").append(indent).append("}");
        } else {
            // For any other object type, convert to string
            sb.append("\"").append(escapeString(value.toString())).append("\"");
        }
    }

    /**
     * Escape special characters in strings for JSON
     */
    private static String escapeString(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}