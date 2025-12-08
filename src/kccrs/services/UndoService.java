package kccrs.services;

import java.util.ArrayList;
import java.util.List;
import kccrs.datastructures.stack.ComplaintStack;
import kccrs.models.Complaint;

public class UndoService {

    private ComplaintStack undoStack;

    public UndoService(ComplaintStack stack) {
        this.undoStack = stack;
    }

    // Undo last operation
    public Complaint undoLast() {
        if (undoStack.isEmpty()) {
            System.out.println("No operations to undo.");
            return null;
        }

        Complaint complaint = undoStack.pop();
        System.out.println("Undo successful! Complaint ID: " + complaint.getId());
        return complaint;
    }

    // Display complaints in stack in reverse order using recursion
    public void displayReverse() {
        List<Complaint> tempList = new ArrayList<>();

        /**
         * ❗FIXED: popping elements empties your stack permanently.
         * I backed them up in tempList and restore later.
         */

        while (!undoStack.isEmpty()) {
            tempList.add(undoStack.pop());
        }

        // recursive print
        printReverse(tempList, tempList.size() - 1);

        // restore stack
        for (int i = tempList.size() - 1; i >= 0; i--) { // <-- FIXED: must restore in correct order
            undoStack.push(tempList.get(i));              // <-- FIXED
        }
    }

    private void printReverse(List<Complaint> list, int index) {
        if (index < 0) return;
        System.out.println(list.get(index));
        printReverse(list, index - 1);
    }

    // Peek without removing
    public Complaint peekLast() {
        return undoStack.peek();
    }
}
