package kccrs.services;

import java.util.ArrayList;
import java.util.List;
import kccrs.models.Complaint;

public class ReportService {

    private List<Complaint> complaints;

    public ReportService(List<Complaint> complaints) {
        this.complaints = new ArrayList<>(complaints); // Copy to avoid modifying original list
    }

    
    /** 
     * Sort by Area using QuickSort
     */
    public void sortByArea() {
        quickSortByArea(0, complaints.size() - 1);
    }

    private void quickSortByArea(int low, int high) {
        if (low < high) {
            int pi = partitionByArea(low, high);
            quickSortByArea(low, pi - 1);
            quickSortByArea(pi + 1, high);
        }
    }

    private int partitionByArea(int low, int high) {
        String pivot = complaints.get(high).getArea();
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (complaints.get(j).getArea().compareToIgnoreCase(pivot) <= 0) {
                i++;
                swap(i, j);
            }
        }
        swap(i + 1, high);
        return i + 1;
    }

    private void swap(int i, int j) {
    Complaint temp = complaints.get(i);
    complaints.set(i, complaints.get(j));
    complaints.set(j, temp);
}

    /** 
     * Sort by Severity using MergeSort
     */
    public void sortBySeverity() {
        complaints = mergeSortBySeverity(complaints);
    }

    private List<Complaint> mergeSortBySeverity(List<Complaint> list) {
        if (list.size() <= 1) return list;

        int mid = list.size() / 2;
        List<Complaint> left = mergeSortBySeverity(list.subList(0, mid));
        List<Complaint> right = mergeSortBySeverity(list.subList(mid, list.size()));

        return mergeSeverity(left, right);
    }

    private List<Complaint> mergeSeverity(List<Complaint> left, List<Complaint> right) {
        List<Complaint> merged = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).getPriority() <= right.get(j).getPriority()) {
                merged.add(left.get(i++));
            } else {
                merged.add(right.get(j++));
            }
        }
        while (i < left.size()) merged.add(left.get(i++));
        while (j < right.size()) merged.add(right.get(j++));
        return merged;
    }

    /** 
     * Sort by Time using Insertion Sort
     */
    public void sortByTime() {
        for (int i = 1; i < complaints.size(); i++) {
            Complaint key = complaints.get(i);
            int j = i - 1;
            while (j >= 0 && complaints.get(j).getTimestamp().isAfter(key.getTimestamp())) {
                complaints.set(j + 1, complaints.get(j));
                j--;
            }
            complaints.set(j + 1, key);
        }
    }

    /**
     * Display report
     */
    public void displayReport() {
        System.out.println("---------- DAILY REPORT ----------");
        for (Complaint c : complaints) {
            System.out.println(c);
        }
        System.out.println("Total Complaints: " + complaints.size());
        System.out.println("---------------------------------");
    }

    /**
     * Get the report as list (optional)
     */
    public List<Complaint> getReport() {
        return complaints;
    }
}
