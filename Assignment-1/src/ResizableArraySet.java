
import java.util.Arrays;
import java.util.Objects;

/**
 * A resizable-array implementation of the SetInterface ADT.
 * Elements are packed contiguously. Loitering is prevented on removal.
 * Null values are strictly rejected.
 */
public class ResizableArraySet<T> implements SetInterface<T> {

    private T[] setArray;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Creates an empty set with a default initial capacity.
     */
    @SuppressWarnings("unchecked")
    public ResizableArraySet() {
        this.setArray = (T[]) new Object[DEFAULT_CAPACITY];
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

        // Check for duplicates using structural equality
        if (contains(newEntry)) {
            return false;
        }

        // Dynamic Growth Rule: Double array capacity when full
        if (size >= setArray.length) {
            ensureCapacity();
        }

        setArray[size] = newEntry;
        size++;
        return true;
    }

    @Override
    public T remove() {
        if (isEmpty()) {
            return null;
        }
        
        // Remove the last active item to avoid shifting overhead while keeping items contiguous
        int targetIndex = size - 1;
        T removedItem = setArray[targetIndex];
        
        setArray[targetIndex] = null; // Prevent loitering
        size--;
        
        return removedItem;
    }

    @Override
    public boolean remove(T anEntry) {
        if (anEntry == null) {
            throw new IllegalArgumentException("Cannot remove null reference from Set.");
        }

        for (int i = 0; i < size; i++) {
            if (setArray[i].equals(anEntry)) {
                // To close the gap without shifting every single item, 
                // move the very last active element into this index slot.
                setArray[i] = setArray[size - 1];
                setArray[size - 1] = null; // Prevent loitering
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        // Explicitly clear references to prevent loitering across the entire buffer
        for (int i = 0; i < size; i++) {
            setArray[i] = null;
        }
        this.size = 0;
    }

    @Override
    public boolean contains(T anEntry) {
        if (anEntry == null) {
            throw new IllegalArgumentException("Set cannot search for null entries.");
        }
        for (int i = 0; i < size; i++) {
            if (setArray[i].equals(anEntry)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T[] toArray() {
        // Returns a safe, completely detached snapshot copy
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[this.size];
        System.arraycopy(setArray, 0, result, 0, this.size);
        return result;
    }

    // --- Dynamic Resizing Strategy ---
    private void ensureCapacity() {
        int newCapacity = setArray.length * 2;
        // Tradeoff: Doubling capacity scales amortized time complexity of additions to O(1).
        // The downside is temporary spatial overhead, reserving memory that might remain unfilled.
        this.setArray = Arrays.copyOf(this.setArray, newCapacity);
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
