package Day7;

import java.util.HashMap;

public class Test03 {
    public static void main(String[] args) {
        MyEntity myEntity1 = new MyEntity("lsy", 22);
        HashMap<MyEntity, String> myEntityStringHashMap = new HashMap<>();
        myEntityStringHashMap.put(myEntity1, "lsy");
        System.out.println(myEntityStringHashMap.get("lsy"));

    }
}
