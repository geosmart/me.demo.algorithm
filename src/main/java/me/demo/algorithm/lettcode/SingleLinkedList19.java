package me.demo.algorithm.lettcode;

/**
 * 给定一个链表，删除链表的倒数第 n 个节点，并且返回链表的头结点。
 * 示例：
 * 给定一个链表: 1->2->3->4->5, 和 n = 2.
 * 当删除了倒数第二个节点后，链表变为 1->2->3->5.
 * 说明： * 给定的 n 保证是有效的。
 * 进阶： * 你能尝试使用一趟扫描实现吗？
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/remove-nth-node-from-end-of-list
 */
class SingleLinkedList19 {
    public static void main(String[] args) {
        int[] array = new int[]{1, 2, 3, 4, 5};
        ListNode head = newSingleLinkedList(array);
        head.printNode();
        head = removeNthFromEnd2(head, 2);
        head.printNode();
        head = removeNthFromEnd(head, 2);
        head.printNode();
    }

    /**
     * 2次遍历删倒数第n个节点
     * 第1次遍历获得链表长度L，L-n+1即为要删的节点
     * 第2次遍历到第L-n节点，指向L-n+2节点，完即删L-n+1节点；
     *
     * @param head 头节点指针
     * @param n    倒数第n个节点
     * @return 头部指针
     */
    public static ListNode removeNthFromEnd2(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode p = dummy;
        int len = 0;
        while (p.next != null) {
            p = p.next;
            len++;
        }
        p = dummy;
        len -= n;
        while (len > 0) {
            len--;
            p = p.next;
        }
        p.next = p.next.next;
        return dummy.next;
    }

    /**
     * 快慢指针，遍历一次，找到倒数第k+1个节点，然后删除第k个节点
     * 1.从链表头部开始遍历，s指针比q指针慢n+1步（间隔n个节点）
     * 2.快指针q到达链表尾部(null)时，慢指针s到达倒数第k+1个节点
     * 3.删除第k个节点：将第k+1个节点的next指向第k+1.next.next
     *
     * @param head 头节点指针
     * @param n    倒数第n个节点
     * @return 头部指针
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        //虚拟结点，用来简化某些极端情况，例如列表中只含有一个结点，或需要删除列表的头部。
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode q = dummy;
        ListNode s = dummy;
        for (int i = 0; i < n + 1; i++) {
            q = q.next;
        }
        while (q != null) {
            s = s.next;
            q = q.next;
        }
        s.next = s.next.next;
        return dummy.next;
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
