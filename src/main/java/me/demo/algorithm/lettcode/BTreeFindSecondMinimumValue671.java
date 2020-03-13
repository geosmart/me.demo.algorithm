package me.demo.algorithm.lettcode;

/**
 * 给定一个二叉树，返回它的中序 遍历。
 * 示例:
 * 输入: [1,null,2,3]
 * * 1
 * ** \
 * ** 2
 * * /
 * 3
 * 输出: [1,3,2]
 * 进阶: 递归算法很简单，你可以通过迭代算法完成吗？
 * 链接：https://leetcode-cn.com/problems/binary-tree-inorder-traversal
 * 中序遍历：左子树---> 根结点 ---> 右子树
 */
class BTreeFindSecondMinimumValue671 {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(2);
        TreeNode l1_left = new TreeNode(3);

        TreeNode l1_left_left = new TreeNode(3);

        TreeNode l1_right = new TreeNode(2);
        TreeNode l2_right_left = new TreeNode(3);
        TreeNode l2_right_right = new TreeNode(4);

        root.left = l1_left;
        l1_left.left = l1_left_left;

        root.right = l1_right;
        l1_right.left = l2_right_left;
        l1_right.right = l2_right_right;

        int res = findSecondMinimumValue(root);
        System.out.println(res);
    }

    public static int findSecondMinimumValue(TreeNode root) {
        if (root == null || (root.left == null && root.right == null)) {
            return -1;
        }
        int left = root.left.val;
        int right = root.right.val;

        if (left == root.val) {
            left = findSecondMinimumValue(root.left);
        }

        if (right == root.val) {
            right = findSecondMinimumValue(root.right);
        }

        if (left != -1 && right != -1) {
            return Math.min(left, right);
        }
        if (left != -1) {
            return left;
        }
        return right;
    }
}
