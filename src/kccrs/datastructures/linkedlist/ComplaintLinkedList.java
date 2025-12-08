package kccrs.datastructures.linkedlist;

import java.util.ArrayList;
import java.util.List;   // <-- added for toList()
import kccrs.models.Complaint;       // <-- added for toList()

public class ComplaintLinkedList {

    private class Node {
        Complaint complaint;
        Node next;

        Node(Complaint complaint) {
            this.complaint = complaint;
            this.next = null;
        }
    }

    private Node head;
    private int size;

    public ComplaintLinkedList() {
        head = null;
        size = 0;
    }

    public void add(Complaint complaint) {
        Node newNode = new Node(complaint);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null)
                current = current.next;
            current.next = newNode;
        }
        size++;
    }

    public Complaint searchByID(String id) {
        Node current = head;
        while (current != null) {
            if (current.complaint.getId().equals(id))
                return current.complaint;
            current = current.next;
        }
        return null;
    }

    public boolean deleteByID(String id) {
        if (head == null) return false;

        if (head.complaint.getId().equals(id)) {
            head = head.next;
            size--;
            return true;
        }

        Node current = head;
        while (current.next != null) {
            if (current.next.complaint.getId().equals(id)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void display() {
        Node current = head;
        while (current != null) {
            System.out.println(current.complaint);
            current = current.next;
        }
    }

    public int size() {
        return size;
    }

    // --------------------------------------------------------------------
    // ✅ ADDED METHOD: Converts LinkedList → List<Complaint>
    // --------------------------------------------------------------------
    public List<Complaint> toList() {
        List<Complaint> list = new ArrayList<>();
        Node current = head;

        while (current != null) {
            list.add(current.complaint);
            current = current.next;
        }

        return list;
    }
}
