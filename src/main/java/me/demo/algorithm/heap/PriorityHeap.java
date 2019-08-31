package me.demo.algorithm.heap;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;

/**
 * 二叉堆
 */
public class PriorityHeap {
    /***
     * 堆数组
     */
    private Object[] heap;

    /***
     * 堆大小
     */
    private int size;

    public PriorityHeap(Object[] array) {
        heap = array;
        size = heap.length;
    }

    /***
     * 将数组调整为满足parent>(left,right)的堆结构
     */
    public void heapify() {
        System.out.println("orgin heap.");
        printHeap();
        //从最后一个父节点开始下沉
        int i = (size >> 1) - 1;
        while (i >= 0) {
            System.out.println(String.format("heapify,parent[%s] siftdown", i));
            siftDown(i, (int) heap[i]);
            i--;
            printHeap();
        }
    }


    /***
     * 插入节点
     * @param node 节点值
     */
    public void add(int node) {
        heap = Arrays.copyOf(heap, size + 1);
        size = size + 1;
        heap[size - 1] = node;
        //从最后一个节点开始,逐父节点单线遍历上浮
        siftUp(size - 1, (int) heap[size - 1]);
    }

    /***
     * 弹出节点
     * @return 堆顶节点值
     */
    public Object pop() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("heap is empty");
        }
        //弹出节点
        Object node = heap[0];
        //堆尾节点移动到堆顶节点
        heap[0] = heap[heap.length - 1];
        //数组
        heap = Arrays.copyOf(heap, size - 1);
        size -= 1;
        if (size > 1) {
            //顶点下沉
            System.out.println(String.format("parent[%s] siftdown", 0));
            siftDown(0, (int) heap[0]);
        }
        return node;
    }

    /***
     * 获取堆顶节点
     * @return 堆顶节点值
     */
    public Object peak() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("heap is empty");
        }
        return heap[0];
    }

    /***
     * 节点下沉
     * @param parent 父节点index
     * @param value 父节点值
     */
    private void siftDown(int parent, int value) {
        //当parent还有子节点时进行循环swap
        while (parent < (size / 2)) {
            //假设left最小
            int left = (parent << 1) + 1;
            int right = left + 1;
            if (right < size && (int) heap[right] < (int) heap[left]) {
                //right最小
                left = right;
            }
            //parent最小
            if (value < (int) heap[left]) {
                break;
            }
            //交换parent与最小值
            heap[parent] = heap[left];
            parent = left;
        }
        //将原始parent放到最终交换后的位置
        heap[parent] = value;
    }

    /***
     * 节点上浮
     * @param child 节点index
     * @param value 节点值
     */
    private void siftUp(int child, int value) {
        //从最后一个节点开始,逐父节点单线遍历上浮
        while (child > 0) {
            System.out.println(String.format("node[%s] siftup", child));
            int parent = (child - 1) / 2;
            //父节点大，则交换上浮
            if ((int) heap[child] < (int) heap[parent]) {
                heap[child] = heap[parent];
                heap[parent] = value;
                child = parent;
                printHeap();
            } else {
                break;
            }
        }
    }

    /***
     * 堆排序
     */
    public int[] heapSort() {
        //构建堆
        heapify();

        int[] sortArray = new int[heap.length];
        //逐个弹出堆顶最值即可完成排序
        for (int i = 0; i < sortArray.length; i++) {
            System.out.println(String.format("pop[%s]", i));
            sortArray[i] = (int) pop();
        }
        return sortArray;
    }

    public void printHeap() {
        System.out.println(JSON.toJSONString(getHeap()));
    }

    public Object[] getHeap() {
        return heap;
    }

}
