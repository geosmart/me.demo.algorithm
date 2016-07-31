package me.demo.algorithm.algorithm.test;

import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;


public class MapTest {


  /**
   * 参考 @http://www.programcreek.com/2013/03/hashmap-vs-treemap-vs-hashtable-vs-linkedhashmap/
   */
  @Test
  public void test() {
	TreeMap<Dog, Integer> treeMap = new TreeMap<Dog, Integer>();
	treeMap.put(new Dog("red", 30), 10);
	treeMap.put(new Dog("black", 20), 15);
	treeMap.put(new Dog("white", 10), 5);
	treeMap.put(new Dog("white", 20), 20);

	// TODO
	for (Entry<Dog, Integer> entry : treeMap.entrySet()) {
	  System.out.println(entry.getKey() + " - " + entry.getValue());
	}
  }

  class Dog implements Comparable<Dog> {
	String color;
	int size;

	Dog(String c, int s) {
	  color = c;
	  size = s;
	}

	public String toString() {
	  return color + this.size + " dog";
	}

	@Override
	public int compareTo(Dog o) {
	  System.out.println(o.size + " -  " + this.size);
	  return o.size - this.size;
	}
  }
}
