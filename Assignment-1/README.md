# SetForge: Generic Set ADT Investigation (README.md)

An engineering implementation of a generic Set Abstract Data Type (ADT) evaluated across two backing structures: a contiguous, dynamic resizable array and an acyclic singly linked list. Built completely from scratch without Java Collection framework dependencies.

---

## 📋 System Configuration & Specifications

*   **Java Version:** Java 17 (or compatible JDK versions)
*   **Null Policy:** Strict Null Rejection. Any attempt to supply a `null` reference to `add()`, `remove(T)`, or `contains(T)` will instantly trigger an `IllegalArgumentException`.
*   **Empty Remove Policy:** Return `null`. Invoking the unspecified `remove()` method on an empty set safely yields a `null` signal instead of halting the system with a runtime exception.

---

## 🗺️ Project File Map

```text
delaneyhickey-project1/
├── README.md                 # Project Overview, Execution Guide, and Manifest
├── DESIGN.md                 # Representation Invariants, Policies, and Pointer Traces
├── COMPLEXITY.md             # Theoretical Asymptotic Runtime and Reasoning Analysis
└── src/
    ├── main/
        └── java/
            └── com/
                └── setforge/
                    ├── SetInterface.java       # Generic ADT Structural Contract
                    ├── ResizableArraySet.java  # Array representation (No loitering)
                    └── LinkedSet.java         # Node-link representation (Acyclic)
    └── test/
        └── java/
            └── com/
                └── setforge/
                    ├── ArraySetTest.java       # Part 4 Execution Test (Array-backed)
                    └── LinkedSetTest.java      # Part 4 Execution Test (Link-backed)
```

---

## 🚀 Execution & Verification Commands

Per the professor's late course updates, formal JUnit testing coverage is deferred until after the midterm. Part 4 evaluation results are integrated directly into standard executable classes with terminal printing hooks.

To run and verify the set algebra solutions, navigate to your root folder in your terminal and compile/execute using standard Java commands:

### Running the Array Set Algebra Scenario:
```bash
javac src/main/java/com/setforge/*.java src/test/java/com/setforge/ArraySetTest.java -d bin
java -cp bin com.setforge.ArraySetTest
```

### Running the Linked Set Algebra Scenario:
```bash
javac src/main/java/com/setforge/*.java src/test/java/com/setforge/LinkedSetTest.java -d bin
java -cp bin com.setforge.LinkedSetTest
```

---

## 🧑‍💻 Personal Verification Statement

Following the instruction modifications communicated by Professor Fatemeh Jamshidi, I pivoted my verification and validity strategy to utilize local manual checks alongside ChatGPT assistance. 

I personally trace-verified the following parameters before compiling the final delivery archive:
1.  **Memory Leakage Checks:** I reviewed `ResizableArraySet.java` to confirm that elements removed from the collection are explicitly set to `null` immediately to block any pointer loitering.
2.  **Pointer Trace Boundaries:** I walked through the single-link modifications in `LinkedSet.java` to confirm that edge extraction cases (removing head nodes, middle nodes, or lone singleton entries) update reference chains safely without dropping subsequent data links.
3.  **Input Immutability Constraints:** I verified that the mathematical algebra queries (`union`, `intersection`, `difference`) generate independent return objects without modifying the original input state configurations.
