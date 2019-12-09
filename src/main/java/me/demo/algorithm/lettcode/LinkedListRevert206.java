package me.demo.algorithm.lettcode;

import java.util.Stack;

/**
 * 反转一个单链表。
 * 示例:
 * 输入: 1->2->3->4->5->NULL
 * 输出: 5->4->3->2->1->NULL
 * 进阶:
 * 你可以迭代或递归地反转链表。你能否用两种方法解决这道题？
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/reverse-linked-list
 */
class LinkedListRevert206 {
    public static void main(String[] args) {
        int[] array = new int[]{3, 2, 0, 4};
//        int[] array = new int[]{1};
//        int[] array = new int[]{1, 2};
//        int[] array = new int[]{};

        ListNode head = newSingleLinkedList(array, false);
        ListNode cycleStartNode = reverseList2(head);
        if (cycleStartNode != null) {
            System.out.println(cycleStartNode.val);
        }
    }

    /**
     * 迭代实现
     *
     * @param head 链表头节点
     * @return 反转后链表头节点
     */
    public static ListNode reverseList(ListNode head) {
        //上一个节点
        ListNode pre = null;
        //当前节点
        ListNode cur = head;
        //临时节点
        ListNode tmp = null;
        while (cur != null) {
            //后续节点的指针
            tmp = cur.next;
            //当前节点的next指向上一个节点->反转
            cur.next = pre;
            //上一个节点=当前节点
            pre = cur;
            //下一次从当前节点开始，新的链表引用
            cur = tmp;
        }
        return pre;
    }


    /**
     * 删除链表节点实现
     * 1.开始遍历：链表不为空或节点数大于1；
     * 2.每次删除next节点，并将删除节点插入到链表的头部
     * 3.将链表head节点指向执行剪切操作后的链表；
     * 3.结束遍历：到达链表尾部时；
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     *
     * @param head 链表头节点
     * @return 反转后链表头节点
     */
    public static ListNode reverseList2(ListNode head) {
        //链表遍历指针
        ListNode p = head;
        ListNode tmp = null;
        while (p != null && p.next != null) {
            //删除next，指向next.next
            tmp = p.next;
            p.next = p.next.next;
            //插入next，指向头
            tmp.next = head;
            //链表head指向执行剪切操作后的链表
            head = tmp;
        }
        return head;
    }

    /**
     * 栈实现
     * 时间复杂度：O(n*2)
     * 空间复杂度：O(n)
     *
     * @param head 链表头节点
     * @return 反转后链表头节点
     */
    public static ListNode reverseList3(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        Stack<ListNode> stack = new Stack<>();
        while (head != null) {
            stack.push(head);
            head = head.next;
        }
        ListNode cur = stack.pop();
        cur.next = null;
        //倒置后的链表
        ListNode newHead = cur;
        while (!stack.isEmpty()) {
            ListNode tmp = stack.pop();
            tmp.next = null;
            newHead.next = tmp;
            newHead = newHead.next;
        }
        return cur;
    }

    /***
     * 根据数组构建链表
     * @param vals 数组
     * @param cycle 是否循环链表
     * @return 链表头节点
     */
    public static ListNode newSingleLinkedList(int[] vals, boolean cycle) {
        if (vals.length == 0) {
            return null;
        }
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
        if (cycle) {
//            nodes[nodes.length - 1].next = nodes[0];
            nodes[nodes.length - 1].next = nodes[1];
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
