package me.demo.algorithm.heap;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;

/**
 * 二叉堆
 * 示例日志：https://gist.github.com/geosmart/c31fe452f0b536ea12fbda72c1385553
 * //# 参考：
 * // [漫画：什么是堆排序？](https://www.itcodemonkey.com/article/8929.html)
 * // [漫画：什么是二叉堆？](https://www.itcodemonkey.com/article/8660.html)
 * // [漫画：什么是优先队列？](https://www.itcodemonkey.com/article/9681.html)
 */
public class PriorityHeap {
    /***
     * 堆数组
     */
    private int[] heapArray;

    public PriorityHeap(int[] array) {
        heapArray = array;
    }

    public static void main(String[] args) {
        int[] array = new int[]{9, 8, 6, 1, 4, 5, 3, 2, 7};
        PriorityHeap heap = new PriorityHeap(array);

        System.out.println("orgin heap.");
        heap.dump();

        heap.heapify();

        heap.heapSort();

        int node = 111;
        heap.insert(node);

        heap.heapSort();

        int heapSize = heap.getHeapArray().length;
        for (int i = 0; i < heapSize; i++) {
            int root = heap.pop();
            System.out.println(String.format("pop node[%s],heap-%s", root, JSON.toJSONString(heap.getHeapArray())));
        }
    }

    public void heapify() {
        int parentIdx = getLastParentNode();
        heapify(parentIdx);
        System.out.println("heapify result.");
        dump();
    }

    /***
     * 构建堆
     * @param parentIdx 父节点索引
     */
    private void heapify(int parentIdx) {
        //从最后一个父节点，逐级向上，遍历到根节点
        while (parentIdx >= 0) {
            //求父节点与左右孩子的最小值
            int minIdx = getMinNode(parentIdx);
            System.out.println(String.format("calc sub tree of parent(%s),min=%s,need swap=%s", heapArray[parentIdx], heapArray[minIdx], (minIdx != parentIdx)));
            //交换位置：最小值不在父节点时
            if (minIdx != parentIdx) {
                swap(heapArray, parentIdx, minIdx);
                System.out.println(String.format("swap index %s<->%s", heapArray[parentIdx], heapArray[minIdx]));
                dump();
                System.out.println(String.format("sub tree of parent[%s] is leaf node? %s", heapArray[minIdx], getLeftChild(minIdx) == -1));
                //左孙子为叶子结点：以当前最小结点作为父节点，调整树使其满足parent比children小
                if (getLeftChild(minIdx) != -1) {
                    heapify(minIdx);
                    dump();
                }
            }
            parentIdx = parentIdx - 1;
        }
    }

    /***
     * 插入节点
     * @param node 节点值
     */
    public void insert(int node) {
        System.out.println(String.format("insert node[%s]", node));
        //grow
        int[] newArray = new int[heapArray.length + 1];
        System.arraycopy(heapArray, 0, newArray, 0, heapArray.length);
        newArray[newArray.length - 1] = node;

        //siftup from last parentNode
        int parentIdx = newArray.length / 2 - 1;
        heapArray = newArray;
        heapify(parentIdx);
        System.out.println(String.format("insert node[%s],heap-%s", node, JSON.toJSONString(heapArray)));
    }

    /***
     * 弹出节点
     * @return
     */
    public int pop() {
        System.out.println("pop node");
        if (size() == 0) {
            throw new IndexOutOfBoundsException("heap is empty");
        }
        //移除堆顶节点
        int root = heapArray[0];
        if (size() == 1) {
            heapArray = new int[0];
            return root;
        }
        //堆尾节点替换为根节点
        heapArray[0] = heapArray[heapArray.length - 1];

        int[] newArray = new int[heapArray.length - 1];
        System.arraycopy(heapArray, 0, newArray, 0, heapArray.length - 1);
        heapArray = newArray;

        //从根节点执行下沉
        heapify(0);
        return root;
    }

    /***
     * 堆排序
     */
    public void heapSort() {
        //构建堆
        int parentIdx = getLastParentNode();
        heapify(parentIdx);

        int[] sortArray = new int[heapArray.length];
        //逐个弹出堆顶最值即可完成排序
        for (int i = 0; i < sortArray.length; i++) {
            sortArray[i] = pop();
        }
        heapArray = sortArray;
        System.out.println(String.format("heapSort-%s", JSON.toJSONString(heapArray)));
    }

    /***
     * 获取二叉堆中某个分支（p,l,r）的最小节点index
     * @param parentIdx
     * @return
     */
    private int getMinNode(int parentIdx) {
        int leftIdx = getLeftChild(parentIdx);
        int rightIdx = getRightChild(parentIdx);
        int minIdx;
        minIdx = getMinIdx(leftIdx, rightIdx);
        minIdx = getMinIdx(minIdx, parentIdx);
        return minIdx;
    }

    private int getMinIdx(int idx1, int idx2) {
        if (idx1 == -1) {
            return idx2;
        } else if (idx2 == -1) {
            return idx1;
        }

        if (heapArray[idx1] < heapArray[idx2]) {
            return idx1;
        } else {
            return idx2;
        }
    }

    /***
     * 获取二叉堆的最后一个父节点的index
     * @return
     */
    private int getLastParentNode() {
        return (heapArray.length >> 1) - 1;
    }


    /***
     * 获取左孩子index
     * @param parentIdx
     * @return
     */
    private int getLeftChild(int parentIdx) {
        int leftIdx = (parentIdx << 1) + 1;
        leftIdx = leftIdx > heapArray.length - 1 ? -1 : leftIdx;
        return leftIdx;
    }

    /***
     * 获取右孩子index
     * @param parentIdx
     * @return
     */
    private int getRightChild(int parentIdx) {
        int rightIdx = (parentIdx << 1) + 2;
        rightIdx = rightIdx > heapArray.length - 1 ? -1 : rightIdx;
        return rightIdx;
    }

    /***
     * 交换a中idx1和idx2的值
     */
    private void swap(int[] a, int idx1, int idx2) {
        int min = a[idx2];
        a[idx2] = a[idx1];
        a[idx1] = min;
    }

    public int size() {
        return heapArray.length;
    }

    public int[] getHeapArray() {
        return heapArray;
    }

    public void dump() {
        System.out.println(stringOfSize(64, '-'));
        int[] heap = new int[heapArray.length + 1];
        System.arraycopy(heapArray, 0, heap, 1, heapArray.length);
        int height = log2(heap.length) + 1;
        for (int i = 1, len = heap.length; i < len; i++) {
            int x = heap[i];
            int level = log2(i) + 1;
            int spaces = (height - level) * 4;
            //FIXME tab calc error
            if (i == len - 1) {
                spaces = (height - level + 1) * 4;
            }

            System.out.print(stringOfSize(spaces, ' '));
            System.out.print(x);

            if ((int) Math.pow(2, level) - 1 == i) {
                System.out.println();
            }
        }
        System.out.println();
        System.out.println(stringOfSize(64, '-'));
    }

    private String stringOfSize(int size, char ch) {
        char[] a = new char[size];
        Arrays.fill(a, ch);
        return new String(a);
    }

    // log with base 2
    private int log2(int x) {
        // = log(x) with base 10 / log(2) with base 10
        return (int) (Math.log(x) / Math.log(2));
    }

}
