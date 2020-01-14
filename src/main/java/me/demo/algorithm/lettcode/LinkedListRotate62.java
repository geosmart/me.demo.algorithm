package me.demo.algorithm.lettcode;

import me.demo.algorithm.lettcode.linkedlist.ListNode;
import me.demo.algorithm.lettcode.linkedlist.SingleLinkkedList;

/**
 * 给定一个链表，旋转链表，将链表每个节点向右移动 k 个位置，其中 k 是非负数。
 *
 * 示例 1:
 *
 * 输入: 1->2->3->4->5->NULL, k = 2
 * 输出: 4->5->1->2->3->NULL
 * 解释:
 * 向右旋转 1 步: 5->1->2->3->4->NULL
 * 向右旋转 2 步: 4->5->1->2->3->NULL
 * 示例 2:
 *
 * 输入: 0->1->2->NULL, k = 4
 * 输出: 2->0->1->NULL
 * 解释:
 * 向右旋转 1 步: 2->0->1->NULL
 * 向右旋转 2 步: 1->2->0->NULL
 * 向右旋转 3 步: 0->1->2->NULL
 * 向右旋转 4 步: 2->0->1->NULL
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/rotate-list
 */
class LinkedListRotate62 {
    public static void main(String[] args) throws Exception {
//        int[] array = new int[]{1, 2, 3, 4, 5};
//        int[] array = new int[]{1};
        int[] array = new int[]{0, 1, 2};
//        int[] array = new int[]{};
        SingleLinkkedList list = new SingleLinkkedList(array);
        ListNode listNode = rotateRight(list.getHead(), 4);
        if (listNode != null) {
            listNode.printNode();
        }
    }

    /***
     * 旋转链表
     * 1.求链表长度
     * 2.计算实际需要移动的位置k
     * 3.快慢指针法获取倒数第k个节点
     * 4.链表旋转：k节点作为新的链表头，k-1节点作为链表尾，中间部分以循环链表方式连接
     * @param head 链表头
     * @param k 移动k个位置
     * @return
     */
    public static ListNode rotateRight(ListNode head, int k) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode fast = dummy.next;
        int n = 1;
        //快指针，求链表长度和tail节点
        while (fast.next != null) {
            fast = fast.next;
            n++;
        }
        //n次移动等于没移动
        if (k % n == 0) {
            return dummy.next;
        }
        //当成循环链表，求余后的k为实际需移动的位置数
        k = k % n;
        ListNode slow = dummy.next;
        //求倒数第k个节点
        for (int i = 0; i < n - k - 1; i++) {
            slow = slow.next;
        }
        ListNode newHead = slow.next;
        slow.next = null;
        //连接原链表的头部
        fast.next = head;
        return newHead;
    }

    /***
     * 旋转链表
     * 1.求链表长度
     * 2.计算实际需要移动的位置k
     * 3.快慢指针法获取倒数第k个节点
     * 4.链表旋转：k节点作为新的链表头，k-1节点作为链表尾，中间部分以循环链表方式连接
     * @param head 链表头
     * @param k 移动k个位置
     * @return
     */
    public static ListNode rotateRightV1(ListNode head, int k) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode tmp = dummy.next;
        int n = 0;
        //求链表长度
        while (tmp != null) {
            tmp = tmp.next;
            n++;
        }
        if (n == 0 || k % n == 0) {
            return dummy.next;
        }
        //当成循环链表，n次移动等于没移动，求余后的k为实际需移动的位置数
        k = k % n;
        //快慢指针求倒数第k个节点
        ListNode slow = dummy;
        ListNode fast = dummy;
        while (k > 0) {
            fast = fast.next;
            k--;
        }
        while (fast.next != null) {
            slow = slow.next;
            fast = fast.next;
        }
        //kNode为返回结果的head
        ListNode kNode = slow.next;
        //原slow节点即为tail
        slow.next = null;
        //连接原链表的头部
        fast.next = head;
        return kNode;
    }
}
