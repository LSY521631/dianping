package Day10;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class Test01 {
    public static void main(String[] args) {
        /**
         * 1.HashSet底层基HashMap集合实现
         * 2.HashSet不允许元素发生重复的
         * 3.HashMap存放key是散列的形式,因此HashSet数据遍历无序
         */
        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        hashSet.add("hello");
        hashSet.add("hello");
        hashSet.add("hello");

        for (int i = 0; i < 100; i++) {
            hashSet.add(i + "");
        }
        for (String str : hashSet
        ) {
            System.out.println(str);
        }

    }
}
