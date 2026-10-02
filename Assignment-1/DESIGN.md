# SetForge Architectural Design Document (DESIGN.md)

This document establishes the representation invariants, structral design choices, and semantic policies for the generic Set ADT implementations.

---

## 1. Duplicate & Equality Policy
### What makes two entries duplicates?
Two entries 'e1' and 'e2' of generic type 'T' are considered duplicates if and only if 'e1.equals(e2)' evaluates to 'true'.

### Defense of Equality Choice for Generic Type T
Using the indentity operator ('==') is rejected because it only checks if two references point to the exact same memory location. 
In a real-world sustem merging crew badges, two separate 'Badge' objects instantiated in different momory segmants containing indentical feilds (e.g., 'CrewMember("A!@")') must be treated as duplicates to prevent access vulnerability risks.

By utilixing 'Object.equals(Object)', we empower individual domain classes to override equality criteria safely based on their actual internal state (Structural equality). 
Futhermore, checking 'e1.equals(e2)' is consistently paired with a defense 'null' guard ('java.util.Objects.equals') to eliminate potential 'NullPointerException' failures.

---

## 2. Representation Invariants (RI)

A Representation Invariant defines conditions that must always remain true before and after any public method execution for an object to be in a valid structural state. 

### 'ResizableArraySet<T> Invariants
1. **Contiguous Elements Packing**: All active enteries reside contiguously within the indices '0' to 'size - 1'. There must be no interstitial 'null' references or "gaps" within this index range.
2. **Size Boundaries**: the logical size tracking variable must satisfy '0 <= size <= setArray.length'.
3. **Prevention of Loitering**: Every inactive index slot from index 'size' up to 'setArray.length -1' must be strictly set to 'null'.
4. **Uniqueness Domain**: For any pair of distinct active indices 'i' and 'j' where '0 <= i < j < size', 'setArray[i].equals(setArray[j])' must evaluate to 'false'.

### 'LinkedSet<T>' Invariants
1. **Size Tracking Accuracy**: The integer value of 'size' must exactly match the total count of non-null nodes reachable by traversing sequentually from the 'headNode' until reaching a 'null' terminator pointer.
2. **Empty Termination**: If 'size == 0', 'headNode' must be strictly 'null'. Conversely, if 'headNode == null', 'size' must strictly equal '0'.
3. **Acyclicity**: The internal pointer chain must be strictly linear and acyclic. No node can reference an earlier node in the traversal chain, ensuring a finite termination path.
4. **Uniqueness Domain**: For any two distinct reachable nodes 'N1' and 'N2' positioned at different links in the chain, 'N1.getData().equals(N2.getData())' must evalute to 'false'.

---
## 3. Empty Set Removal Policy
### Chosen Policy
When 'remove()' is invoked on an empty set, the method will return 'null'.

### Policy Justification
Returning 'null' is chosen over throwing an explicit runtime exception (like 'IllegalStateException') because an empty state is a predictable structural boundary rather than a fatal, unrecoverable engineering failure. 
It mimics a standard polling mechanism: a caller checking an empty station registry can cmoothly receive a 'null' indicator and proceed to other tasks without setting up heavy, preformace-costing try-catch exception blocks.
Since the system rejects 'null' as a valid element addition, a 'null' output safely and unambiguously signals that the collection is empty. 

---

##4. Set Algebra Independence and Immutability
### Why the three algebra operations must return independent sets
the algebraic operations ('union', 'intersection', 'difference') must perform deep structual allocation and return completely un-aliased, independent objects.
Returning a shared memory reference (an alias) or a collection that directly wraps internal array buffers or node references breaches encapsulation boundaries.

### Failure Scenario
Imagine a scenario where 'union' incorrectly returns an aliased view or directly leaks internal array tracking states:
1. An administrator creates an operational access list 'activeCrew' by calling 'alpha.union(beta)'.
2. Because of an aliasing flaw, the internal storage buffer of 'activeCrew' is directly linked to or shares elements with 'alpha'.
4. Due to memory aliasing, the entries inside 'activeCrew' are silently altered or cleared completely in tandem. The emergency response team loses their structural lookup capabilities, locking personnel out of safe zones during a crisis. 

## 5. ResizableArraySet Checkpoint Trace Matrix
This execution trace maps a small internal capacity buffer initialized to a starting length of 2 (`capacity = 2`), forcing rapid resizing and validation of our packing states.

