package Day7;

import java.util.HashMap;

public class Test04 {
    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            hashMap.put("lsy" + i, "" + i);
        }

        hashMap.forEach((k, v) -> {
            System.out.println(k + "," + v);
        });
    }
}
