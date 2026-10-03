package com.setforge;

/**
 * Execution test verifying LinkedSet algebra functionality 
 * and documenting the result of Part 4.
 */
public class LinkedSetTest {
    public static void main(String[] args) {
        System.out.println("=== LINKED SET ALGEBRA TEST ===");

        SetInterface<String> alpha = new LinkedSet<>();
        alpha.add("A12"); alpha.add("B07"); alpha.add("C31"); alpha.add("D04");

        SetInterface<String> beta = new LinkedSet<>();
        beta.add("B07"); beta.add("D04"); beta.add("E18"); beta.add("F22");

        SetInterface<String> revoked = new LinkedSet<>();
        revoked.add("C31"); revoked.add("F22");

        // Part 4 Logic: (Alpha UNION Beta) DIFFERENCE Revoked
        SetInterface<String> clearCrew = alpha.union(beta).difference(revoked);

        System.out.println("Active Crew Set Size: " + clearCrew.getCurrentSize());
        System.out.print("Allowed Access IDs: ");
        for (Object id : clearCrew.toArray()) {
            System.out.print(id + " ");
        }
        System.out.println("\nExpected Output: E18 D04 B07 A12 (Order may vary due to head insertion)");
    }
}
