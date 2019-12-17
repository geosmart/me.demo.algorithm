package me.demo.algorithm.lettcode.linkedlist;

import java.util.Objects;

/***
 * 单链表节点
 */
public class ListNode {
    public Integer val;
    public ListNode next;

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