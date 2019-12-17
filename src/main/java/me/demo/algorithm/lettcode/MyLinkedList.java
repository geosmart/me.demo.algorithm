package me.demo.algorithm.lettcode;

import me.demo.algorithm.lettcode.linkedlist.ListNode;
import me.demo.algorithm.lettcode.linkedlist.SingleLinkkedList;

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
        sList.getHead().printNode();
    }

}
