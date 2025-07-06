package Day1;

import java.util.ArrayList;

public class Test01 {
    public static void main(String[] args) {

        // 懒加载的形式 真正需要使用的时候才会加载
        // 数组初始化没有容量
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("a");
        arrayList.add("a");
        arrayList.add("a");

        System.out.println(arrayList);


    }
}
