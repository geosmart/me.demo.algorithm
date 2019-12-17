package me.demo.algorithm.lettcode;

import me.demo.algorithm.lettcode.linkedlist.ListNode;
import me.demo.algorithm.lettcode.linkedlist.SingleLinkkedList;

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
        int[] array = new int[]{1, 2, 3, 4};
//        int[] array = new int[]{1};
//        int[] array = new int[]{1, 2};
//        int[] array = new int[]{};

        ListNode head = new SingleLinkkedList(array).getHead();
        ListNode cycleStartNode = reverseList(head);
        if (cycleStartNode != null) {
            cycleStartNode.printNode();
        }
    }

    /**
     * 迭代实现
     *
     * @param head 链表头节点
     * @return 反转后链表头节点
     */
    public static ListNode reverseList1(ListNode head) {
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

    /**
     * 递归实现
     * 时间复杂度：O(n)，假设 n 是列表的长度，那么时间复杂度为 O(n)。
     * 空间复杂度：O(n)，由于使用递归，将会使用隐式栈空间。递归深度可能会达到 n 层。
     *
     * @param head 链表头节点
     * @return 反转后链表头节点
     */
    public static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        //p为最后一个节点
        ListNode p = reverseList(head.next);
        //反转
        head.next.next = head;
        //当前next设为null，防止循环指针
        head.next = null;
        return p;
    }

}
