package com.setforge;

/**
 * Execution test verifying ResizableArraySet algebra functionality 
 * and documenting the result of Part 4.
 */
public class ArraySetTest {
    public static void main(String[] args) {
        System.out.println("=== RESIZABLE ARRAY SET ALGEBRA TEST ===");

        SetInterface<String> alpha = new ResizableArraySet<>();
        alpha.add("A12"); alpha.add("B07"); alpha.add("C31"); alpha.add("D04");

        SetInterface<String> beta = new ResizableArraySet<>();
        beta.add("B07"); beta.add("D04"); beta.add("E18"); beta.add("F22");

        SetInterface<String> revoked = new ResizableArraySet<>();
        revoked.add("C31"); revoked.add("F22");

        // Part 4 Logic: (Alpha UNION Beta) DIFFERENCE Revoked
        SetInterface<String> clearCrew = alpha.union(beta).difference(revoked);

        System.out.println("Active Crew Set Size: " + clearCrew.getCurrentSize());
        System.out.print("Allowed Access IDs: ");
        for (Object id : clearCrew.toArray()) {
            System.out.print(id + " ");
        }
        System.out.println("\nExpected Output: A12 B07 D04 E18");
    }
}


