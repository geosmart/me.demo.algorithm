package me.demo.algorithm.lettcode;

import me.demo.algorithm.lettcode.linkedlist.ListNode;

import java.util.HashSet;
import java.util.Set;

/**
 * 给定一个链表，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。
 * 说明：不允许修改给定的链表。
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/linked-list-cycle-ii
 */
class LinkedListCycle142 {
    public static void main(String[] args) {
        int[] array = new int[]{3, 2, 0, 4};
//        int[] array = new int[]{1};
//        int[] array = new int[]{1, 2};
//        int[] array = new int[]{};

        ListNode head = newSingleLinkedList(array, true);
        ListNode cycleStartNode = detectCycle(head);
        if (cycleStartNode != null) {
            System.out.println(cycleStartNode.val);
        }
    }

    /**
     * Floyd求链表中环的入口节点
     * 1.定义快慢指针求相遇点，快指针比慢指针快2倍；
     * 2.在相遇点，定义1个指针从头，1个指针从相遇点，一起开始逐步前进，下次相遇点即为环入口；
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     *
     * @param head 链表头节点
     * @return 是否循环链表
     */
    public static ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }
        //寻找相遇
        ListNode slow = head.next;
        ListNode fast = head.next.next;
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                return null;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        //寻找环入口
        ListNode meetSlow = head;
        while (meetSlow != fast) {
            fast = fast.next;
            meetSlow = meetSlow.next;
        }
        return meetSlow;
    }

    /**
     * 求链表中环的入口节点
     * 用Set 保存已经访问过的节点，遍历整个列表并返回第一个出现重复的节点。
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     *
     * @param head 链表头节点
     * @return 是否循环链表
     */
    public static ListNode detectCycle2(ListNode head) {
        Set<ListNode> nodes = new HashSet<>();
        while (head != null) {
            if (nodes.contains(head)) {
                return head;
            }
            nodes.add(head);
            head = head.next;
        }
        return null;
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
}
