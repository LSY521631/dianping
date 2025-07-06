package Day5;

import java.util.HashSet;
import java.util.Iterator;

public class Test04 {
    public static void main(String[] args) {


        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("hello1");
        hashSet.add("hello2");
        hashSet.add("hello3");
        hashSet.add("hello4");
        hashSet.add("hello5");
        for (String s : hashSet
        ) {
            System.out.println(s);

        }
    }
}

