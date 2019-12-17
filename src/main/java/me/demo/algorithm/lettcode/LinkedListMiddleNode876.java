package me.demo.algorithm.lettcode;

import me.demo.algorithm.lettcode.linkedlist.ListNode;
import me.demo.algorithm.lettcode.linkedlist.SingleLinkkedList;

/**
 * 给定一个带有头结点 head 的非空单链表，返回链表的中间结点。
 * 如果有两个中间结点，则返回第二个中间结点。
 */
class LinkedListMiddleNode876 {
    public static void main(String[] args) {
        //只有1个节点
        int[] array = new int[]{1, 2};
        ListNode head = new SingleLinkkedList(array).getHead();
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

}
