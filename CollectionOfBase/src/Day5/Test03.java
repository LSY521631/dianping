package Day5;

public class Test03 {
    public static void main(String[] args) {
        MyHashSet<String> myHashSet = new MyHashSet<>();
        myHashSet.add("hello1");
        myHashSet.add("hello2");
        myHashSet.add("hello1");
        System.out.println(myHashSet);

    }
}
