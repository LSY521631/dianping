package Day4;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Test01 {
    public static void main(String[] args) {

        /**
         * LinkedList底层基于链表实现增删效非常高查询效是非常低
         * 底层基于双向链表实现
         */
        List<String> linkedList = new LinkedList<>();
        linkedList.add("hello1");
        linkedList.add("hello2");
        linkedList.add("hello3");
        linkedList.add("hello4");
        System.out.println(linkedList.size());
        Iterator<String> iterator1 = linkedList.iterator();
        while (iterator1.hasNext()) {
            System.out.println(iterator1.next());

        }
        linkedList.remove(0);
        System.out.println("删除之后");
        Iterator<String> iterator2 = linkedList.iterator();
        while (iterator2.hasNext()) {
            System.out.println(iterator2.next());
        }
    }
}
