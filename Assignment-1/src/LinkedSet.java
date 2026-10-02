/**
 * A singly linked list implementation of the SetInterface ADT.
 * Maintains an explicit size counter and prevents structural leakages.
 * Null values are strictly rejected.
 */
public class LinkedSet<T> implements SetInterface<T> {

    // Private static nested generic node class prevents representation details leakage
    private static class Node<E> {
        private E data;
        private Node<E> next;

        private Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> headNode;
    private int size;

    /**
     * Initializes an empty linked set structure.
     */
    public LinkedSet() {
        this.headNode = null;
        this.size = 0;
    }

    @Override
    public int getCurrentSize() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean add(T newEntry) {
        if (newEntry == null) {
            throw new IllegalArgumentException("Null entries are not permitted in this Set.");
        }

        // Check for duplicates structurally using contains()
        if (contains(newEntry)) {
            return false;
        }

        // Insert at the head of the chain for O(1) linkage performance
        Node<T> newNode = new Node<>(newEntry);
        newNode.next = headNode;
        headNode = newNode;
        size++;
        return true;
    }

    @Override
    public T remove() {
        if (isEmpty()) {
            return null;
        }

        // Remove from head for maximum efficiency
        T removedData = headNode.data;
        headNode = headNode.next;
        size--;
        return removedData;
    }

    @Override
    public boolean remove(T anEntry) {
        if (anEntry == null) {
            throw new IllegalArgumentException("Cannot remove null reference from Set.");
        }

        Node<T> previousNode = null;
        Node<T> currentNode = headNode;

        while (currentNode != null) {
            if (currentNode.data.equals(anEntry)) {
                if (previousNode == null) {
                    // Case 1: Removing the head node (or a singleton node)
                    headNode = currentNode.next;
                } else {
                    // Case 2: Removing a middle or tail node
                    previousNode.next = currentNode.next;
                }
                size--;
                return true;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        return false;
    }

    @Override
    public void clear() {
        // Severing headNode cuts off references, making the whole chain garbage-collectable
        this.headNode = null;
        this.size = 0;
    }

    @Override
    public boolean contains(T anEntry) {
        if (anEntry == null) {
            throw new IllegalArgumentException("Set cannot search for null entries.");
        }

        Node<T> currentNode = headNode;
        while (currentNode != null) {
            if (currentNode.data.equals(anEntry)) {
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    @Override
    public T[] toArray() {
        // Generates an independent linear snapshot copy array
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[this.size];
        
        Node<T> currentNode = headNode;
        int index = 0;
        while (currentNode != null && index < size) {
            result[index] = currentNode.data;
            index++;
            currentNode = currentNode.next;
        }
        return result;
    }

    // --- Placeholders for Step 4 Algebra Operations ---
    @Override
    public SetInterface<T> union(SetInterface<T> otherSet) {
        throw new UnsupportedOperationException("Union operation will be implemented in Step 4.");
    }

    @Override
    public SetInterface<T> intersection(SetInterface<T> otherSet) {
        throw new UnsupportedOperationException("Intersection operation will be implemented in Step 4.");
    }

    @Override
    public SetInterface<T> difference(SetInterface<T> otherSet) {
        throw new UnsupportedOperationException("Difference operation will be implemented in Step 4.");
    }
}

