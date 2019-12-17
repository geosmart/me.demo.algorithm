package me.demo.algorithm.lettcode;

import me.demo.algorithm.lettcode.linkedlist.ListNode;
import me.demo.algorithm.lettcode.linkedlist.SingleLinkkedList;

/**
 * 反转从位置 m 到 n 的链表。请使用一趟扫描完成反转。
 * 说明:
 * 1 ≤ m ≤ n ≤ 链表长度。
 * 示例:
 * 输入: 1->2->3->4->5->NULL, m = 2, n = 4
 * 输出: 1->4->3->2->5->NULL
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/reverse-linked-list-ii
 */
class LinkedListRevert92 {

    public static void main(String[] args) {
        int[] array = new int[]{1, 2, 3, 4, 5};

        SingleLinkkedList list = new SingleLinkkedList(array);
        ListNode listNode = reverseBetween(list.getHead(), 2, 4);
        listNode.printNode();
    }

    public static ListNode reverseBetween(ListNode head, int m, int n) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        //遍历指针
        ListNode cur = dummy;
        //反转的前1个节点
        ListNode pre = null;
        //定义m的前1个节点，n的最后1个节点
        ListNode front = null, last = null;

        //找到m-n之间的节点，并反转
        for (int i = 1; i < m; i++) {
            cur = cur.next;
        }
        pre = cur;
        last = cur.next;
        for (int i = m; i <= n; i++) {
            cur = pre.next;
            pre.next = cur.next;
            cur.next = front;
            front = cur;
        }
        cur = pre.next;
        pre.next = front;
        last.next = cur;
        return dummy.next;
    }
}
