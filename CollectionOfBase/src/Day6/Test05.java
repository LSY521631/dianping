package Day6;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Test05 {
    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("001", "LSY");
        hashMap.put("002", "ZGL");
        hashMap.put("003", "YSJ");

        // 使用集合中的entrySet()方法,使用迭代器
        Set<Map.Entry<String, String>> entries = hashMap.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
