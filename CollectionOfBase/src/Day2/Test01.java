package Day2;

import java.util.ArrayList;

public class Test01 {
    public static void main(String[] args) {

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("hello");
        arrayList.add("world");
        String string = arrayList.get(1);
        System.out.println(string);
    }
}
