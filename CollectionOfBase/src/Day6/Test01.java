package Day6;

import java.util.HashSet;

public class Test01 {
    public static void main(String[] args) {
        /**
         * HashMap底层是基于HashMap集合实现的
         * HashMap的底层是不允许key重复
         * HashMap的底层add方法是如何实现新增元素保证key不允许重复的呢？？？
         * ((k=p.key)== key || (key != null && key.equals(k))))
         * 第一次调用add方法将s1对象存入在 HashMap集合
         * 第二次调用add方法时存入s2对象进行比较s1==s2(=false,比较的是内存地址)||s1.equals(s2)
         * equals方法的底层在没有重写时，也是比较内存地址(s1==s2=false)
         * 判断key在HashMap集合中是否存在，如果存在就不能继续新增;如果不存在就继续插入
         */
        HashSet<Student> students = new HashSet<>();
        Student s1 = new Student("32102132", "LSY");
        Student s2 = new Student("32102132", "LSY");
        Student s3 = new Student("32102132", "LSY");
        Student s4 = new Student("32102132", "LSY");
        // s1的内存地址赋值给s5;s1与s5的内存地址是相同的
        Student s5 = s1;
        Student s6 = new Student("32102131", "LCY");
        students.add(s1);
        students.add(s2);
        students.add(s3);
        students.add(s4);
        students.add(s5);
        students.add(s6);

        System.out.println(students.size());
        for (Student stu : students
        ) {
            System.out.println(stu);
        }

    }
}
