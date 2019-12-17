package me.demo.algorithm.lettcode;

import me.demo.algorithm.lettcode.linkedlist.ListNode;

import java.util.HashSet;

/**
 * 给定一个链表，判断链表中是否有环。
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/linked-list-cycle
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
class LinkedListCycle141 {
    public static void main(String[] args) {
//        int[] array = new int[]{3, 2, 0, 4};
//        int[] array = new int[]{1};
        int[] array = new int[]{1, 2};
//        int[] array = new int[]{};

        ListNode head = newSingleLinkedList(array, true);
        boolean hasCycle = hasCycle3(head);
        System.out.println(hasCycle);
    }

    /**
     * 链表中是否有环
     * 1.定义2个哨兵节点指向链表head，快指针比慢指针快2步
     * 2.遍历链表，当快指针指想=向链表head时，返回true
     * 空间复杂度：O(1)，使用了慢指针和快指针两个结点，所以空间复杂度为 O(1)。
     * 时间复杂度：O(n)，将 n 设为链表中结点的总数。
     * 为了分析时间复杂度，分别考虑下面两种情况。
     * 1.链表中不存在环：快指针将会首先到达尾部，其时间取决于列表的长度，也就是 O(n)。
     * 2.链表中存在环：将慢指针的移动过程划分为两个阶段：非环部分与环形部分：
     * 慢指针在走完非环部分阶段后将进入环形部分：此时快指针已经进入环中，迭代次数=非环部分长度=N；
     * 两个指针都在环形区域中：需要经过 {二者之间距离}/{速度差值}次循环后，快指针可以追上慢指针
     * ​这个距离几乎就是 {环形部分长度 K}，我们得出这样的结论{迭代次数} = {环形部分长度 K}
     * 所以总时间复杂度为 O(N+K)，也就是 O(n)。
     *
     * @param head 链表头节点
     * @return 是否循环链表
     */
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode s = head;
        ListNode q = head.next;
        while (s != q) {
            if (q == null || q.next == null) {
                return false;
            }
            s = s.next;
            q = q.next.next;
        }
        return true;
    }

    /**
     * 链表中是否有环
     * 1.定义2个哨兵节点指向链表head，快指针比慢指针快2步
     * 2.遍历链表，当快指针指想=向链表head时，返回true；快指针到达链尾，则无环终止
     *
     * @param head 链表头节点
     * @return 是否循环链表
     */
    public static boolean hasCycle2(ListNode head) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode s = dummy;
        ListNode q = dummy.next;

        while (q != null) {
            if (s == q) {
                return true;
            }
            //快指针到达链尾，无环终止
            if (q.next == null) {
                break;
            }
            s = s.next;
            q = q.next.next;
        }
        return false;
    }

    /**
     * 链表中是否有环
     * 1.定义HashSet存储链表节点
     * 2.遍历链表，如果链表的下一个节点存在链表中，则表示存在环，返回true
     * 时间复杂度：O(n)，对于含有 n 个元素的链表，我们访问每个元素最多一次。添加一个结点到哈希表中只需要花费 O(1) 的时间。
     * 空间复杂度：O(n)，空间取决于添加到哈希表中的元素数目，最多可以添加 n 个元素。
     *
     * @param head 链表头节点
     * @return 是否循环链表
     */
    public static boolean hasCycle3(ListNode head) {
        HashSet<ListNode> nodes = new HashSet<>();
        while (head != null) {
            if (nodes.contains(head)) {
                return true;
            } else {
                nodes.add(head);
            }
            head = head.next;
        }
        return false;
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
//            nodes[nodes.length - 1].next = nodes[1];
            nodes[nodes.length - 1].next = nodes[0];
        }
        return nodes[0];
    }

}
