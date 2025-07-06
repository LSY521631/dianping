package Day10;

public class Test02 {
    public static void main(String[] args) {
        MyLinkedHashSet<String> stringMyLinkedHashSet = new MyLinkedHashSet<>();
        for (int i = 0; i < 100; i++) {
            stringMyLinkedHashSet.add(i + "");
        }
        System.out.println(stringMyLinkedHashSet);
    }
}
