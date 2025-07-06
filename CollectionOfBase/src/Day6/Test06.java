package Day6;

import java.util.HashMap;

public class Test06 {
    public static void main(String[] args) {

        HashMap<String, Stu> stuHashMap = new HashMap<>();
        stuHashMap.put("32102132", new Stu("LSY", 22));
        stuHashMap.put("32102133", new Stu("ZGL", 23));
        stuHashMap.put("32102135", new Stu("YSJ", 32));
        // 遍历集合所有元素
        for (String key : stuHashMap.keySet()
        ) {
            Stu stu = stuHashMap.get(key);
            System.out.println("学号:" + key + ",姓名:" + stu.getName() + ",年龄:" + stu.getAge());
        }
    }
}
