package kccrs.datastructures.linkedlist;

import kccrs.models.Complaint;

public class DoublyLinkedList {
    private class Node {
        Complaint complaint;
        Node next;
        Node prev;

        Node(Complaint complaint) {
            this.complaint = complaint;
        }
    }

    private Node head, tail;

    public DoublyLinkedList() {
        head = null;
        tail = null;
    }

    public void add(Complaint complaint) {
        Node node = new Node(complaint);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }

    public void displayForward() {
        Node current = head;
        while (current != null) {
            System.out.println(current.complaint);
            current = current.next;
        }
    }

    public void displayBackward() {
        Node current = tail;
        while (current != null) {
            System.out.println(current.complaint);
            current = current.prev;
        }
    }
}
