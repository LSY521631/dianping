package Day9;

import java.util.ArrayList;
import java.util.HashMap;

public class Test02 {
    public static void main(String[] args) {
        ArrayList<HashMap<String, String>> maps = new ArrayList<>();
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("lsy", "111");
        HashMap<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("zgl", "123");
        HashMap<String, String> hashMap3 = new HashMap<>();
        hashMap3.put("lvy", "121");

        // 存放在ArrayList集合中
        maps.add(hashMap1);
        maps.add(hashMap2);
        maps.add(hashMap3);

        // 遍历输出嵌套的集合
        for (HashMap<String, String> hashmap : maps
        ) {
            System.out.println(hashmap);
        }


    }
}
