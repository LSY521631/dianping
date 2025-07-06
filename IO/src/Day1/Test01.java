package Day1;

import java.io.File;

/**
 * 目标：掌握File创建对象，代表具体文件的方案
 */

public class Test01 {
    public static void main(String[] args) {
        // 1.创建一个file对象，代指某个具体的文件
        // File f1 = new File("C:\\Users\\刘善宇\\Desktop\\Test\\text1.txt");
        File f1 = new File("C:/Users/刘善宇/Desktop/Test/text1.txt");
        File f2 = new File(
                "C:" + File.separator + "Users" + File.separator + "刘善宇" + File.separator + "Desktop");
        // 文件大小,字节码表示
        long l = f1.length();
        System.out.println(l);
        // java中只会记录该文件夹的信息，不会取文件夹内所有文件的大小
        System.out.println(f2.length());

        // 注意:File对象可以指代一个不存在的文件路径
        File f3 = new File("D:/hello/test1.txt");
        System.out.println(f3.length());
        System.out.println(f3.exists()); // 返回false

        // 我现在要定位的文件是在模块中，应该怎么定位呢?
        // 绝对路径:带盘符的
        File f4 = new File("G:\\JavaProjects\\IO\\src\\Day1\\test1.txt");
        System.out.println(f4.length());
        // 相对路径(重点):不带盘符，默认是直接去工程下寻找文件的。
        File f5 = new File("src\\Day1\\test1.txt");
        System.out.println(f5.length());
    }

}