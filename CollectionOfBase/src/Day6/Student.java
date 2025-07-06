package Day6;

import java.util.Objects;

public class Student {

    // 学生学号
    private String number;
    // 学生姓名
    private String name;

    // 生成的有参构造方法
    public Student(String number, String name) {
        this.number = number;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
    /**
     * HashSet去除重复数据需要重写元素对应的equals和ashCode的方法
     * idea可以自动可以生成
     * 比较两个对象成员属性值是否是一样的
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(number, student.number) && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, name);
    }
}
