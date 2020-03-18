package me.demo.algorithm.algorithm.test;

import org.junit.jupiter.api.Test;

/**
 * @author geosmart
 */
public class ArrayCase {

  /**
   * 一个有序数列，序列中的每一个值都能够被2或者3或者5所整除，这个序列的初始值从1开始，但是1并不在这个数列中。求第1500个值是多少？
   * 
   * @return
   */ 
  @Test
  public void out_of_china() {
	Integer i01 = 59;
	int i02 = 59;
	Integer i03 = Integer.valueOf(59);
	Integer i04 = new Integer(59);
	System.out.println(i01 == i02);
	System.out.println(i01 == i03);
	System.out.println(i03 == i04);
	System.out.println(i02 == i04);
  }
}
