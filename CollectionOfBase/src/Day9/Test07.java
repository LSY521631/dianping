package Day9;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Test07 {
    public static void main(String[] args) {
        /**
         * LinkedHashMap与HashMap集合用法都是相同的
         * 唯一区别：
         * LinkedHashMap是有序的Map集合
         * HashMap是无序的Map集合
         */
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        for (int i = 0; i < 100; i++) {
            linkedHashMap.put(i + "", i + "");
        }

        for (Map.Entry<String, String> entry : linkedHashMap.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }
    }
}
