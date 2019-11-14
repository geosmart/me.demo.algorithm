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

        List<Integer> res = inorderTraversal(root);
        System.out.println(JSON.toJSONString(res));
    }

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> nodes = new ArrayList<>();
        traversalTreeInOrder(nodes, root);
        return nodes;
    }

    /***
     * 子树遍历
     * @param nodes 遍历结果数组
     * @param root 根节点
     */
    public void traversalTreeInOrder(List<Integer> nodes, TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.println(String.format("sub_root[%s],nodes:%s", root.val, JSON.toJSONString(nodes)));
        if (root.left != null) {
            //左子树遍历
            traversalTreeInOrder(nodes, root.left);
        }
        //根节点
        nodes.add(root.val);
        if (root.right != null) {
            //右子树遍历
            traversalTreeInOrder(nodes, root.right);
        }
    }

    /***
     * 树节点
     */
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }
}
