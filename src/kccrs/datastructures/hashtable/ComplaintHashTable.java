package kccrs.datastructures.hashtable;

import kccrs.models.Complaint;
import java.util.LinkedList;

public class ComplaintHashTable {
    private class HashNode {
        String key;
        Complaint complaint;
        HashNode next;

        HashNode(String key, Complaint complaint) {
            this.key = key;
            this.complaint = complaint;
        }
    }

    private int capacity = 100; // default size
    private HashNode[] table;

    public ComplaintHashTable() {
        table = new HashNode[capacity];
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public boolean insert(Complaint complaint) {
        String key = complaint.getId();
        int index = hash(key);
        HashNode node = table[index];

        while (node != null) {
            if (node.key.equals(key))
                return false; // duplicate
            node = node.next;
        }

        HashNode newNode = new HashNode(key, complaint);
        newNode.next = table[index];
        table[index] = newNode;
        return true;
    }

    public Complaint search(String key) {
        int index = hash(key);
        HashNode node = table[index];
        while (node != null) {
            if (node.key.equals(key))
                return node.complaint;
            node = node.next;
        }
        return null;
    }

    public void display() {
        for (int i = 0; i < capacity; i++) {
            HashNode node = table[i];
            while (node != null) {
                System.out.println(node.complaint);
                node = node.next;
            }
        }
    }
}
