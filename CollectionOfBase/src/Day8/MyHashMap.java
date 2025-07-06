package Day8;

/**
 * hashMap集合底层根据hash算法存放和查找key
 * 时间复杂度是为o(1)
 * 只需要查询一次
 *
 * @param <K>
 * @param <V>
 */
public class MyHashMap<K, V> {

    // 假设数组的容量=10000(涉及数组的扩容机制，此处直接将数组的大小写死)
    private Entry[] entrys = new Entry[10000];

    // Entry用来存放键值对
    class Entry<K, V> {
        K k; // hashmap中的Key
        V v; // hashmap中的Value
        int hash; // hashmap中的Key的hash值
        // 下面还有很多待会。。。

        // 下一个节点
        Entry<K, V> next;

        // 有参构造方法
        public Entry(K k, V v) {
            this.k = k;
            this.v = v;
        }
    }

    public void put(K k, V v) {

        /**
         * 1.根据key的hashcode取余entrys.length;
         * 余数为该key存放在数组对应的index位置
         */
        int index = k.hashCode() % entrys.length;

        /**
         * put方法中解决hash冲突
         * 1.先判断该index位置是否存放链表
         * 2.如果能够取出该Entry对象
         */
        Entry oldEntry = entrys[index];
        if (oldEntry == null) {
            entrys[index] = new Entry<>(k, v);
        } else {
            oldEntry.next = new Entry<>(k, v);
        }


    }

    public V get(K k) {
        int index = k.hashCode() % entrys.length;
        return (V) entrys[index].v;
    }

    public static void main(String[] args) {
        MyHashMap<Object, Object> myHashMap = new MyHashMap<>();
        myHashMap.put("lsy", 22);
        System.out.println(myHashMap.get("lsy"));
    }
}
