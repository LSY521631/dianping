package Day9;

import java.util.ArrayList;
import java.util.Collections;

public class Test05 {
    public static void main(String[] args) {

        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(88);
        integers.add(81);
        integers.add(82);
        integers.add(86);

        // 对集合中的元素升序
        Collections.sort(integers);
        for (Integer i : integers
        ) {
            System.out.println(i);
        }
        System.out.println("-------------------------");

        // Collections只能够对单列集合操作
        // Collections不可以对Map接口下的子类做操作

        // 对集合中的元素反转
        Collections.reverse(integers);
        for (Integer i : integers
        ) {
            System.out.println(i);
        }
        System.out.println("-------------------------");

        // 对集合中的元素随机排列
        Collections.shuffle(integers);
        for (Integer i : integers
        ) {
            System.out.println(i);
        }

    }
}