| Step | Operation Called | Internal Buffer Array State `[Idx 0, Idx 1, Idx 2, ...]` | Size | Capacity | Invariant Probed & Verified |
|:---|:---|:---|:---|:---|:---|
| 0 | Initial State | `[null, null]` | 0 | 2 | **RI-1 & RI-3**: Empty state has valid bounds; all slots null. |
| 1 | `add("A12")` | `["A12", null]` | 1 | 2 | **RI-1**: Packed contiguously. No gaps. |
| 2 | `add("B07")` | `["A12", "B07"]` | 2 | 2 | **RI-2**: Reached capacity threshold cleanly without overflow. |
| 3 | `add("B07")` *(Duplicate)* | `["A12", "B07"]` | 2 | 2 | **RI-4**: Uniqueness verified. Set remains unchanged, returns false. |
| 4 | `add("C31")` *(Forces Resize 1)* | `["A12", "B07", "C31", null]` | 3 | 4 | **RI-1 & RI-2**: Array doubled to 4. Elements remain packed. |
| 5 | `add("D04")` | `["A12", "B07", "C31", "D04"]` | 4 | 4 | **RI-2**: Reached secondary upper capacity threshold. |
| 6 | `add("E18")` *(Forces Resize 2)* | `["A12", "B07", "C31", "D04", "E18", null, null, null]` | 5 | 8 | **Dynamic Growth**: Array doubled to 8. Confirms two resizes success. |
| 7 | `add("A12")` *(Duplicate post-resize)* | `["A12", "B07", "C31", "D04", "E18", null, null, null]` | 5 | 8 | **RI-4**: Uniqueness remains intact across expanded boundaries. |
| 8 | `remove("A12")` *(Remove First)* | `["E18", "B07", "C31", "D04", null, null, null, null]` | 4 | 8 | **RI-1 & RI-3**: Element `E18` fills gap. Index 4 set to null (No loitering). |
| 9 | `remove("C31")` *(Remove Middle)*| `["E18", "B07", "D04", null, null, null, null, null]` | 3 | 8 | **RI-1 & RI-3**: Element `D04` fills gap. Index 3 set to null (No loitering). |
| 10| `remove("D04")` *(Remove Last)*  | `["E18", "B07", null, null, null, null, null, null]` | 2 | 8 | **RI-1 & RI-3**: Index 2 explicitly set to null. Elements remain contiguous. |
| 11| `add("A12")` *(Re-add)* | `["E18", "B07", "A12", null, null, null, null, null]` | 3 | 8 | **State Recovery**: Re-added item appends to next open index slot. |

## 6. LinkedSet Structural Pointer Verification

This analysis details node pointer adjustments during precise extraction sequences, noting the catastrophic reference breaks to guard against.

### Structural Scenarios Trace

#### Scenario A: Removing the Head Node
*   **Initial Chain State**: `headNode ➔ [Node1: "A12"] ➔ [Node2: "B07"] ➔ [Node3: "C31"] ➔ null`
*   **Correct Pointer Update**: `headNode = headNode.next;`
*   **Final Chain State**: `headNode ➔ [Node2: "B07"] ➔ [Node3: "C31"] ➔ null`
*   **The Catastrophic Mutation Bug**: Accidentally setting `headNode = null;` or execution of `headNode.next = null;`. Doing so would permanently orphan the subsequent links (`Node2` and `Node3`), rendering the rest of the valid database structure unrecoverable in memory.

#### Scenario B: Removing a Middle Node (Targeting "B07")
*   **Initial Chain State**: `headNode ➔ [Node1: "A12"] ➔ [Node2: "B07"] ➔ [Node3: "C31"] ➔ null`
*   **Tracking Trackers**: `previousNode` points to `Node1`, `currentNode` points to `Node2`.
*   **Correct Pointer Update**: `previousNode.next = currentNode.next;`
*   **Final Chain State**: `headNode ➔ [Node1: "A12"] ➔ [Node3: "C31"] ➔ null`
*   **The Catastrophic Mutation Bug**: Assigning `currentNode.next = previousNode;` (creating a cyclic trap loop) or setting `previousNode.next = null;`. severing the reference using `null` targets splits the chain into an unlinked sequence, throwing away `Node3` entirely.

#### Scenario C: Removing the Last Node (Tail Node)
*   **Initial Chain State**: `headNode ➔ [Node1: "A12"] ➔ [Node2: "B07"] ➔ [Node3: "C31"] ➔ null`
*   **Tracking Trackers**: `previousNode` points to `Node2`, `currentNode` points to `Node3`.
*   **Correct Pointer Update**: `previousNode.next = currentNode.next;` (which resolves cleanly to `null`).
*   **Final Chain State**: `headNode ➔ [Node1: "A12"] ➔ [Node2: "B07"] ➔ null`
*   **The Catastrophic Mutation Bug**: Executing `headNode.next = null;` during secondary iterations. This bypasses the node-specific trackers and wipes out all nodes following the head element (`Node2` disappears alongside `Node3`), violating the entire state preservation rule.
