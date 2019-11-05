package me.demo.algorithm.LinkedList;

import java.util.Iterator;
import java.util.LinkedList;

/***
 * 链表查找
 */
public class LinkedListSearch {
    private LinkedList<Integer> list;

    public LinkedListSearch(LinkedList<Integer> list) {
        this.list = list;
    }

    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < 11; i++) {
            list.add(i);
        }
        int middle = new LinkedListSearch(list).findMiddle();
        System.out.println(middle);
        int divide = new LinkedListSearch(list).findDivideNode(4);
        System.out.println(divide);
    }

    /***
     * 查找中间点
     * @return
     */
    public int findMiddle() {
        if (list.isEmpty()) {
            return -1;
        }
        Iterator<Integer> slowIt = list.iterator();
        Iterator<Integer> fastIt = list.iterator();
        int middle = slowIt.next();
        while (fastIt.hasNext()) {
            fastIt.next();
            if (fastIt.hasNext()) {
                fastIt.next();
            }
            middle = slowIt.next();
        }
        return middle;
    }

    /***
     * 查找等分点
     * @param divide
     * @return
     */
    public int findDivideNode(int divide) {
        if (list.isEmpty()) {
            return -1;
        }
        Iterator<Integer> slowIt = list.iterator();
        Iterator<Integer> fastIt = list.iterator();
        int middle = slowIt.next();
        while (fastIt.hasNext()) {
            for (int i = 0; i < divide; i++) {
                if (fastIt.hasNext()) {
                    fastIt.next();
                } else {
                    break;
                }
            }
            middle = slowIt.next();
        }
        return middle;
    }
}
