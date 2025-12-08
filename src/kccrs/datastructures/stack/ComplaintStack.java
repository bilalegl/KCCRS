package kccrs.datastructures.stack;

import kccrs.models.Complaint;
import java.util.LinkedList;

public class ComplaintStack {
    private LinkedList<Complaint> stack;

    public ComplaintStack() {
        stack = new LinkedList<>();
    }

    public void push(Complaint complaint) {
        stack.addFirst(complaint);
    }

    public Complaint pop() {
        if (stack.isEmpty()) return null;
        return stack.removeFirst();
    }

    public Complaint peek() {
        if (stack.isEmpty()) return null;
        return stack.getFirst();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public void display() {
        for (Complaint c : stack)
            System.out.println(c);
    }

    public int size() {
        return stack.size();
    }
}
