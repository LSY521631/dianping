package Day7;

public class Test02 {
    public static void main(String[] args) {
        // hashcode属于object父类中
        // java虚拟机给每个对象生成一个hashcode值,为整数类型,int类型
        String str1 = "lsy";
        String str2 = "lsy";
        Object o1 = new Object();
        System.out.println(str1.equals(str2));
        System.out.println(str1.hashCode());
        System.out.println(str2.hashCode());
        // hashcode生成规则:堆内存地址转化成整数类型
        System.out.println(o1.hashCode());
        /**
         * 1.如果equals方法比较两个对象相等,则HashCode值也一定相等;
         * 若只重写equals方法而不重写HashCode方法,两对象的HashCode值不同，违背了第一条原则
         * 2.但是两个对象的HashCode值相等b,不代表使用equals比较也相等
         * 3.如果两个对象的HashCode值相等但是值是不同; 专业术语:Hash冲突问题。
         */
        String strA = "a";
        Integer int97 = 97;
        // 整数类型 =包装类Integer的hashcode值是多少呢? 为该整数类型值（97）
        System.out.println(strA.hashCode());
        System.out.println(int97.hashCode());

    }
}
