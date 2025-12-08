package kccrs.datastructures.queue;

import kccrs.models.Complaint;
import java.util.LinkedList;

public class ComplaintQueue {
    private LinkedList<Complaint> queue;

    public ComplaintQueue() {
        queue = new LinkedList<>();
    }

    public void enqueue(Complaint complaint) {
        queue.addLast(complaint);
    }

    public Complaint dequeue() {
        if (queue.isEmpty()) return null;
        return queue.removeFirst();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void display() {
        for (Complaint c : queue)
            System.out.println(c);
    }

    public int size() {
        return queue.size();
    }
}
