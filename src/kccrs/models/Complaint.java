package kccrs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Complaint {
    private String id;
    private String citizenName;
    private String area;          // e.g., Korangi, Saddar
    private String type;          // Water, Garbage, Electricity, Gas, Traffic
    private String severity;      // Low, Medium, High, Critical
    private LocalDateTime timestamp;

    public Complaint(String id, String citizenName, String area, String type, String severity, LocalDateTime timestamp) {
        this.id = id;
        this.citizenName = citizenName;
        this.area = area;
        this.type = type;
        this.severity = severity;
        this.timestamp = timestamp;
    }

    // Getters
    public String getId() { return id; }
    public String getCitizenName() { return citizenName; }
    public String getArea() { return area; }
    public String getType() { return type; }
    public String getSeverity() { return severity; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // For CSV saving
    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.join(",", id, citizenName, area, type, severity, timestamp.format(formatter));
    }

    // Display for console / GUI
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("ID: %s | Name: %s | Area: %s | Type: %s | Severity: %s | Time: %s",
                id, citizenName, area, type, severity, timestamp.format(formatter));
    }

    // Severity priority mapping for heap (lower number = higher priority)
    public int getPriority() {
        return switch (severity.toLowerCase()) {
            case "critical" -> 1;
            case "high" -> 2;
            case "medium" -> 3;
            case "low" -> 4;
            default -> 5; // unknown severity
        };
    }
}
