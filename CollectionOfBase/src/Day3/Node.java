package Day3;

public class Node<E> {

    // node节点元素值
    private E v;
    // 当前节点指向下一个节点
    Node<E> next;



    public static void main(String[] args) {
        Node<String> stringNode3 = new Node<>();
        stringNode3.v = "c";
        Node<String> stringNode2 = new Node<>();
        stringNode2.v = "b";
        stringNode2.next = stringNode3;
        Node<String> stringNode1 = new Node<>();
        stringNode1.v = "a";
        stringNode1.next = stringNode2;
        System.out.println(stringNode1);

        // 新增节点
        Node<String> stringNode4 = new Node<>();
        stringNode4.v = "d";
        addNote(stringNode3, stringNode4);
        ShowNode(stringNode1);

        System.out.println();
        // 删除node2节点
        stringNode1.next = stringNode3;
        ShowNode(stringNode1);

    }

    // 遍历链表的方法
    public static void ShowNode(Node<?> node) {

        Node<?> cuNode = node;
        while (cuNode != null) {
            System.out.print(cuNode.v);
            // 当前node节点的下一个节点赋值给cuNode
            cuNode = cuNode.next;
        }
    }

    // 新增节点方法
    // Node<String> tailNode ----尾节点
    // Node<String> newNode ----新增节点
    public static void addNote(Node<String> tailNode, Node<String> newNode) {
        tailNode.next = newNode;
    }

}
