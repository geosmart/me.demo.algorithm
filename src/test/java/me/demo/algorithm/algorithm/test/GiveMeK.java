package me.demo.algorithm.algorithm.test;// 设计一个“GiveMeK”类，用来返回在一个无穷的整数流(int stream)中第k大的数。
// 1. 请在GiveMeK类中加入需要的成员。
// 2. 实现类的构造方法 : 输入为k的值和数据流中一开始的数据，构造一个GiveMeK实例。
// 3. 实现showMe方法 : 输入一个新的数据流元素，返回当前流的第k大的数。
// 假设k >= 1，并且一开始的数据流中已经有至少k-1个元素。
// 注意: 数据流为无穷大，showMe方法可被调用任意多次。请注意空间使用情况。

// 例子：
// int k = 2;							// 第2大的元素
// int[] s = {7, 3, 5, 8};				// 一开始数据流中存在的4个元素，都为整数
// GiveMeK gmk = new GiveMeK(k, s);	    // 构造一个新实例
// gmk.showMe(2);		        // 往数据流中加入新元素2，当前数据流为{7, 3, 5, 8, 2}， 返回第2大元素7
// gmk.showMe(9);		        // 往数据流中加入新元素9，当前数据流为{7, 3, 5, 8, 2, 9}， 返回第2大元素8
// gmk.showMe(9);		        // 往数据流中加入新元素9，当前数据流为{7, 3, 5, 8, 2, 9, 9}， 返回第2大元素9
// gmk.showMe(1);		        // 往数据流中加入新元素1，当前数据流为{7, 3, 5, 8, 2, 9, 9, 1}， 返回第2大元素9

import java.util.PriorityQueue;

// 请在以下代码中完成
// GiveMeK defines a class with a single method “showMe” that shows the kth largest element in a unbounded int stream
public class GiveMeK {
    // 1.add necessary fields
    //默认小顶堆
    int[] stream;
    int k;
    PriorityQueue<Integer> heap;

    public static void main(String[] args) {
        int k = 2;                            // 第2大的元素
        int[] s = {7, 3, 5, 8};                // 一开始数据流中存在的4个元素，都为整数
        GiveMeK gmk = new GiveMeK(k, s);
        System.out.println(gmk.showMe(2));//7
        System.out.println(gmk.showMe(9));//8
        System.out.println(gmk.showMe(9));//9
        System.out.println(gmk.showMe(1));//9
    }

    // 2.construct an instance of GiveMeK class
    public GiveMeK(int k, int[] stream) {
        this.k = k;
        this.stream = stream;
        this.heap = constructTopKHeap(stream, k);
    }

    // 3.showMe adds a new value to the stream and returns the Kth largest element seen so far
    public int showMe(int value) {
        //add value
        int[] nums = new int[stream.length + 1];
        System.arraycopy(stream, 0, nums, 0, stream.length);
        nums[stream.length] = value;
        stream = nums;
        //show
        //以最小堆存topK个数，第k大的就在堆顶
        if (heap.peek() < value) {
            //如果元素比堆顶大，移除堆顶，将较大的元素入堆，heap会自动sift最小值到peek
            heap.poll();
            heap.offer(value);
        }
        return heap.peek();
    }

    /***
     * 找出前k个最大数，采用二叉堆实现
     */
    public PriorityQueue<Integer> constructTopKHeap(int[] nums, int k) {
        //以最小堆存topK个数，第k大的就在堆顶
        PriorityQueue<Integer> heap = new PriorityQueue<>(k);
        for (int num : nums) {
            //小于k个之前，一直offer
            if (heap.size() < k) {
                heap.offer(num);
            }
            //当等于k个后，每次将堆顶与后续offer的值比较
            else if (heap.peek() < num) {
                //如果元素比堆顶大，移除堆顶，将较大的元素入堆，heap会自动sift最小值到peek
                heap.poll();
                heap.offer(num);
            }
        }
        return heap;
    }
}
