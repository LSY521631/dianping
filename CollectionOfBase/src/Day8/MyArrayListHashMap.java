package Day8;

import java.util.ArrayList;

/**
 * 简易的HashMap集合
 * 缺点：速度慢
 * 有点：可以保证存放的键值对是有序存放，不是散列
 *
 * @param <K>
 * @param <V>
 */
public class MyArrayListHashMap<K, V> {
    // 创建一个容器存放
    private ArrayList<Entry<K, V>> entryArrayList = new ArrayList<Entry<K, V>>();

    // Entry用来存放键值对
    class Entry<K, V> {
        K k;
        V v;
        // 下面还有很多待会。。。

        // 有参构造方法
        public Entry(K k, V v) {
            this.k = k;
            this.v = v;
        }
    }

    public void put(K k, V v) {
        entryArrayList.add(new Entry<>(k, v));
    }

    /**
     * 如果基于ArrayList集合实现HashMap
     * 查找时间复杂度就是o(n);n值为循环次数
     */
    public V get(K k) {
        for (Entry<K, V> entry : entryArrayList
        ) {
            if (entry.k.equals(k)) {
                return entry.v;
            }

        }
        return null;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        MyArrayListHashMap<Object, Object> myArrayListHashMap = new MyArrayListHashMap<>();
        myArrayListHashMap.put("lsy", "22");
        System.out.println(myArrayListHashMap.get("lsy"));
    }
}
