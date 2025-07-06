package Day4;

public class MyLinkedList<E> {
    private Node<E> first;// 第一个节点
    private Node<E> last;// 链表最后一个节点
    int size = 0;// LinkedList集合中节点的数量

    // LinkedList底层是基于链表实现
    private static class Node<E> {
        private E item; // 当前节点的值
        private Node<E> next;// 下一个节点
        private Node<E> prev;// 上一个节点

        /**
         * @param pred 当前节点的上一个节点
         * @param item 当前节点的值
         * @param next 当前节点的下一个节点
         */
        public Node(Node<E> pred, E item, Node<E> next) {
            this.item = item;
            this.next = next;
            this.prev = pred;
        }


    }


    public void add(E e) {

        // 获取当前链表中的最后一个节点
        Node<E> l = last;
        // 创建一个新的node节点，且新的节点的上一个节点为原来的last,.next为null
        Node<E> newNode = new Node<>(l, e, null);
        // 新的节点就变成了最后的节点
        last = newNode;
        if (l == null) {
            // 如果链表中没有最后一个节点，则该链表为空,则第一个节点为新建的节点
            first = newNode;
        } else {
            // 原来最后一个节点的.next是新节点
            l.next = newNode;
        }
        size++;
    }


    public static void main(String[] args) {
        MyLinkedList<String> myLinkedList = new MyLinkedList<>();
        myLinkedList.add("hello1");
        myLinkedList.add("hello2");
        System.out.println(myLinkedList.get(0));
        myLinkedList.remove(0);
        System.out.println(myLinkedList.get(0));
    }

    // 封装get方法
    public E get(int index) {
        return node(index).item;
        // 下标越界还有报错，此处没写
    }

    /**
     * 根据index查询链表中对应的node节点
     *
     * @param index
     */
    Node<E> node(int index) {

        if (index < index << 1) {
            // 查询链表中间值的左边,左边从头开始
            Node<E> f = first;
            for (int i = 0; i < index; i++) {
                f = f.next;
            }
            return f;
        } else {
            // 查询链表中间值的右边
            Node<E> l = last;
            for (int i = size - 1; i > index; i--) {
                l = l.prev;

            }
            return l;
        }

    }

    public E remove(int index) {
        return unlink(node(index));
    }

    private E unlink(Node<E> node) {
        // 1.根据index查询对应的node节点，时间复杂度已经是o(n)
        // 2.链表的删除效率高，比ArrayList效率高,ArrayList的删除是移动数组

        final E element = node.item; // 获取删除节点的元素值
        Node<E> prev = node.prev;   // 删除节点的上一个节点
        Node<E> next = node.next;   // 删除节点的下一个节点

        // 如果删除的节点的上一个节点为null
        if (prev == null) {
            // 则删除的节点为头节点
            first = next;
        } else { // 如果删除的节点的上一个节点不为null
            // 删除的节点的上一个节点.next==删除节点的下一个节点
            prev.next = next;
            next.prev = null; // ?

        }
        // 如果删除的节点的下一个节点为null
        if (next == null) {
            // 则删除的节点为尾节点,则尾节点改成删除节点的上一个节点
            last = prev;
        } else {
            // 如果删除的不是尾节点，则删除的下一个节点的.prev==删除的上一个节点
            next.prev = prev;
            next.next = null; // gc回收??
        }
        node.item = null;// 回收
        size--;
        return element;
    }
}
