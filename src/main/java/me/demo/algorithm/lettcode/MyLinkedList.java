package me.demo.algorithm.lettcode;

import java.util.Objects;

/**
 * 实现单链表、循环链表、双向链表，支持增删操作
 */
class MyLinkedList {
    public static void main(String[] args) {
        int[] array = new int[]{1, 2, 3, 4};
//        int[] array = new int[]{1};
//        int[] array = new int[]{1, 2};
//        int[] array = new int[]{};
        SingleLinkkedList sList = new SingleLinkkedList();
        for (int i = 0; i < array.length; i++) {
            sList.addNode(new ListNode(array[i]));
        }
        sList.addNode(new ListNode(9));
        sList.removeNode(new ListNode(9));
        sList.head.printNode();
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

    public static class SingleLinkkedList {
        ListNode head;

        public SingleLinkkedList() {
            this.head = new ListNode(null);
        }

        public void addNode(ListNode node) {
            ListNode p = head;
            while (p.next != null) {
                p = p.next;
            }
            p.next = node;
        }

        /***
         * 找到节点，再在原链表中删节点
         * 时间复杂度：O(n)
         * 空间复杂度：O(1)
         * @param node 链表节点
         */
        public void removeNode(ListNode node) {
            ListNode find = head;
            ListNode p = head;
            int i = 0;
            while (find != null) {
                //找到节点
                if (node.equals(find)) {
                    //从原链表中删除节点
                    for (int j = 0; j < i; j++) {
                        if (j == i - 1) {
                            p.next = p.next.next;
                            break;
                        }
                        p = p.next;
                    }
                    break;
                }
                find = find.next;
                i++;
            }
        }
    }

    /***
     * 单链表节点
     */
    public static class ListNode {
        Integer val;
        ListNode next;

        public ListNode(Integer val) {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListNode listNode = (ListNode) o;
            return val.equals(listNode.val);
        }

        @Override
        public int hashCode() {
            return Objects.hash(val);
        }
    }
}
