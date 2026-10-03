# SetForge Complexity Lab Report (COMPLEXITY.md)

This report details the Big-O asymptotic runtime derivations for both Set ADT implementations and breaks down the core structural performance differences.

---

## 1. Asymptotic Runtime Analysis Matrix

| Operation / Method | `ResizableArraySet<T>` Complexity | `LinkedSet<T>` Complexity |
| :--- | :--- | :--- |
| `getCurrentSize()` | **O(1)** | **O(1)** |
| `isEmpty()` | **O(1)** | **O(1)** |
| `add(T newEntry)` | **O(n)** *(Amortized)* | **O(n)** |
| `contains(T anEntry)` | **O(n)** | **O(n)** |
| `remove()` *(Unspecified)* | **O(1)** | **O(1)** |
| `remove(T anEntry)` | **O(n)** | **O(n)** |
| `clear()` | **O(n)** | **O(1)** |
| `toArray()` | **O(n)** | **O(n)** |
| `union(SetInterface<T> other)` | **O(n × m)** | **O(n × m)** |
| `intersection(SetInterface<T> other)`| **O(n × m)** | **O(n × m)** |
| `difference(SetInterface<T> other)`  | **O(n × m)** | **O(n × m)** |

*Note: n represents the size of the current set; m represents the size of the `otherSet` input.*

---

## 2. Complexity Derivation Brain Dump

### Why `remove()` is O(1) in both setups
*   **ResizableArraySet**: To avoid the O(n) overhead of shifting elements backward when an item is removed, our implementation target extracts from the very last active slot (`size - 1`). Array index lookups happen in direct constant time.
*   **LinkedSet**: Our algorithm immediately decouples the front-most node (`headNode`), updates the pointer to `headNode.next`, and updates the counter. It avoids any sequential link traversal.

### Why Algebraic Operations are O(n × m)
For `union`, `intersection`, and `difference`, the system runs a sequential traversal loop through all elements of the first set (n items). For each item, it invokes `.contains()` or `.add()` on the target set which scales lineally based on its active size (m elements). This nested loop profile yields a combined quadratic execution curve.

---

## 3. The Linked List "Reasoning Trap"

### The Paradox
A basic singly linked list structure can insert a fresh element at its head pointer in absolute constant time (**O(1)**) simply by shuffling two address references. However, our `LinkedSet.add()` operation remains bound to a linear **O(n)** runtime constraint.

### Precise Architectural Reason
A Set ADT enforces a **strict unique element domain rule**. Before any element addition can be committed to the linked chain, the data structure is forced to guarantee that the incoming entry is not already stashed in the collection. It must invoke `contains(newEntry)`. 
Searching an un-indexed, unsorted singly linked list requires a sequential pointer-chase traversal loop from `headNode` down to the terminal `null` marker. This inspection takes O(n) time in the worst-case scenario. Even though the subsequent pointer injection takes O(1) time, the dominant term dictates that the overall method runtime scales as **O(n)**.

### Proposed Optimization (Out of Scope)
*   **The Optimization**: We could store an auxiliary **Hash Table** or balanced **Binary Search Tree** alongside our linked chain that tracks only the identity of items present within the node chain. This would drop the duplicate validation scan step down to an expected **O(1)** time complexity.
*   **Why it is Forbidden**: This assignment imposes explicit storage constraints. The *Rules of the Build* forbid importing or backing production arrays with external frameworks like `HashSet` or `TreeSet`. Maintaining a secondary custom array-backed hash tracker would demand duplicate memory footprint models, which violates the strict single structural node sequence rule specified for this phase.
