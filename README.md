# Karachi City Complaint & Response System (KCCRS)

A small desktop application demonstrating core data structures (LinkedList, HashTable, Queue, Stack, Heap) and sorting algorithms (QuickSort, MergeSort, Insertion Sort) to manage citizen complaints for municipal authorities.

**Author:** :Muhammad Bilal
**Context:** Solution prototype for automating complaint recording, prioritization, processing, undo operations and simple reporting (CSV-backed). :

---

## Project goals / summary
KCCRS provides:
- Online registration UI for citizen complaints.
- Data-structure-backed storage and processing:
  - FIFO processing via a `ComplaintQueue`.
  - Priority processing via `ComplaintHeap` (severity-based).
  - Undo via `ComplaintStack`.
  - Fast lookup via `ComplaintHashTable`.
  - Full list via `ComplaintLinkedList`.
- Daily reporting with sorting by area, severity, and time.
- CSV persistence for active and processed complaints.

---

## Features
- GUI (JavaFX) with tabs for Add, Process, Undo, and Report.
- Add complaint (ID auto-generated), process next (FIFO) or urgent (priority), undo last operation.
- Reporting: sort by Area (QuickSort), Severity (MergeSort), Time (Insertion Sort).
- CSV-based persistence: `data/complaints.csv` (active) and `data/processed.csv` (processed).

---

## Project structure (high level)

kccrs/
├─ src/main/java/kccrs/
│ ├─ Main.java
│ ├─ models/Complaint.java
│ ├─ services/
│ │ ├─ ComplaintService.java
│ │ ├─ ReportService.java
│ │ └─ UndoService.java
│ ├─ datastructures/
│ │ ├─ linkedlist/ComplaintLinkedList.java
│ │ ├─ linkedlist/DoublyLinkedList.java
│ │ ├─ queue/ComplaintQueue.java
│ │ ├─ stack/ComplaintStack.java
│ │ ├─ heap/ComplaintHeap.java
│ │ └─ hashtable/ComplaintHashTable.java
│ └─ utils/
│ ├─ CSVHandler.java
│ ├─ IDGenerator.java
│ └─ TimeUtils.java
├─ data/
│ ├─ complaints.csv
│ └─ processed.csv
└─ resources/
└─ styles.css


---

## Requirements
- Java 11+ (recommended Java 17+)  
- JavaFX SDK (matching your JDK) or run via an IDE that bundles JavaFX (IntelliJ/Eclipse)  
- (Optional) Maven or Gradle to create a proper build

---

## Quick start (IDE)
1. Import as a Java project in IntelliJ / Eclipse.
2. Add JavaFX libraries to the module classpath or configure VM args:
--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml
3. Run `kccrs.Main`.

---

## Build & run (recommended: Maven)
If you add Maven, use `org.openjfx:javafx` artifacts. Example (high level):
```bash
# Build (after creating pom.xml and adding JavaFX deps)
mvn clean package

# Run (example)
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/kccrs.jar
```

Data format (CSV)

Each line in data/complaints.csv / data/processed.csv:

ID,CitizenName,Area,Type,Severity,yyyy-MM-dd HH:mm:ss

Example:

CMP-1A2B3C4D,Ali Khan,Gulshan,Water,High,2026-02-26 11:12:00

