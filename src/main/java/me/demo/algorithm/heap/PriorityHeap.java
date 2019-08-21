package me.demo.algorithm.heap;

import com.alibaba.fastjson.JSON;

/**
 * 二叉堆
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
        System.out.println(String.format("origin heap[%s]", JSON.toJSONString(heap.getHeapArray())));

        int parentIdx = array.length / 2 - 1;
        heap.heapify(parentIdx);
        System.out.println(String.format("heapify-%s", JSON.toJSONString(heap.getHeapArray())));

        int node = 0;
        heap.insert(node);
        System.out.println(String.format("insert node[%s],heap-%s", node, JSON.toJSONString(heap.getHeapArray())));

        int heapSize = heap.getHeapArray().length;
        for (int i = 0; i < heapSize; i++) {
            int root = heap.pop();
            System.out.println(String.format("pop node[%s],heap-%s", root, JSON.toJSONString(heap.getHeapArray().length)));
        }
    }

    /***
     * 构建堆
     * @param parentIdx
     */
    private void heapify(int parentIdx) {
        //从最后一个父节点，逐级向上，遍历到根节点
        while (parentIdx >= 0) {
            //求父节点与左右孩子的最小值
            int minIdx = getMinNode(parentIdx);
            //交换位置：最小值不在父节点时
            if (minIdx != parentIdx) {
                swap(heapArray, parentIdx, minIdx);
                System.out.println(String.format("swap[%s-%s],%s,parent[%s]", parentIdx, minIdx, JSON.toJSONString(heapArray), parentIdx));
                //左孙子为叶子结点：以当前最小结点作为父节点，调整树使其满足parent比children小
                if (getLeftIdx(heapArray, minIdx) < heapArray.length - 1) {
                    heapify(minIdx);
                }
            }
            parentIdx = parentIdx - 1;
        }
    }

    /***
     * 插入节点
     * @param node
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
     * @param array
     */
    public void heapSort(int[] array) {
        //todo 以重复pop实现堆排序
    }

    public int size() {
        return heapArray.length;
    }

    /***
     * 获取二叉堆中某个分支（p,l,r）的最小节点index
     * @param parentIdx
     * @return
     */
    private int getMinNode(int parentIdx) {
        int leftIdx = getLeftIdx(heapArray, parentIdx);
        int rightIdx = getRightIdx(heapArray, parentIdx);
        int minIdx;
        if (heapArray[leftIdx] < heapArray[rightIdx]) {
            minIdx = leftIdx;
        } else {
            minIdx = rightIdx;
        }
        if (heapArray[parentIdx] < heapArray[minIdx]) {
            minIdx = parentIdx;
        }
        return minIdx;
    }

    /***
     * 交换a中idx1和idx2的值
     */
    private void swap(int[] a, int idx1, int idx2) {
        int min = a[idx2];
        a[idx2] = a[idx1];
        a[idx1] = min;
    }

    /***
     * 获取左孩子index
     * @param a
     * @param parentIdx
     * @return
     */
    private int getLeftIdx(int[] a, int parentIdx) {
        int leftIdx = (2 * parentIdx + 1) > a.length - 1 ? a.length - 1 : (2 * parentIdx + 1);
        return leftIdx;
    }

    /***
     * 获取右孩子index
     * @param a
     * @param parentIdx
     * @return
     */
    private int getRightIdx(int[] a, int parentIdx) {
        int rightIdx = (2 * parentIdx + 2) > a.length - 1 ? a.length - 1 : (2 * parentIdx + 2);
        return rightIdx;
    }

    public int[] getHeapArray() {
        return heapArray;
    }
}
