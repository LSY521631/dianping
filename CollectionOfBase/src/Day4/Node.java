package Day4;

public class Node<E> {

    // node节点元素值
    private E v;
    // 当前节点指向下一个节点
    Node<E> next;
    // 当前节点指向上一个节点
    Node<E> pred;

    public static void main(String[] args) {
        Node<String> stringNode3 = new Node<>();
        stringNode3.v = "c";
        Node<String> stringNode2 = new Node<>();
        stringNode2.v = "b";
        stringNode2.next = stringNode3;
        Node<String> stringNode1 = new Node<>();
        stringNode1.v = "a";
        stringNode1.next = stringNode2;


        // 双向链表
        stringNode3.pred = stringNode2;
        stringNode2.pred = stringNode1;

        System.out.println(stringNode1);

    }


}
