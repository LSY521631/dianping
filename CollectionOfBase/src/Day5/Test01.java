package Day5;

import java.util.HashMap;
import java.util.Map;

public class Test01 {
    public static void main(String[] args) {
        // 创建hashMap集合
        Map<String, String> hashMap = new HashMap<>();
        // 向集合中存放元素
        hashMap.put("lsy", "23");
        hashMap.put("zgl", "23");
        hashMap.put("ysj", "32");
        System.out.println(hashMap);
        // 根据指定的键值，删除对应的键值对
        hashMap.remove("ysj");
        System.out.println(hashMap);
        // 判断键值是否存在
        String key = "lsy";
        if (hashMap.containsKey(key)) {
            System.out.println(key + "存在");
        } else {
            System.out.println(key + "不存在");
        }
        // 判断键值对中value值是否存在
        String value = "22";
        if (hashMap.containsValue(value)) {
            System.out.println(value + "存在");
        } else {
            System.out.println(value + "不存在");
        }

        // 清空
        hashMap.clear();
        System.out.println(hashMap);
        // 判断集合是否为空
        if (hashMap.isEmpty()) {
            System.out.println("hashMap集合为空");
        } else {
            System.out.println("hashMap集合不为空");
        }

        Map<String, String> hashMap2 = null;
        // System.out.println(hashMap2.isEmpty());
        // 会直接报错，Map<String, String> hashMap2 = null;
    }
}
