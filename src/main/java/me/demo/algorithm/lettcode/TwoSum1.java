package me.demo.algorithm.lettcode;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/***
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
 *
 * 示例:
 * 给定 nums = [2, 7, 11, 15], target = 9
 *
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 */
class TwoSum1 {

    public static void main(String[] args) {
        TwoSum1 s = new TwoSum1();
        int[] nums = new int[]{2, 7, 11, 15};
        System.out.println(JSON.toJSONString(s.twoSum1(nums, 9)));
        System.out.println(JSON.toJSONString(s.twoSum2(nums, 9)));
        System.out.println(JSON.toJSONString(s.twoSum3(nums, 9)));
    }

    /***
     * 暴力法
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(1)
     */
    public int[] twoSum1(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalArgumentException(String.format("can not calc twoSum of %s in %s", target, JSON.toJSONString(nums)));
    }

    /***
     * 两遍Hash表
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> numsMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            numsMap.put(nums[i], i);
        }

        for (int j = 0; j < nums.length; j++) {
            int left = target - nums[j];
            //注意此处numsMap.get(left) != j避免target为偶数时，取到相同的
            if (numsMap.containsKey(left) && numsMap.get(left) != j) {
                return new int[]{numsMap.get(left), j};
            }
        }
        throw new IllegalArgumentException(String.format("can not calc twoSum of %s in %s", target, JSON.toJSONString(nums)));
    }

    /***
     * 一遍Hash表
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int[] twoSum3(int[] nums, int target) {
        Map<Integer, Integer> numsMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int left = target - nums[i];
            if (numsMap.containsKey(left)) {
                return new int[]{numsMap.get(left), i};
            }
            numsMap.put(nums[i], i);
        }
        throw new IllegalArgumentException(String.format("can not calc twoSum of %s in %s", target, JSON.toJSONString(nums)));
    }
}