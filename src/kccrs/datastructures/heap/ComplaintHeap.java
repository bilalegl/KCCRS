package kccrs.datastructures.heap;

import kccrs.models.Complaint;
import java.util.ArrayList;

public class ComplaintHeap {
    private ArrayList<Complaint> heap;

    public ComplaintHeap() {
        heap = new ArrayList<>();
    }

    private void swap(int i, int j) {
        Complaint temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public void push(Complaint complaint) {
        heap.add(complaint);
        heapifyUp(heap.size() - 1);
    }

    public Complaint pop() {
        if (heap.isEmpty()) return null;
        Complaint top = heap.get(0);
        Complaint last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        return top;
    }

    public Complaint peek() {
        if (heap.isEmpty()) return null;
        return heap.get(0);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void display() {
        for (Complaint c : heap)
            System.out.println(c);
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index).getPriority() < heap.get(parent).getPriority()) {
                swap(index, parent);
                index = parent;
            } else break;
        }
    }

    private void heapifyDown(int index) {
        int left, right, smallest;
        while (true) {
            left = 2 * index + 1;
            right = 2 * index + 2;
            smallest = index;

            if (left < heap.size() && heap.get(left).getPriority() < heap.get(smallest).getPriority())
                smallest = left;
            if (right < heap.size() && heap.get(right).getPriority() < heap.get(smallest).getPriority())
                smallest = right;

            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else break;
        }
    }

    public int size() {
        return heap.size();
    }
}
