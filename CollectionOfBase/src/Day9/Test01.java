package Day9;

import java.util.HashMap;

public class Test01 {
    public static void main(String[] args) {

        /**
         * HashMap的键值key是否可以存放自定义的对象呢?---可以的
         * 如果HashMap的key存放我们自定义的对象
         * 注意键值key,即我们自定义的对象
         * 需要重写equals和hascCode方法
         */
        HashMap<String, String> stringHashMap = new HashMap<>();
        HashMap<Student, String> hashMap = new HashMap<>();
        // HashMap集合中key是不允许重复,如果发生了相同的key,则认为是在对value值做修改
        stringHashMap.put("lsy", "123");
        stringHashMap.put("lsy", "1234");
        System.out.println(stringHashMap.size()); // =1

        hashMap.put(new Student("lsy", 22), "12345");
        hashMap.put(new Student("lsy", 22), "12346");
        hashMap.put(new Student("zgl", 22), "111111");
        System.out.println(hashMap.size()); // =2 重写equals和hascCode方法后size为1

        // 遍历输出学生的手机号码
        for (Student stu : hashMap.keySet()) {
            String phone = hashMap.get(stu);
            System.out.println("key:" + stu.toString() + ",value:" + phone);
        }

    }
}
