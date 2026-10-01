public interface SetInterface<T> {

    /**
     * Gets the current number of elements inside this set.
     * @return The integer number of elements.
     */
    int getCurrentSize();

    /**
     * Determines whether this set contains no elements.
     * @return true if the set is empty; false otherwise
     */
    boolean isEmpty();
    
    /**
     * Adds a new entry to this set, avoiding duplicates.
     * @param newEntry The object to be added as a new entry.
     * @return true if the addition was successful; false if the item was a duplicate.
     * @throws IllegalArgumentException if {@code newEntry} is null.
     */
    boolean add(T newEntry);

    /**
     * Removes an unspecified entry from this set if possible.
     * @return Either the removed entry, or null if the set was empty.
     */
    T remove();

    /**
     * Removes one occurrence of a specific entry from this set, if possible.
     * @param anEntry The entry to be removed.
     * @return true if the removal was successful; false otherwise.
     * @throws IllegalArgumentException if {@code anEntry} is null.
     */
    boolean remove(T anEntry);

    /**
     * Removes all elements from this set, resetting it to an empty state.
     */
    void clear();

    /**
     * Tests whether this set contains a specific entry.
     * 
     * @param anEntry The entry to locate
     * @return true if the set contains anEntry; false otherwise.
     * @throws IllegalArgumentException if {@code anEntry} is null.
     */
    boolean contains(T anEntry);

    /**
     * Retrieves all entries that are currently in this set.
     * @return A new array containing all the entries in the set.
     */
    T[] toArray();

    /**
     * Computes the mathematical union of this set and another set.
     * Neither input set is modified by this operation.
     * @param otherSet The other set to combine with.
     * @return A new independent set containing elements from both sets.
     * @throws IllegalArgumentException if {@code otherSet} is null.
     */
    SetInterface<T> union(SetInterface<T> otherSet);

    /**
     * Computes the mathematical intersection of this set and another set.
     * Neither input set is modified by this operation.
     * @param otherSet The other set to intersect with.
     * @return A new independent set containing only elements present in both sets.
     * @throws IllegalArgumentException if {@code otherSet} is null.
     */
    SetInterface<T> intersection(SetInterface<T> otherSet);

    /**
     * Computes the mathematical directional difference of this set minus another set.
     * Neither input set is modified by this operation.
     * @param otherSet The set to subtract from this set.
     * @return A new independent set containing elements in this set but not the other.
     * @throws IllegalArgumentException if {@code otherSet} is null.
     */
    SetInterface<T> difference(SetInterface<T> otherSet);

} 