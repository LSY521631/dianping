package Day10;

import java.util.LinkedHashMap;

public class MyLinkedHashSet<E> {
    /**
     * LinkedHashSet底层是基于LinkedHashMap实现
     */
    private LinkedHashMap<E, Object> linkedHashMap;
    /**
     * PRESENT:一个静态对象，用于作为 LinkedHashMap 中的值。
     * 因为 LinkedHashSet 只关心键的存在，不关心值
     * 所以使用一个固定的对象作为值。
     */
    private static Object PRESENT = new Object();

    public MyLinkedHashSet() {
        linkedHashMap = new LinkedHashMap();
    }

    public void add(E e) {
        linkedHashMap.put(e, PRESENT);
    }

    @Override
    public String toString() {
        return "MyLinkedHashSet{" +
                "linkedHashMap=" + linkedHashMap +
                '}';
    }
}
