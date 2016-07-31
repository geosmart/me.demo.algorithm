package me.demo.algorithm.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 快速排序（Quicksort）又称划分交换排序（partition-exchange sort）
 * 
 * @see 参考：http://visualgo.net/sorting
 * @complex 在平均状况下，排序n个项目要Ο(n logn)次比较。在最坏状况下则需要Ο(n2)次比较，但这种状况并不常见。 事实上，快速排序通常明显比其他Ο(n log
 *          n)算法更快，因为它的内部循环（inner loop）可以在大部分的架构上很有效率地被实现出来。
 * 
 * @author geosmart
 * 
 */
public class QuikSorting {

  static int[] result = new int[5];

  public static void quickSort(List<Integer> arr) {
	if (arr.size() > 1) {

	  int pivotIndex = 0;
	  int pivot = arr.get(pivotIndex);
	  List<Integer> left = new ArrayList<Integer>();

	  for (int i = 0; i < arr.size(); i++) {
		if (arr.get(i) < pivot) {
		  System.out.println("left add " + arr.get(i));
		  left.add(arr.get(i));
		} else {
		  result[arr.size() - 1] = arr.get(i);
		}
	  }
	  System.out.println("QuikSorting.quickSort()");
	  quickSort(left);
	}
  }


  public static void print(int[] list) {
	String str = "";
	for (int i : list) {
	  str += i + "  ";
	}
	System.out.println(str);
  }
}
