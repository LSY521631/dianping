package Day6;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Test04 {
    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("001", "LSY");
        hashMap.put("002", "ZGL");
        hashMap.put("003", "YSJ");

        // 使用集合中的entrySet()方法,使用for...each
        // map集合中键值对通过entry封装
        Set<Map.Entry<String, String>> entries = hashMap.entrySet();
        for (Map.Entry<String, String> entry : entries
        ) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        System.out.println();
        // 优化
        for (Map.Entry<String, String> entry :
                hashMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
