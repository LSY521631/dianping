package Day5;

import java.util.HashMap;
import java.util.HashSet;

public class MyHashSet<E> {
    /**
     * HashSet的底层是基于HashMap集合实现
     * 元素的值就是HashMap种的Key值
     */
    private HashMap<E, Object> map;

    // 私有的静态常量 PRESENT，其类型为 Object;作为占位符
    private static final Object PRESENT = new Object();

    public MyHashSet() {
        map = new HashMap<>();
    }

    public void add(E e) {
        map.put(e, PRESENT);
    }

    @Override
    public String toString() {
        return "MyHashSet{" +
                "map=" + map +
                '}';
    }
}
