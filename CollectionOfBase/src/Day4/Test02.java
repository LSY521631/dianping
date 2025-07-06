package Day4;

import java.util.HashMap;

public class Test02 {
    public static void main(String[] args) {
        // 创建hashMap集合并输出
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("lsy", "23");
        hashMap.put("zgl", "23");
        hashMap.put("ysj", "32");
        hashMap.put("ysj", "24");// 不允许k重复，k重复直接修改v值
        System.out.println(hashMap);
    }
}
