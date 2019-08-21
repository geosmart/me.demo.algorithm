package me.demo.algorithm.search;
public class BinarySearch2 {

    public static void main(String[] args) {
        int[] srcList = new int[]{10, 11, 13, 14, 14, 15, 16, 17, 18, 112, 112, 123};
        for (int i = 0; i < srcList.length; i++) {
            System.out.println(String.format("search %s", srcList[i]));
            int res = search(srcList, 0, srcList.length - 1, srcList[i]);
            System.out.println(res);
        }
        int res = search(srcList, 0, srcList.length - 1, 24);
        System.out.println(res);
        int res2 = search(srcList, 0, srcList.length - 1, -2);
        System.out.println(res2);
    }

    //快速排序
    void sort(int[] srcList) {

    }

    public static int binarySearch(int[] srcList, int target) {
        return search(srcList, 0, srcList.length - 1, target);
    }

    private static int search(int[] srcList, int fromIndex, int toIndex, int target) {
        //未找到
        if (target > srcList[toIndex] || target < srcList[fromIndex]) {
            System.out.println(String.format("from[%s],to[%s],not found[%s]", srcList[fromIndex], srcList[toIndex], target));
            return -1;
        }
        //与首|尾一致
        if (target == srcList[fromIndex]) {
            System.out.println(String.format("from[%s],to[%s]", srcList[fromIndex], srcList[toIndex]));
            return fromIndex;
        } else if (target == srcList[toIndex]) {
            System.out.println(String.format("from[%s],to[%s]", srcList[fromIndex], srcList[toIndex]));
            return toIndex;
        }
        //未找到
        if (toIndex - fromIndex == 1) {
            System.out.println(String.format("from[%s],to[%s],not found[%s]", srcList[fromIndex], srcList[toIndex], target));
            return -1;
        }
        //中间值
        int half = fromIndex + (toIndex - fromIndex) / 2;
        System.out.println(String.format("from[%s],to[%s],half[%s]", srcList[fromIndex], srcList[toIndex], srcList[half]));
        //中间值一致
        if (target == srcList[half]) {
            return half;
        } else if (target < srcList[half]) {
            //递归左子树
            return search(srcList, fromIndex, half, target);
        } else if (target > srcList[half]) {
            //递归右子树
            return search(srcList, half, toIndex, target);
        }
        return -1;
    }
}
