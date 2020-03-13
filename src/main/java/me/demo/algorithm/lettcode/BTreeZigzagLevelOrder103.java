package me.demo.algorithm.lettcode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * 给定一个二叉树，返回其节点值的锯齿形层次遍历。（即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）。
 * 例如：
 * 给定二叉树 [3,9,20,null,null,15,7],
 * 3
 * / \
 * 9  20
 * /  \
 * 15   7
 * 返回锯齿形层次遍历如下：
 * [
 * [3],
 * [20,9],
 * [15,7]
 * ]
 */
class BTreeZigzagLevelOrder103 {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public static void main(String[] args) {
//        TreeNode root = tree1();
        TreeNode root = tree2();


        List<List<Integer>> res = zigzagLevelOrder(root);
        System.out.println(res);
    }

    private static TreeNode tree1() {
        TreeNode root = new TreeNode(1);
        TreeNode l1_left = new TreeNode(2);
        TreeNode l1_left_left = new TreeNode(4);
        root.left = l1_left;
        l1_left.left = l1_left_left;

        TreeNode l1_right = new TreeNode(3);
        TreeNode l1_right_right = new TreeNode(5);
        root.right = l1_right;
        l1_right.right = l1_right_right;
        return root;
    }


    private static TreeNode tree2() {
        TreeNode root = new TreeNode(3);
        TreeNode l1_left = new TreeNode(9);
        root.left = l1_left;

        TreeNode l1_right = new TreeNode(20);
        TreeNode l1_right_left = new TreeNode(15);
        TreeNode l1_right_right = new TreeNode(7);
        root.right = l1_right;
        l1_right.left = l1_right_left;
        l1_right.right = l1_right_right;
        return root;
    }

    /**
     * 层与层之间LIFO，FILO交叉，正好满足一层正序，一层反序；
     */
    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> levels = new ArrayList<>();
        if (root == null) {
            return levels;
        }
        return BFS(root, levels);
    }

    private static List<List<Integer>> BFS(TreeNode root, List<List<Integer>> levels) {
        Deque<TreeNode> deque = new LinkedList<>();
        deque.addLast(root);
        int level = 0;

        //遍历方向需隔层反转
        boolean reverse = true;
        while (deque.size() > 0) {
            levels.add(new ArrayList<>());
            int queueSize = deque.size();
            for (int i = 0; i < queueSize; i++) {
                TreeNode node;
                if (reverse) {
                    //last in first out(先left再right)
                    node = deque.removeFirst();
                    if (node.left != null) {
                        deque.addLast(node.left);
                    }
                    if (node.right != null) {
                        deque.addLast(node.right);
                    }
                } else {
                    //first in last out （先right再left）
                    node = deque.removeLast();
                    if (node.right != null) {
                        deque.addFirst(node.right);
                    }
                    if (node.left != null) {
                        deque.addFirst(node.left);
                    }
                }
                levels.get(level).add(node.val);
                System.out.println(level + ",node-" + node.val);
            }
            reverse = !reverse;
            level++;
        }
        return levels;
    }
}
