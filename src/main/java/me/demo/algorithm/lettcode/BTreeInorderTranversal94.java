package me.demo.algorithm.lettcode;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
        TreeNode l1_left = new TreeNode(2);
        TreeNode l2_left_left = new TreeNode(4);
        TreeNode l2_left_right = new TreeNode(5);


        TreeNode l1_right = new TreeNode(3);
        TreeNode l2_right_left = new TreeNode(6);
        TreeNode l2_right_right = new TreeNode(7);
        TreeNode l3_right_left_left = new TreeNode(8);
        TreeNode l3_right_left_right = new TreeNode(9);

        root.left = l1_left;
        root.right = l1_right;
        l1_left.left = l2_left_left;
        l1_left.right = l2_left_right;
        l1_right.left = l2_right_left;
        l1_right.right = l2_right_right;
        l2_right_left.left = l3_right_left_left;
        l2_right_left.right = l3_right_left_right;

        List<Integer> nodes = new ArrayList<>();
        traversalTreeInOrder_recurse(nodes, root);
        System.out.println(JSON.toJSONString(nodes));
        nodes = traversalTreeInOrder_stack(root);
        System.out.println(JSON.toJSONString(nodes));
    }


    /***
     * 子树遍历
     * @param root 根节点
     */
    public static List<Integer> traversalTreeInOrder_stack(TreeNode root) {
        List<Integer> nodes = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        //遍历所有路径，直到某条路径没有叶子节点or栈已空时，结束遍历
        while (curr != null || !stack.isEmpty()) {
            //树遍历左子树并入栈，到叶子节点停止
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            //叶子节点出栈
            curr = stack.pop();
            nodes.add(curr.val);
            //当前节点，从右子树遍历
            curr = curr.right;
        }
        return nodes;
    }

    /***
     * 子树遍历
     * @param nodes 遍历结果数组
     * @param root 根节点
     */
    public static void traversalTreeInOrder_recurse(List<Integer> nodes, TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.println(String.format("sub_root[%s],nodes:%s", root.val, JSON.toJSONString(nodes)));
        if (root.left != null) {
            //左子树遍历
            traversalTreeInOrder_recurse(nodes, root.left);
        }
        //根节点
        nodes.add(root.val);
        if (root.right != null) {
            //右子树遍历
            traversalTreeInOrder_recurse(nodes, root.right);
        }
    }

    /***
     * 树节点
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }
}
