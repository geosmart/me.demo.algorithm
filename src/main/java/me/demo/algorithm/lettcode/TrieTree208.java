package me.demo.algorithm.lettcode;

import java.util.Arrays;
import java.util.List;

/***
 实现一个 Trie (前缀树)，包含 insert, search, 和 startsWith 这三个操作。

 示例:

 Trie trie = new Trie();

 trie.insert("apple");
 trie.search("apple");   // 返回 true
 trie.search("app");     // 返回 false
 trie.startsWith("app"); // 返回 true
 trie.insert("app");
 trie.search("app");     // 返回 true
 说明:

 你可以假设所有的输入都是由小写字母 a-z 构成的。
 保证所有输入均为非空字符串。

 来源：力扣（LeetCode）
 链接：https://leetcode-cn.com/problems/implement-trie-prefix-tree
 */
class TrieTree208 {

    public static void main(String[] args) {
        Trie obj = new Trie();
        obj.insert("hello");
        obj.insert("中国");
        System.out.println(obj.search("中国"));
        System.out.println(obj.startsWith("中"));
        System.out.println(obj.search("hell"));
        System.out.println(obj.startsWith("helloa"));
        System.out.println(obj.search("hello"));
        System.out.println(obj.startsWith("hell"));
        System.out.println(obj.startsWith("helloa"));
        System.out.println(obj.startsWith("hello"));
    }

    /***
     * 关键是查找字符串在trie树中所处的叶子节点的位置
     *
     * insert：searchLeaf找到叶子节点，在后续添加新的节点，注意插入的最后一个节点要打个标识，用于处理有更长的路径insert后search不到；
     * search：searchLeaf找到叶子节点，叶子节点是最后一个节点且路径长度和输入字符串的长度一致；
     * startWith：searchLeaf找到叶子节点，叶子节点的长度和输入字符串的长度一致表示搜到了即可；
     *
     */
    static class Trie {
        private static class Node {
            public char val;
            public Node[] next;
            boolean wordEnd = false;

            public Node(char val) {
                this.val = val;
            }
        }

        /***
         * trie树的根节点
         */
        private Node root;

        /**
         * Initialize your data structure here.
         */
        public Trie() {
            root = new Node((char) 0);
        }

        /**
         * Inserts a word into the trie.
         */
        public void insert(String word) {
            if (word == null || word.trim().length() == 0) {
                return;
            }
            char[] chars = word.toCharArray();
            //查找已存在的路径
            List res = searchLeaf(chars);
            int leafIndex = (int) res.get(0);
            Node leafNode = (Node) res.get(1);

            //已存在路径直接返回
            if (leafIndex == chars.length) {
                leafNode.wordEnd = true;
                return;
            }
            //插入后续节点
            for (int i = leafIndex; i < chars.length; i++) {
                Node node = new Node(chars[i]);
                if (leafNode.next == null) {
                    leafNode.next = new Node[]{node};
                } else {
                    //层级已有节点时，需要扩容合并（仅适合读多写少）
                    Node[] nexts = new Node[leafNode.next.length + 1];
                    System.arraycopy(leafNode.next, 0, nexts, 0, leafNode.next.length);
                    nexts[leafNode.next.length] = node;
                    leafNode.next = nexts;
                }
                leafNode = node;
            }
            leafNode.wordEnd = true;
        }

        /**
         * Returns if the word is in the trie.
         */
        public boolean search(String word) {
            if (word == null || word.trim().length() == 0 || root.next == null) {
                return false;
            }
            char[] chars = word.toCharArray();
            List res = searchLeaf(chars);
            int leafIndex = (int) res.get(0);
            Node leafNode = (Node) res.get(1);
            return leafNode.wordEnd && leafIndex == chars.length;
        }

        /**
         * Returns if there is any word in the trie that starts with the given prefix.
         */
        public boolean startsWith(String prefix) {
            if (prefix == null || prefix.trim().length() == 0 || root.next == null) {
                return false;
            }
            char[] chars = prefix.toCharArray();
            List res = searchLeaf(chars);
            return (int) res.get(0) == chars.length;
        }

        /***
         * 搜索字符串在trie树中匹配的叶子节点
         * @param chars 字符串
         * @return [leafIndex, leafNode]
         */
        private List searchLeaf(char[] chars) {
            //已存在路径的叶子节点
            Node leaf = root;
            //叶子节点的索引
            int p = 0;
            Node[] nextNodes = root.next;
            //循环深入查找下一层的节点中是否存在与word路径一致的
            while (nextNodes != null) {
                if (p == chars.length) {
                    break;
                }
                boolean isFind = false;
                for (Node node : nextNodes) {
                    if (chars[p] == node.val) {
                        isFind = true;
                        leaf = node;
                        p++;
                        break;
                    }
                }
                //这一层找到了，继续下一层查找
                if (isFind) {
                    nextNodes = leaf.next;
                } else {
                    nextNodes = null;
                }
            }
            return Arrays.asList(p, leaf);
        }
    }
}