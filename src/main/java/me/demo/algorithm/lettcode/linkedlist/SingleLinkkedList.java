package me.demo.algorithm.lettcode.linkedlist;

public class SingleLinkkedList {
    private ListNode head;

    public ListNode getHead() {
        return head;
    }

    public SingleLinkkedList() {
        this.head = new ListNode(null);
    }

    public SingleLinkkedList(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                head = new ListNode(array[i]);
            } else {
                addNode(new ListNode(array[i]));
            }
        }
    }


    public void addNode(ListNode node) {
        ListNode p = head;
        while (p.next != null) {
            p = p.next;
        }
        p.next = node;
    }

    /***
     * 找到节点，再在原链表中删节点
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * @param node 链表节点
     */
    public void removeNode(ListNode node) {
        ListNode find = head;
        ListNode p = head;
        int i = 0;
        while (find != null) {
            //找到节点
            if (node.equals(find)) {
                //从原链表中删除节点
                for (int j = 0; j < i; j++) {
                    if (j == i - 1) {
                        p.next = p.next.next;
                        break;
                    }
                    p = p.next;
                }
                break;
            }
            find = find.next;
            i++;
        }
    }
}