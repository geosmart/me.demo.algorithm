package me.demo.algorithm.lettcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 给定一个二叉树，返回其按层次遍历的节点值。 （即逐层地，从左到右访问所有节点）。
 * 例如:
 * 给定二叉树: [3,9,20,null,null,15,7],
 * 3
 * / \
 * 9  20
 * /  \
 * 15   7
 * 返回其层次遍历结果：
 * [
 * [3],
 * [9,20],
 * [15,7]
 * ]
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/binary-tree-level-order-traversal
 */
class BTreeLevelOrder102 {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    static List<List<Integer>> levels = new ArrayList<List<Integer>>();

    public static void main(String[] args) {
        TreeNode root = new TreeNode(2);
        TreeNode l1_left = new TreeNode(3);
        TreeNode l1_left_left = new TreeNode(3);
        TreeNode l1_left_right = new TreeNode(6);
        root.left = l1_left;
        l1_left.left = l1_left_left;
        l1_left.right = l1_left_right;

        TreeNode l1_right = new TreeNode(2);
        TreeNode l1_rightt_left = new TreeNode(3);
        TreeNode l1_right_right = new TreeNode(4);
        root.right = l1_right;
        l1_right.left = l1_rightt_left;
        l1_right.right = l1_right_right;


        List<List<Integer>> res = levelOrder_queue(root);
        System.out.println(res);
    }

    /***
     * 利用外部队列遍历
     */
    public static List<List<Integer>> levelOrder_queue(TreeNode root) {
        if (root == null) {
            return levels;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        int level = 0;
        while (!queue.isEmpty()) {
            // start the current level
            levels.add(new ArrayList<>());

            //当前层的元素大小=队列大小
            int level_length = queue.size();
            // 循环只处理当前层的元素
            for (int i = 0; i < level_length; ++i) {
                TreeNode node = queue.remove();

                //元素加入当前层所在数组
                levels.get(level).add(node.val);

                //当前层级的子节点加入队列，下一层会处理
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            // go to next level
            level++;
        }
        return levels;
    }

    /***
     * 递归逐层遍历
     */
    public static List<List<Integer>> levelOrder_recurse(TreeNode root) {
        if (root == null) {
            return levels;
        }
        helper(root, 0);
        return levels;
    }

    private static void helper(TreeNode node, int level) {
        if (levels.size() == level) {
            //当前层初始化
            levels.add(new ArrayList<Integer>());
        }
        levels.get(level).add(node.val);

        //递归左子树
        if (node.left != null) {
            helper(node.left, level + 1);
        }
        //递归右子树
        if (node.right != null) {
            helper(node.right, level + 1);
        }
    }
}
