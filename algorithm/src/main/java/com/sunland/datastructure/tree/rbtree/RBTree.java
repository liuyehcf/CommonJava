package com.sunland.datastructure.tree.rbtree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import static com.sunland.datastructure.tree.rbtree.Color.*;

/**
 * Created by HCF on 2017/4/6.
 */

class RBTreeNode {
    int val;
    RBTreeNode left;
    RBTreeNode right;
    RBTreeNode parent;
    Color color;

    RBTreeNode(int val) {
        this.val = val;
    }
}

public class RBTree {
    private RBTreeNode nil;

    private RBTreeNode root;

    public RBTree() {
        nil = new RBTreeNode(0);
        nil.color = BLACK;
        nil.left = nil;
        nil.right = nil;
        nil.parent = nil;

        root = nil;
    }

    public void insert(int val) {
        RBTreeNode x = root;
        RBTreeNode y = nil;
        RBTreeNode z = new RBTreeNode(val);
        while (x != nil) {
            y = x;
            if (z.val < x.val) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        z.parent = y;
        z.left = nil;
        z.right = nil;
        z.color = RED;
        if (y == nil) {
            root = z;
        } else if (z.val < y.val) {
            y.left = z;
        } else {
            y.right = z;
        }
        insertFix(z);
        if (!check()) throw new RuntimeException();
    }

    private void insertFix(RBTreeNode x) {
        while (x.parent.color == RED) {
            if (x.parent == x.parent.parent.left) {
                RBTreeNode y = x.parent.parent.right;
                if (y.color == RED) {
                    x.parent.color = BLACK;
                    y.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        x = x.parent;
                        leftRotate(x.parent);
                    }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    rightRotate(x.parent.parent);
                }
            } else {
                RBTreeNode y = x.parent.parent.left;
                if (y.color == RED) {
                    x.parent.color = BLACK;
                    y.color = BLACK;
                    x.parent.parent.color = RED;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.left) {
                        x = x.parent;
                        rightRotate(x.parent);
                    }
                    x.parent.color = BLACK;
                    x.parent.parent.color = RED;
                    leftRotate(x.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    private void leftRotate(RBTreeNode x) {
        RBTreeNode y = x.right;
        x.right = y.left;
        if (y.left != nil) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(RBTreeNode y) {
        RBTreeNode x = y.left;
        y.left = x.right;
        if (x.right != nil) {
            x.right.parent = y;
        }
        x.parent = y.parent;
        if (y.parent == nil) {
            root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }
        x.right = y;
        y.parent = x;
    }

    public void insert(int[] vals) {
        for (int val : vals) {
            insert(val);
        }
    }

    public int max() {
        RBTreeNode x = max(root);
        if (x == nil) throw new RuntimeException();
        return x.val;
    }

    private RBTreeNode max(RBTreeNode x) {
        while (x.right != nil) {
            x = x.right;
        }
        return x;
    }

    public int min() {
        RBTreeNode x = min(root);
        if (x == nil) throw new RuntimeException();
        return x.val;
    }

    private RBTreeNode min(RBTreeNode x) {
        while (x.left != nil) {
            x = x.left;
        }
        return x;
    }


    public boolean search(int val) {
        RBTreeNode x = search(root, val);
        return x != nil;
    }

    private RBTreeNode search(RBTreeNode x, int val) {
        while (x != nil) {
            if (x.val == val) return x;
            else if (val < x.val) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        return nil;
    }

    public void delete(int val) {
        RBTreeNode z = search(root, val);
        if (z == nil) throw new RuntimeException();

        RBTreeNode y = z;//y代表"被删除"的节点
        RBTreeNode x = nil;//x代表移动到"被删除"节点的节点
        Color yOriginColor = y.color;
        if (z.left == nil) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == nil) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = min(z.right);
            yOriginColor = y.color;
            x = y.right;
            transplant(y, x);

            y.right = z.right;
            y.right.parent = y;

            y.left = z.left;
            y.left.parent = y;

            transplant(z, y);
            y.color = z.color;
            //TODO 以上两句改为z.val=y.val应该也是可以的
        }

        if (yOriginColor == BLACK) {
            deleteFix(x);
        }

        if (!check()) throw new RuntimeException();
    }

    private void transplant(RBTreeNode u, RBTreeNode v) {
        v.parent = u.parent;
        if (u.parent == nil) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
    }

    private void deleteFix(RBTreeNode x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                RBTreeNode w = x.parent.right;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                    //这里是可能直接退出循环的,此时x若为红色，那么x就是红色带额外的黑色，因此将其改为黑色就行
                } else {
                    if (w.left.color == RED) {
                        w.left.color = BLACK;
                        w.color = RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }

            } else {
                RBTreeNode w = x.parent.left;
                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                    //这里是可能直接退出循环的,此时x若为红色，那么x就是红色带额外的黑色，因此将其改为黑色就行
                } else {
                    if (w.right.color == RED) {
                        w.right.color = BLACK;
                        w.color = RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    private boolean rule5;

    private boolean check() {
        if (root.color == RED) return false;
        if (nil.color == RED) return false;
        if (!checkRule4(root)) return false;
        rule5 = true;
        checkRule5(root);
        if (!rule5) return false;
        return true;
    }

    private boolean checkRule4(RBTreeNode root) {
        if (root == nil) return true;
        if (root.color == RED &&
                (root.left.color == RED || root.right.color == RED))
            return false;
        return checkRule4(root.left) && checkRule4(root.right);
    }

    private int checkRule5(RBTreeNode root) {
        if (root == nil) return 1;
        int leftBlackHigh = checkRule5(root.left);
        int rightBlackHigh = checkRule5(root.right);
        if (leftBlackHigh != rightBlackHigh) {
            rule5 = false;
            return -1;
        }
        return leftBlackHigh + (root.color == BLACK ? 1 : 0);
    }

    public void preOrderTraverse() {
        StringBuilder sbRecursive = new StringBuilder();
        StringBuilder sbStack = new StringBuilder();
        StringBuilder sbElse = new StringBuilder();

        preOrderTraverseRecursive(root, sbRecursive);
        preOrderTraverseStack(sbStack);
        preOrderTraverseElse(sbElse);

        System.out.println(sbRecursive.toString());
        System.out.println(sbStack.toString());
        System.out.println(sbElse.toString());

        if (!sbRecursive.toString().equals(sbStack.toString()) ||
                !sbRecursive.toString().equals(sbElse.toString()))
            throw new RuntimeException();
    }

    private void preOrderTraverseRecursive(RBTreeNode root, StringBuilder sb) {
        if (root != nil) {
            sb.append(root.val + ", ");
            preOrderTraverseRecursive(root.left, sb);
            preOrderTraverseRecursive(root.right, sb);
        }
    }

    private void preOrderTraverseStack(StringBuilder sb) {
        LinkedList<RBTreeNode> stack = new LinkedList<RBTreeNode>();
        RBTreeNode cur = root;
        while (cur != nil || !stack.isEmpty()) {
            while (cur != nil) {
                sb.append(cur.val + ", ");
                stack.push(cur);
                cur = cur.left;
            }
            if (!stack.isEmpty()) {
                RBTreeNode peek = stack.pop();
                cur = peek.right;
            }
        }
    }

    private void preOrderTraverseElse(StringBuilder sb) {
        RBTreeNode cur = root;
        RBTreeNode pre = nil;
        while (cur != nil) {
            if (pre == cur.parent) {
                sb.append(cur.val + ", ");
                pre = cur;
                if (cur.left != nil) {
                    cur = cur.left;
                } else if (cur.right != nil) {
                    cur = cur.right;
                } else {
                    cur = cur.parent;
                }
            } else if (pre == cur.left) {
                pre = cur;
                if (cur.right != nil) {
                    cur = cur.right;
                } else {
                    cur = cur.parent;
                }
            } else {
                pre = cur;
                cur = cur.parent;
            }
        }
    }


    public void inOrderTraverse() {
        StringBuilder sbRecursive = new StringBuilder();
        StringBuilder sbStack = new StringBuilder();
        StringBuilder sbElse = new StringBuilder();

        inOrderTraverseRecursive(root, sbRecursive);
        inOrderTraverseStack(sbStack);
        inOrderTraverseElse(sbElse);

        System.out.println(sbRecursive.toString());
        System.out.println(sbStack.toString());
        System.out.println(sbElse.toString());

        if (!sbRecursive.toString().equals(sbStack.toString()) ||
                !sbRecursive.toString().equals(sbElse.toString()))
            throw new RuntimeException();
    }

    private void inOrderTraverseRecursive(RBTreeNode root, StringBuilder sb) {
        if (root != nil) {
            inOrderTraverseRecursive(root.left, sb);
            sb.append(root.val + ", ");
            inOrderTraverseRecursive(root.right, sb);
        }
    }

    private void inOrderTraverseStack(StringBuilder sb) {
        LinkedList<RBTreeNode> stack = new LinkedList<RBTreeNode>();
        RBTreeNode cur = root;
        while (cur != nil || !stack.isEmpty()) {
            while (cur != nil) {
                stack.push(cur);
                cur = cur.left;
            }
            if (!stack.isEmpty()) {
                RBTreeNode peek = stack.pop();
                sb.append(peek.val + ", ");
                cur = peek.right;
            }
        }
    }

    private void inOrderTraverseElse(StringBuilder sb) {
        RBTreeNode cur = root;
        RBTreeNode pre = nil;
        while (cur != nil) {
            if (pre == cur.parent) {
                pre = cur;
                if (cur.left != nil) {
                    cur = cur.left;
                } else if (cur.right != nil) {
                    sb.append(cur.val + ", ");
                    cur = cur.right;
                } else {
                    sb.append(cur.val + ", ");
                    cur = cur.parent;
                }
            } else if (pre == cur.left) {
                pre = cur;
                sb.append(cur.val + ", ");
                if (cur.right != nil) {
                    cur = cur.right;
                } else {
                    cur = cur.parent;
                }
            } else {
                pre = cur;
                cur = cur.parent;
            }
        }
    }

    public void postOrderTraverse() {
        StringBuilder sbRecursive = new StringBuilder();
        StringBuilder sbStack1 = new StringBuilder();
        StringBuilder sbStack2 = new StringBuilder();
        StringBuilder sbStack3 = new StringBuilder();
        StringBuilder sbElse = new StringBuilder();

        postOrderTraverseRecursive(root, sbRecursive);
        postOrderTraverseStack1(sbStack1);
        postOrderTraverseStack2(sbStack2);
        postOrderTraverseStack3(sbStack3);
        postOrderTraverseElse(sbElse);

        System.out.println(sbRecursive.toString());
        System.out.println(sbStack1.toString());
        System.out.println(sbStack2.toString());
        System.out.println(sbStack3.toString());
        System.out.println(sbElse.toString());

        if (!sbRecursive.toString().equals(sbStack1.toString()) ||
                !sbRecursive.toString().equals(sbStack2.toString())||
                !sbRecursive.toString().equals(sbStack3.toString())||
                !sbRecursive.toString().equals(sbElse.toString()))
            throw new RuntimeException();
    }

    private void postOrderTraverseRecursive(RBTreeNode root, StringBuilder sb) {
        if (root != nil) {
            postOrderTraverseRecursive(root.left, sb);
            postOrderTraverseRecursive(root.right, sb);
            sb.append(root.val + ", ");
        }
    }

    private void postOrderTraverseStack1(StringBuilder sb) {
        LinkedList<RBTreeNode> stack = new LinkedList<RBTreeNode>();
        RBTreeNode cur = root;
        while (cur != nil || !stack.isEmpty()) {
            while (cur != nil) {
                sb.insert(0,cur.val + ", ");
                stack.push(cur);
                cur = cur.right;
            }
            if (!stack.isEmpty()) {
                RBTreeNode peek = stack.pop();
                cur = peek.left;
            }
        }
    }

    private void postOrderTraverseStack2(StringBuilder sb){
        LinkedList<RBTreeNode> stack = new LinkedList<RBTreeNode>();
        RBTreeNode cur = root;
        Map<RBTreeNode,Integer> map=new HashMap<RBTreeNode,Integer>();
        while(cur!=nil||!stack.isEmpty()){
            while(cur!=nil){
                stack.push(cur);
                map.put(cur,1);
                cur=cur.left;
            }
            if(!stack.isEmpty()){
                RBTreeNode peek=stack.pop();
                if(map.get(peek)==2){
                    sb.append(peek.val+", ");
                    cur=nil;
                }
                else{
                    stack.push(peek);
                    map.put(peek,2);
                    cur=peek.right;
                }
            }
        }
    }

    private void postOrderTraverseStack3(StringBuilder sb){
        RBTreeNode pre=nil;
        LinkedList<RBTreeNode> stack=new LinkedList<RBTreeNode>();
        stack.push(root);
        while(!stack.isEmpty()){
            RBTreeNode peek=stack.peek();
            if(peek.left==nil&&peek.right==nil||pre.parent==peek){
                pre=peek;
                sb.append(peek.val+", ");
                stack.pop();
            }
            else{
                if(peek.right!=nil){
                    stack.push(peek.right);
                }
                if(peek.left!=nil){
                    stack.push(peek.left);
                }
            }
        }
    }

    private void postOrderTraverseElse(StringBuilder sb) {
        RBTreeNode cur = root;
        RBTreeNode pre = nil;
        while (cur != nil) {
            if (pre == cur.parent) {
                pre = cur;
                if (cur.left != nil) {
                    cur = cur.left;
                } else if (cur.right != nil) {
                    cur = cur.right;
                } else {
                    sb.append(cur.val + ", ");
                    cur = cur.parent;
                }
            } else if (pre == cur.left) {
                pre = cur;
                if (cur.right != nil) {
                    cur = cur.right;
                } else {
                    sb.append(cur.val + ", ");
                    cur = cur.parent;
                }
            } else {
                sb.append(cur.val + ", ");
                pre=cur;
                cur = cur.parent;
            }
        }
    }

}


class TestRBTree {
    public static void main(String[] args) {
        RBTree rbTree = new RBTree();
        int n = 10000;
        for (int i = 0; i < n; i++) {
            System.out.println("insert " + i);
            rbTree.insert(i);
        }

        for (int i = 0; i < n; i++) {
            if (!rbTree.search(i)) {
                System.out.println(i + " is not exists!");
                throw new RuntimeException();
            }
        }
        rbTree.preOrderTraverse();
        rbTree.inOrderTraverse();
        rbTree.postOrderTraverse();


        for (int i = 0; i < n; i++) {
            System.out.println("delete " + i);
            rbTree.delete(i);
        }
    }
}