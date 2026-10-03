import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public abstract class SetContractTest {

    // Factory method implemented by concrete subclasses
    protected abstract <T> SetInterface<T> createSet();

    // Custom helper to check membership without assuming array order
    protected <T> void assertSetContainsExactly(SetInterface<T> set, T... expectedElements) {
        assertEquals(expectedElements.length, set.getCurrentSize(), "Set size mismatch.");
        for (T element : expectedElements) {
            assertTrue(set.contains(element), "Set missing expected element: " + element);
        }
    }

    // --- 1. BOUNDARY FAMILY ---
    @Test
    public void testBoundaryEmptyAndSingleton() {
        SetInterface<String> set = createSet();
        
        // Empty boundaries
        assertTrue(set.isEmpty());
        assertEquals(0, set.getCurrentSize());
        assertNull(set.remove()); // Empty remove policy verified
        
        // Singleton transitions
        assertTrue(set.add("A12"));
        assertFalse(set.isEmpty());
        assertEquals(1, set.getCurrentSize());
        
        // Clear transition
        set.clear();
        assertTrue(set.isEmpty());
        assertEquals(0, set.getCurrentSize());
    }

    // --- 2. DUPLICATE/EQUALITY FAMILY ---
    @Test
    public void testStructuralEqualityNotIdentity() {
        SetInterface<CustomBadge> set = createSet();
        
        // Two distinct object instances in memory with structurally identical data
        CustomBadge badge1 = new CustomBadge("B07");
        CustomBadge badge2 = new CustomBadge("B07");
        
        assertNotSame(badge1, badge2, "Test configuration error: references must be distinct objects.");
        
        assertTrue(set.add(badge1));
        assertFalse(set.add(badge2), "Set should reject duplicate based on structural .equals(), not reference ==.");
        assertEquals(1, set.getCurrentSize());
    }

    // --- 3. MUTATION/ALIASING FAMILY ---
    @Test
    public void testSnapshotAndAlgebraImmutability() {
        SetInterface<String> set1 = createSet();
        set1.add("A12");
        set1.add("B07");

        // Verify toArray snapshots are decoupled arrays
        String[] snapshot = set1.toArray();
        assertEquals(2, snapshot.length);
        snapshot[0] = "MUTATED"; 
        assertTrue(set1.contains("A12"), "Mutating returned array snapshot must not alter underlying set.");

        // Verify algebra inputs are completely immutable
        SetInterface<String> set2 = createSet();
        set2.add("B07");
        set2.add("C31");

        SetInterface<String> unionSet = set1.union(set2);
        
        // Check that inputs are untouched and results are fully independent objects
        assertSetContainsExactly(set1, "A12", "B07");
        assertSetContainsExactly(set2, "B07", "C31");
        assertNotSame(set1, unionSet);
        assertNotSame(set2, unionSet);
    }

    // --- 4. ALGEBRAIC PROPERTIES FAMILY ---
    @Test
    public void testAlgebraicProperties() {
        SetInterface<String> alpha = createSet();
        alpha.add("A12"); alpha.add("B07"); alpha.add("C31"); alpha.add("D04");

        SetInterface<String> beta = createSet();
        beta.add("B07"); beta.add("D04"); beta.add("E18"); beta.add("F22");

        SetInterface<String> revoked = createSet();
        revoked.add("C31"); revoked.add("F22");

        // 1. Solve assignment access scenario: (alpha U beta) \ revoked = {A12, B07, D04, E18}
        SetInterface<String> clearCrew = alpha.union(beta).difference(revoked);
        assertSetContainsExactly(clearCrew, "A12", "B07", "D04", "E18");

        // 2. Identity Property with Empty Set
        SetInterface<String> empty = createSet();
        assertSetContainsExactly(alpha.union(empty), "A12", "B07", "C31", "D04");
        assertSetContainsExactly(alpha.intersection(empty)); // should be empty
        assertSetContainsExactly(alpha.difference(empty), "A12", "B07", "C31", "D04");

        // 3. Commutativity of Intersection (alpha ∩ beta == beta ∩ alpha)
        SetInterface<String> int1 = alpha.intersection(beta);
        SetInterface<String> int2 = beta.intersection(alpha);
        assertSetContainsExactly(int1, "B07", "D04");
        assertSetContainsExactly(int2, "B07", "D04");

        // 4. Directionality/Non-commutativity of Difference (alpha \ beta != beta \ alpha)
        SetInterface<String> diffAB = alpha.difference(beta);
        SetInterface<String> diffBA = beta.difference(alpha);
        assertSetContainsExactly(diffAB, "A12", "C31");
        assertSetContainsExactly(diffBA, "E18", "F22");
    }

    // --- 5. RANDOMIZED SEQUENCE FAMILY ---
    @Test
    public void testRandomizedOperationsSequence() {
        SetInterface<Integer> set = createSet();
        // Fixed seed ensures deterministic, reproducible test runs across environments
        Random rand = new Random(42); 
        java.util.Set<Integer> oracle = new java.util.HashSet<>(); // Only allowed inside tests!

        for (int i = 0; i < 600; i++) {
            int operation = rand.nextInt(3);
            int value = rand.nextInt(100);

            if (operation == 0) { // Add
                assertEquals(oracle.add(value), set.add(value));
            } else if (operation == 1) { // Contains
                assertEquals(oracle.contains(value), set.contains(value));
            } else { // Remove explicit
                assertEquals(oracle.remove(value), set.remove(value));
            }
            assertEquals(oracle.size(), set.getCurrentSize());
        }
    }

    // Helper Class for structural equality tests
    private static class CustomBadge {
        private final String id;
        public CustomBadge(String id) { this.id = id; }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CustomBadge that = (CustomBadge) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() { return Objects.hash(id); }
    }
}

