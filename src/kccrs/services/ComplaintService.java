package kccrs.services;

import java.time.LocalDateTime;
import java.util.List;
import kccrs.datastructures.hashtable.ComplaintHashTable;
import kccrs.datastructures.heap.ComplaintHeap;
import kccrs.datastructures.linkedlist.ComplaintLinkedList;
import kccrs.datastructures.queue.ComplaintQueue;
import kccrs.datastructures.stack.ComplaintStack;
import kccrs.models.Complaint;
import kccrs.utils.CSVHandler;
import kccrs.utils.IDGenerator;

public class ComplaintService {

    private ComplaintLinkedList complaintList;
    private ComplaintHashTable hashTable;
    private ComplaintQueue processingQueue;
    private ComplaintHeap priorityQueue;
    private ComplaintStack undoStack;

    private final String complaintFile = "data/complaints.csv";
    private final String processedFile = "data/processed.csv";

    public ComplaintService() {
        complaintList = new ComplaintLinkedList();
        hashTable = new ComplaintHashTable();
        processingQueue = new ComplaintQueue();
        priorityQueue = new ComplaintHeap();
        undoStack = new ComplaintStack();

        loadComplaints(); // Load data from CSV on startup
    }

    // Add new complaint
    public boolean addComplaint(String citizenName, String area, String type, String severity) {
        String id = IDGenerator.generateID();
        LocalDateTime now = LocalDateTime.now();
        Complaint complaint = new Complaint(id, citizenName, area, type, severity, now);

        // Check for duplicates
        if (!hashTable.insert(complaint)) {
            System.out.println("Duplicate complaint ID detected!");
            return false;
        }

        complaintList.add(complaint);
        processingQueue.enqueue(complaint);
        priorityQueue.push(complaint);
        undoStack.push(complaint);

        saveComplaints();
        System.out.println("Complaint added successfully! ID: " + id);
        return true;
    }

    // Search by ID
    public Complaint searchByID(String id) {
        return complaintList.searchByID(id);
    }

    // Process next complaint (FIFO)
    public Complaint processNextComplaint() {
        Complaint complaint = processingQueue.dequeue();
        if (complaint != null) {
            undoStack.push(complaint);
            saveProcessedComplaint(complaint);
            deleteFromActive(complaint);
        }
        return complaint;
    }

    // Process next urgent complaint (Priority Queue)
    public Complaint processNextUrgentComplaint() {
        Complaint complaint = priorityQueue.pop();
        if (complaint != null) {
            undoStack.push(complaint);
            saveProcessedComplaint(complaint);
            deleteFromActive(complaint);
        }
        return complaint;
    }

    // Display all complaints
    public void displayAllComplaints() {
        complaintList.display();
    }

    // Undo last added/processed complaint
    public Complaint undoLastOperation() {
        Complaint complaint = undoStack.pop();
        if (complaint != null) {
            complaintList.add(complaint);
            processingQueue.enqueue(complaint);
            priorityQueue.push(complaint);
            hashTable.insert(complaint);
            System.out.println("Undo successful for complaint ID: " + complaint.getId());
        } else {
            System.out.println("No operation to undo.");
        }
        saveComplaints();
        return complaint;
    }

    // Load complaints from CSV
    private void loadComplaints() {
        List<Complaint> complaints = CSVHandler.loadComplaints(complaintFile);
        for (Complaint c : complaints) {
            complaintList.add(c);
            processingQueue.enqueue(c);
            priorityQueue.push(c);
            hashTable.insert(c);
        }
    }

    // Save complaints to CSV
    private void saveComplaints() {

        /**
         * ❗ FIXED: Your previous code had wrong logic.
         * You said “LinkedList does not expose iterator” but that is YOUR custom LinkedList.
         * So I rewrote save logic to use complaintList.toList()
         * (You MUST implement toList() in ComplaintLinkedList).
         */

        List<Complaint> list = complaintList.toList(); // <-- FIXED
        CSVHandler.saveComplaints(list, complaintFile);
    }

    // Save processed complaint separately
    private void saveProcessedComplaint(Complaint complaint) {
        List<Complaint> processed = CSVHandler.loadComplaints(processedFile);
        processed.add(complaint);
        CSVHandler.saveComplaints(processed, processedFile);
    }

    // Delete complaint from active data structures
    private void deleteFromActive(Complaint complaint) {
        complaintList.deleteByID(complaint.getId());
        // HashTable delete not implemented — okay
    }

    // Fallback: extract all active complaints if needed
    public List<Complaint> getAllActiveComplaints() {
        return complaintList.toList();
    }

    // --------------------------------------------------------------------
// ❗ FIXED: Added getter so Swing/UI can access UndoService
// --------------------------------------------------------------------
public ComplaintStack getUndoStack() {
    return undoStack;
}

}
