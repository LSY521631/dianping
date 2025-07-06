package Day7;

import java.util.Objects;

public class MyEntity {
    private String name;
    private Integer age;

    public MyEntity(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        // 自己手写重写equals方法
        // 重写了父类中equals比较两个对象中成员属性值是否相同
        if (this == o) {
            // 先判断两个对象内存地址是否相等,若相等直接返回true
            return true;
        }
        // 比较两个对象的成员属性值是否相等
        // 强转
        MyEntity myEntity = (MyEntity) o;
        if (this.name.equals(myEntity.name) && this.age.equals(myEntity.age)) {
            // this代指调用该方法的对象，本例为Test01中的myEntity1
            return true;
        }

        return false;

        // 系统自带的
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        MyEntity myEntity = (MyEntity) o;
//        return Objects.equals(name, myEntity.name) && Objects.equals(age, myEntity.age);
    }

    @Override
    public int hashCode() {
        // 自己手写
        return this.name.hashCode() + this.age.hashCode();
        // 系统自带的
//        return Objects.hash(name, age);
    }
}
