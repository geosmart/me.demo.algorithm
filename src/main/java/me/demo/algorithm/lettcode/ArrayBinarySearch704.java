package me.demo.algorithm.lettcode;

/***
 给定一个 n 个元素有序的（升序）整型数组 nums 和一个目标值 target  ，写一个函数搜索 nums 中的 target，如果目标值存在返回下标，否则返回 -1。


 示例 1:

 输入: nums = [-1,0,3,5,9,12], target = 9
 输出: 4
 解释: 9 出现在 nums 中并且下标为 4
 示例 2:

 输入: nums = [-1,0,3,5,9,12], target = 2
 输出: -1
 解释: 2 不存在 nums 中因此返回 -1
  

 提示：

 你可以假设 nums 中的所有元素是不重复的。
 n 将在 [1, 10000]之间。
 nums 的每个元素都将在 [-9999, 9999]之间。

 来源：力扣（LeetCode）
 链接：https://leetcode-cn.com/problems/binary-search
 */
class ArrayBinarySearch704 {

    public static void main(String[] args) {
//        int[] a = new int[]{1, 1, 2};
//        int res = search_cnt(a, 1);

        int[] a = new int[]{6, 7, 1, 2, 3, 4, 5};
        int res = search_rotation(a, 6);

        System.out.println(res);
    }

    /***
     * 81. 搜索旋转排序数组（存在重复值）
     * 最坏情况下O(n)
     */
    public boolean search_rotation_with_repeat(int[] a, int tar) {
        int mid = 0, left = 0, right = a.length - 1;
        while (left <= right) {
            mid = left + (right - left) / 2;
            if (a[mid] == tar) {
                return true;
            }
            int  sum1=0,sum2=0;
            char[] chars=String.valueOf(sum1+sum2).toCharArray();

            //重复值处理，起点前移
            if (a[left] == a[mid]) {
                left++;
                continue;
            }
            //判断有序
            if (a[mid] >= a[left]) {
                //左搜
                if (tar >= a[left] && tar < a[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if (tar > a[mid] && tar <= a[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return false;
    }

    /***
     * 33. 搜索旋转排序数组
     * 输入: nums = [4,5,6,7,0,1,2], target = 0
     * 输出: 4
     * 题目要求 O(logN) 的时间复杂度，基本可以断定本题是需要使用二分查找，怎么分是关键。
     * 由于题目说数字了无重复，
     * 举个例子：1 2 3 4 5 6 7 可以大致分为两类，
     * 第一类 2 3 4 5 6 7 1 这种，也就是 nums[left] <= nums[mid]。此例子中就是 2 <= 5。
     * 这种情况下，前半部分有序。因此如果 nums[left] <=target<nums[mid]，则在前半部分找，否则去后半部分找。
     * 第二类 6 7 1 2 3 4 5 这种，也就是 nums[left] > nums[mid]。此例子中就是 6 > 2。
     * 这种情况下，后半部分有序。因此如果 nums[mid] <target<=nums[right]，则在后半部分找，否则去前半部分找。
     */
    public static int search_rotation(int[] a, int tar) {
        int mid = 0, left = 0, right = a.length - 1;
        while (left <= right) {
            mid = left + (right - left) / 2;
            if (a[mid] == tar) {
                return mid;
            }
            //此处关键是需要判断查找方向，根据mid和left的值来判断哪边有序，不能根据tar和mid的大小来判断
            if (a[left] <= a[mid]) {//前半部分有序
                //tar在左边[left,mid)，在左边找，否则换右边找
                if (tar >= a[left] && tar < a[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else if (a[left] > a[mid]) {//前半部分无序，则后半部分有序
                //tar在右边(mid,right]，在右边找，否则换左边查
                if (tar > a[mid] && tar <= a[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }

    /***
     * 34. 在排序数组中查找元素的第一个和最后一个位置
     */
    public static int[] searchRange(int[] a, int tar) {
        int mid = 0, left = 0, right = a.length - 1;
        int[] idx = new int[]{-1, -1};
        while (left <= right) {
            mid = left + (right - left) / 2;
            System.out.println(String.format("l-r[%s,%s]=%s,%s,mid[%s]=%s", left, right, a[left], a[right], mid, a[mid]));
            if (a[mid] < tar) {
                left = mid + 1;
            } else if (a[mid] > tar) {
                right = mid - 1;
            } else if (a[mid] == tar) {
                //找到目标后，左右延伸寻找边界
                System.out.println("find");
                idx[0] = mid;
                idx[1] = mid;
                for (int i = mid - 1; i >= 0; i--) {
                    if (a[i] == tar) {
                        idx[0] = i;
                    } else {
                        break;
                    }
                }
                for (int i = mid + 1; i < a.length; i++) {
                    if (a[i] == tar) {
                        idx[1] = i;
                    } else {
                        break;
                    }
                }
                break;
            }
        }
        return idx;
    }

    /***
     * 面试题53 - I. 在排序数组中查找数字 I
     */
    public static int search_cnt(int[] a, int tar) {
        int cnt = 0, mid = 0, left = 0, right = a.length - 1;
        while (left <= right) {
            mid = left + (right - left) / 2;
            System.out.println(String.format("l-r[%s,%s]=%s,%s,mid[%s]=%s", left, right, a[left], a[right], mid, a[mid]));
            if (a[mid] < tar) {
                left = mid + 1;
            } else if (a[mid] > tar) {
                right = mid - 1;
            } else if (a[mid] == tar) {
                //找到目标后，左右延伸寻找相同值
                cnt++;
                System.out.println("find");
                for (int i = mid - 1; i >= 0; i--) {
                    if (a[i] == tar) {
                        cnt++;
                    } else {
                        break;
                    }
                }
                for (int i = mid + 1; i < a.length; i++) {
                    if (a[i] == tar) {
                        cnt++;
                    } else {
                        break;
                    }
                }
                break;
            }
        }
        return cnt;
    }

    /***
     * 704. 二分查找
     */
    public static int search(int[] a, int target) {
        int mid, left = 0, right = a.length - 1;
        while (left <= right) {
            mid = left + (right - left) / 2;
            if (a[mid] == target) {
                return mid;
            } else if (a[mid] < target) {
                left = mid + 1;
            } else if (a[mid] > target) {
                right = mid - 1;
            }
        }
        return -1;
    }
}