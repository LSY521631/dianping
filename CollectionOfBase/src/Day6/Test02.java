package Day6;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Test02 {
    public static void main(String[] args) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("001", "LSY");
        hashMap.put("002", "ZGL");
        hashMap.put("003", "YSJ");
        // 1.根据键值对获取
        System.out.println(hashMap.get("001"));
        // 查找不存在的key,返回null
        System.out.println(hashMap.get("009"));
        // 2.获取所有键的集合(返回Set集合),不包含value值
        System.out.println("获取所有键的集合(返回Set集合)");
        Set<String> strings = hashMap.keySet();
        for (String str : strings
        ) {
            System.out.println(str);
        }
        // 3.获取所有值的集合(返回Collection集合),不包含key值
        System.out.println("获取所有键的集合(返回Collection集合)");
        Collection<String> values = hashMap.values();
        for (String str : values
        ) {
            System.out.println(str);
        }
        // 4.获取所有键值对象的集合
        System.out.println("获取所有键值对象的集合");
        Set<Map.Entry<String, String>> entrySet = hashMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet
        ) {
            System.out.println(entry);
        }
        System.out.println();
        // 5.如果存在相应的key则返回其对应的value
        // 否则返回给定的默认值defaultValue
        String aDefault1 = hashMap.getOrDefault("001", "default");
        String aDefault2 = hashMap.getOrDefault("007", "default");
        System.out.println(aDefault1);
        System.out.println(aDefault2);

    }
}
