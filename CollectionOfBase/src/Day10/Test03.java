package Day10;

import java.util.HashSet;
import java.util.Random;

public class Test03 {
    public static void main(String[] args) {
        // 1.创建HashSet集合
        HashSet<Integer> integers = new HashSet<>();
        // 2.生成的随机数存放在HashSet集合中
        Random random = new Random();
        // 3.通过循环控制,将生成随机数存放到HashSet集合中
        while (integers.size() < 10) {
            //将生成的随机数存放在HashSet集合,HashSet不允许元素重复
            int number = random.nextInt(20) + 1;
            integers.add(number);

        }

        System.out.println("生成的随机数:");
        for (Integer num : integers
        ) {
            System.out.println(num);
        }
    }
}
