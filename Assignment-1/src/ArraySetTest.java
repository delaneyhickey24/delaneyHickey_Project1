package com.setforge; 

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArraySetTest extends SetContractTest {
    @Override
    protected <T> SetInterface<T> createSet() {
        return new ResizableArraySet<>();
    }

    @Test
    public void testArraySpecificCapacityBoundary() {
        // Force the ResizableArraySet past its initial structural threshold to verify integrity
        SetInterface<Integer> set = createSet();
        for (int i = 0; i < 15; i++) {
            assertTrue(set.add(i));
        }
        assertEquals(15, set.getCurrentSize());
    }


    @Test
    public void testCrossImplementationDivergence() {
        SetInterface<Integer> arraySet = new ResizableArraySet<>();
        SetInterface<Integer> linkedSet = new LinkedSet<>();

        // Drive both setups with identical sequential insertions/removals
        int[] actions = {10, 20, 30, 40, 50, 20, 60, 10};
        for (int val : actions) {
            arraySet.add(val);
            linkedSet.add(val);
        }

        arraySet.remove(30);
        linkedSet.remove(30);

        // Assert structural convergence of size and membership across both variations
        assertEquals(arraySet.getCurrentSize(), linkedSet.getCurrentSize());
        for (int val : actions) {
            assertEquals(arraySet.contains(val), linkedSet.contains(val));
        }
    }

}

