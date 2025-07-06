package Day2;

import java.util.ArrayList;

public class Test02 {
    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("0");
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        arrayList.add("4");

        arrayList.remove(1);
        System.out.println(arrayList.get(1));
        System.out.println(arrayList.get(3));
    }
}
