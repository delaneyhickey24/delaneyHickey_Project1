
public class LinkedSetTest extends SetContractTest {
    @Override
    protected <T> SetInterface<T> createSet() {
        return new LinkedSet<>();
    }
}
