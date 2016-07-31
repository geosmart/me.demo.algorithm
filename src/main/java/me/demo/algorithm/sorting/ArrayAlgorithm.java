package me.demo.algorithm.sorting;


/**
 * 数组常用算法
 * 
 * 
 * @author geosmart
 * 
 */
public class ArrayAlgorithm {

  /**
   * Launch the application.
   * 
   * @param args
   */
  public static void main(String args[]) {
	int[] arr = new int[] {5, 3, 1, 4, 2};
	SortingUtil.print(arr);
	reverse(arr);
	  SortingUtil.print(arr);
  }


  /**
   * 折半首尾替换
   * 
   * 
   * @param arr
   */
  public static void reverse(int[] arr) {
	int len = arr.length;
	for (int i = 0; i < len / 2; i++) {
	  int temp = arr[i];
	  arr[i] = arr[len - 1 - i];
	  arr[len - 1 - i] = temp;
	}
  }

}
