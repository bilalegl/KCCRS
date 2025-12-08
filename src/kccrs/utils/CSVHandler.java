package kccrs.utils;

import kccrs.models.Complaint;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {

    public static void saveComplaints(List<Complaint> complaints, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Complaint c : complaints) {
                writer.write(c.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }

    public static List<Complaint> loadComplaints(String filename) {
        List<Complaint> complaints = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) return complaints;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                String id = parts[0];
                String name = parts[1];
                String area = parts[2];
                String type = parts[3];
                String severity = parts[4];
                LocalDateTime timestamp = TimeUtils.parse(parts[5]);

                complaints.add(new Complaint(id, name, area, type, severity, timestamp));
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }

        return complaints;
    }
}
