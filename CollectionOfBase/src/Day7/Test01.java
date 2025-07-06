package Day7;

public class Test01 {
    public static void main(String[] args) {


        String str1 = "lsy";
        String str2 = "lsy";
        System.out.println(str1.equals(str2));

        MyEntity myEntity1 = new MyEntity("lsy", 22);
        MyEntity myEntity2 = new MyEntity("lsy", 22);
        // myEntity1==myEntity2；equals底层在比较两个对象的内存地址是否相同
        System.out.println(myEntity1.equals(myEntity2));
        System.out.println(myEntity1.hashCode());
        System.out.println(myEntity2.hashCode());
    }
}
