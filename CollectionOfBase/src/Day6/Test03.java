package Day6;

import java.util.HashMap;
import java.util.Set;

public class Test03 {
    /**
     * 思路：
     * 1.先获取到HashMap中所有的键值
     * 2.调用get方法,获取对应键值的value值
     */
    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("001", "LSY");
        hashMap.put("002", "ZGL");
        hashMap.put("003", "YSJ");
        // 1.先获取到HashMap中所有的键值
        Set<String> keySet = hashMap.keySet();
        for (String key : keySet
        ) {
            // 2.调用get方法,获取对应键值的value值
            String s = hashMap.get(key);
            System.out.println(key + ":" + s);
        }
    }
}
