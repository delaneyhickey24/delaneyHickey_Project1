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
