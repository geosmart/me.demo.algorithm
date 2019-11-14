package me.demo.algorithm.lettcode;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

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
class BTreeInorderTranversal94 {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        TreeNode right = new TreeNode(2);
        TreeNode left = new TreeNode(3);
        root.right = right;
        right.left = left;

        BTreeInorderTranversal94 solution = new BTreeInorderTranversal94();
        List<Integer> res = solution.inorderTraversal(root);
        System.out.println(JSON.toJSONString(res));
    }

    /**
     * Definition for a binary tree node.
     */
    public static class TreeNode {
        int val;
        public TreeNode left;
        public TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public List<Integer> inorderTraversal(TreeNode root) {
        return null;
    }
}