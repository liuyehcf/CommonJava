package com.sunland.hihocoder.competition.microsoft20170408;

import java.util.*;

/**
 * Created by liuye on 2017/4/9 0009.
 */
public class Item4 {
    private static class TreeNode {
        int F;//父亲节点
        int IN;//刺杀该节点需要的信息量
        int IP;//刺杀该节点能获取的信息量
        int C;//刺杀该节点的消费(最终是要最小化这个,而信息量只是一个限制条件)

        public TreeNode(int F, int IN, int IP, int C) {
            this.F = F;
            this.IN = IN;
            this.IP = IP;
            this.C = C;
            children = new ArrayList<TreeNode>();
            parent = null;
        }

        List<TreeNode> children;
        TreeNode parent;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        List<TreeNode> list = new ArrayList<TreeNode>();
        for (int i = 0; i < N; i++) {
            list.add(new TreeNode(
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt(),
                    scanner.nextInt()
            ));
        }

        //建立一棵树
        TreeNode root = null;
        for (TreeNode node : list) {
            if (node.F == 0) {
                root = node;
            } else {
                TreeNode father = list.get(node.F - 1);
                father.children.add(node);
                node.parent = father;
            }
        }

        Map<TreeNode, Integer> map = new HashMap<TreeNode, Integer>();

        int res = INCost(root, map);
        if (res == Integer.MAX_VALUE) {
            System.out.println(-1);
        } else {
            System.out.println(res + root.C);
        }
    }

    private static int INCost(TreeNode root, Map<TreeNode, Integer> map) {
        if (map.containsKey(root)) return map.get(root);
        long[] dp = new long[root.IN + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;//获取0点信息需要的花费


        for (int i = 1; i <= root.IN; i++) {
            for (TreeNode child : root.children) {
                //todo 这种循环方式会导致dp[i]的值多次依赖一个直系下属
                dp[i] = Math.min(dp[i], INCost(child, map) + child.C + dp[Math.max(0, i - child.IP)]);
            }
        }

        map.put(root, (int) dp[root.IN]);
        return map.get(root);
    }

    /**
     * 为什么要这样
     *
     * @param root
     * @param map
     * @return
     */
    private static int INCostX86(TreeNode root, Map<TreeNode, Integer> map) {
        if (map.containsKey(root)) return map.get(root);
        long[] dp = new long[root.IN + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;//获取0点信息需要的花费

        for (TreeNode child : root.children) {
            for (int j = root.IN; j > 0; j--) {
                int kk = INCostX86(child, map);
                dp[j] = Math.min(dp[Math.max(0, j - child.IP)] + kk + child.C, dp[j]);
            }
        }
        map.put(root, (int) dp[root.IN]);
        return map.get(root);
    }

}
