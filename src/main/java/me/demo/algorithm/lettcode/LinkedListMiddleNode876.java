package me.demo.algorithm.lettcode;

/**
 * 给定一个带有头结点 head 的非空单链表，返回链表的中间结点。
 * 如果有两个中间结点，则返回第二个中间结点。
 */
class LinkedListMiddleNode876 {
    public static void main(String[] args) {
        //只有1个节点
        int[] array = new int[]{1, 2};
        ListNode head = newSingleLinkedList(array);
        head.printNode();
        head = middleNode(head);
        head.printNode();
    }

    /**
     * 快慢指针求中点
     * 快指针走2步，慢指针走1步，快指针到达链尾时，慢指针停止；
     *
     * @param head 头节点指针
     * @return 头部指针
     */
    public static ListNode middleNode(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode q = dummy;
        ListNode s = dummy;
        while (q != null) {
            q = q.next;
            s = s.next;
            if (q == null) {
                break;
            }
            q = q.next;
        }
        return s;
    }

    /***
     * 根据数组构建链表
     * @param vals 数组
     * @return 链表头节点
     */
    public static ListNode newSingleLinkedList(int[] vals) {
        ListNode[] nodes;
        nodes = new ListNode[vals.length];
        for (int i = 0; i < vals.length; i++) {
            if (nodes[i] == null) {
                nodes[i] = new ListNode(vals[i]);
            }
            if (i < vals.length - 1) {
                nodes[i + 1] = new ListNode(vals[i + 1]);
                nodes[i].next = nodes[i + 1];
            }
        }
        return nodes[0];
    }

    /***
     * 单链表节点
     */
    public static class ListNode {
        int val;
        ListNode next;

        public ListNode(int val) {
            this.val = val;
        }

        public void printNode() {
            ListNode printNode = this;
            StringBuilder sb = new StringBuilder();
            while (printNode != null) {
                sb.append("-->").append(printNode.val);
                printNode = printNode.next;
            }
            System.out.println(sb);
        }
    }
}
