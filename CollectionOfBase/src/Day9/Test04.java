package Day9;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Test04 {
    public static void main(String[] args) {

        // 1.键盘录入一个字符串
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入一个字符串");
        String str = sc.nextLine();

        // 2.创建一个HashMap集合统计字符在整个字符串中出现的次数
        // key=字符
        // value=该字符出现的次数
        HashMap<Character, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            // charAt(i)是 String 类的一个方法，用于获取字符串中指定索引位置的字符
            Character key = str.charAt(i);
            Integer value = hashMap.get(key);

            if (value == null) {
                // 如果该字符串第一次出现是在HashMap集合查找不到的
                value = 1;  // 第一次出现
            } else {
                value++;
            }
            hashMap.put(key, value);
        }

        // 3.输出
        for (Map.Entry<Character, Integer> entry : hashMap.entrySet()) {
            System.out.println("字符串" + entry.getKey());
            System.out.println("出现的次数" + entry.getValue());
            System.out.println();
        }
    }
}
