package me.demo.algorithm.lettcode;

import me.demo.algorithm.lettcode.linkedlist.ListNode;
import me.demo.algorithm.lettcode.linkedlist.SingleLinkkedList;

/**
 * 2. 两数相加
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 * <p>
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 * <p>
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * <p>
 * 示例：
 * <p>
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-two-numbers
 */
class LinkedListAddTwoNumber {
    public static void main(String[] args) {
        ListNode l1 = new SingleLinkkedList(new int[]{2, 4, 3}).getHead();
        ListNode l2 = new SingleLinkkedList(new int[]{5, 6, 4}).getHead();
        ListNode head = addTwoNumbers(l1, l2);
        head.printNode();
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode p = null;
        ListNode a = l1;
        ListNode b = l2;
        //定义一个进位标志
        int carry = 0;
        while (a != null || b != null) {
            //步骤1：a和b节点的值相加，如果有进位还要加上进位的值
            int val = (a == null ? 0 : a.val) + (b == null ? 0 : b.val) + carry;
            //步骤1：进位处理，和>10进1，当前值对10求余
            carry = val >= 10 ? 1 : 0;
            //不管有没有进位，val都应该小于10
            val = val % 10;
            //步骤3：将结果节点赋值给新节点
            p = (a == null ? b : a);
            p.val = val;
            //步骤4：a和b指针都前进一位
            a = (a == null ? null : a.next);
            b = (b == null ? null : b.next);
            //步骤5：根据a和b是否为空，p指针也前进一位
            p.next = (a == null ? b : a);
        }
        //不要忘记最后的边界条件，如果循环结束carry>0说明有进位需要处理这个条件
        if (carry > 0) {
            p.next = new ListNode(1);
        }
        //每次迭代实际上都是将val赋给a指针的，所以最后返回的是l1
        return l1;
    }
}
