package com.sunland.datastructure.tree.btree;

/**
 * Created by HCF on 2017/4/5.
 */

class BTreeNode {
    int n;

    int[] keys;

    BTreeNode[] children;

    boolean isLeaf;

    BTreeNode(int t) {
        n = 0;
        keys = new int[2 * t - 1];
        children = new BTreeNode[2 * t];
        isLeaf = false;
    }
}

public class BTree {
    private int t;

    private BTreeNode root;

    private BTreeNode createNode() {
        return new BTreeNode(t);
    }

    public BTree(int t) {
        this.t = t;
        this.root = createNode();
        this.root.isLeaf = true;
    }

    public void insert(int k) {
        if (root.n == 2 * t - 1) {
            BTreeNode s = createNode();
            s.isLeaf = false;
            s.children[0] = root;
            root = s;
            split(root, 0);
        }
        insertNotFull(root, k);
    }

    private void split(BTreeNode x, int i) {
        BTreeNode z = createNode();
        BTreeNode y = x.children[i];
        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
        }
        if (!y.isLeaf) {
            for (int j = 0; j < t; j++) {
                z.children[j] = y.children[j + t];
            }
        }
        for (int j = x.n; j > i; j--) {
            x.keys[j] = x.keys[j - 1];
            x.children[j + 1] = x.children[j];
        }
        x.keys[i] = y.keys[t - 1];
        x.children[i + 1] = z;
        x.n++;

        z.n = y.n = t - 1;
        z.isLeaf = y.isLeaf;
    }

    private void insertNotFull(BTreeNode x, int k) {
        int i = x.n - 1;
        if (x.isLeaf) {
            while (i >= 0 && x.keys[i] >= k) {
                x.keys[i + 1] = x.keys[i];
                i--;
            }
            i++;
            x.keys[i] = k;
            x.n++;
        } else {
            while (i >= 0 && x.keys[i] >= k) {
                i--;
            }
            i++;
            if (x.children[i].n == 2 * t - 1) {
                split(x, i);
                if (k > x.keys[i]) {
                    i++;
                }
            }
            insertNotFull(x.children[i], k);
        }
    }

    public void insert(int[] keys) {
        for (int key : keys) {
            insert(key);
        }
    }

    public void delete(int k) {
        if (root.n == 1) {
            if (!root.isLeaf && root.children[0].n == t - 1 && root.children[1].n == t - 1) {
                merge(root, 0);
                root = root.children[0];
            }
        }
        deleteNotNone(root, k);
    }

    private void merge(BTreeNode x, int i) {
        BTreeNode y = x.children[i];
        BTreeNode z = x.children[i + 1];
        for (int j = 0; j < t - 1; j++) {
            y.keys[j + t] = z.keys[j];
        }
        if (!y.isLeaf) {
            for (int j = 0; j < t; j++) {
                y.children[j + t] = z.children[j];
            }
        }

        y.keys[t - 1] = x.keys[i];
        for (int j = i; j < x.n - 1; j++) {
            x.keys[j] = x.keys[j + 1];
            x.children[j + 1] = x.children[j + 2];
        }
        x.n--;
        y.n = 2 * t - 1;
    }

    private void deleteNotNone(BTreeNode x, int k) {
        int i = 0;
        if (x.isLeaf) {
            while (i < x.n && k > x.keys[i]) {
                i++;
            }
            if (x.keys[i] != k) throw new RuntimeException("no such an element");
            while (i < x.n - 1) {
                x.keys[i] = x.keys[i + 1];
                i++;
            }
            x.n--;
        } else {
            while (i < x.n && k > x.keys[i]) {
                i++;
            }
            BTreeNode y = x.children[i];
            BTreeNode z = null;
            if (i < x.n) {
                z = x.children[i + 1];
            }
            if (i < x.n && x.keys[i] == k) {
                if (y.n > t - 1) {
                    int kk = maximum(x.children[i]);
                    deleteNotNone(x.children[i], kk);
                    x.keys[i] = kk;
                } else if (z.n > t - 1) {
                    int kk = minimum(x.children[i + 1]);
                    deleteNotNone(x.children[i + 1], kk);
                    x.keys[i] = kk;
                } else {
                    merge(x, i);
                    deleteNotNone(x.children[i], k);
                }
            } else {
                BTreeNode p = null;
                if (i > 0) {
                    p = x.children[i - 1];
                }
                if (y.n == t - 1) {
                    if (p != null && p.n > t - 1) {
                        shiftToRight(x, i - 1, p, y);
                    } else if (z != null && z.n > t - 1) {
                        shiftToLeft(x, i, y, z);
                    } else if (p != null) {
                        merge(x, i - 1);
                        y = p;
                    } else {
                        merge(x, i);
                    }
                    deleteNotNone(y, k);
                }
            }
        }
    }

    private int maximum(BTreeNode x) {
        while (!x.isLeaf) {
            x = x.children[x.n];
        }
        return x.keys[x.n - 1];
    }

    private int minimum(BTreeNode x) {
        while (!x.isLeaf) {
            x = x.children[0];
        }
        return x.keys[0];
    }

    private void shiftToRight(BTreeNode x, int i, BTreeNode p, BTreeNode y) {
        for (int j = y.n; j > 0; j--) {
            y.keys[j] = y.keys[j - 1];
        }
        y.keys[0] = x.keys[i];
        x.keys[i] = p.keys[p.n - 1];

        if (!y.isLeaf) {
            for (int j = y.n + 1; j > 0; j--) {
                y.children[j] = y.children[j - 1];
            }
            y.children[0] = p.children[p.n];
        }

        y.n++;
        p.n--;
    }

    private void shiftToLeft(BTreeNode x, int i, BTreeNode y, BTreeNode z) {
        y.keys[y.n] = x.keys[i];
        x.keys[i] = z.keys[0];
        for (int j = 0; j < z.n - 1; j++) {
            z.keys[j] = z.keys[j + 1];
        }
        if (!y.isLeaf) {
            y.children[y.n + 1] = z.children[0];
            for (int j = 0; j < z.n; j++) {
                z.children[j] = z.children[j + 1];
            }
        }
        y.n++;
        z.n--;
    }

    public void print() {
        print(root);
        System.out.println();
    }

    private void print(BTreeNode x) {
        if (x.isLeaf) {
            for (int i = 0; i < x.n; i++) {
                System.out.print(x.keys[i] + ", ");
            }
        } else {
            for (int i = 0; i < x.n; i++) {
                print(x.children[i]);
                System.out.print(x.keys[i] + ", ");
            }
            print(x.children[x.n]);
        }
    }

    public boolean search(int k) {
        return search(root, k);
    }

    private boolean search(BTreeNode x, int k) {
        if (x.isLeaf) {
            for (int i = 0; i < x.n; i++) {
                if (k == x.keys[i]) return true;
            }
            return false;
        } else {
            int i = 0;
            while (i < x.n && k > x.keys[i]) {
                i++;
            }
            if (i < x.n && k == x.keys[i]) return true;
            return search(x.children[i], k);
        }
    }

    public int successor(int k) {
        if (!search(k) || k == maximum(root)) throw new RuntimeException();
        return successor(root, k);
    }

    private int successor(BTreeNode x, int k) {
        int i = 0;
        if (x.isLeaf) {
            while (x.keys[i] <= k) {
                i++;
            }
            //i must less than x.n
            return x.keys[i];
        } else {
            while (i < x.n && x.keys[i] <= k) {
                i++;
            }
            if (k >= maximum(x.children[i])) {
                //i couldn't equals x.n
                return x.keys[i];
            } else {
                return successor(x.children[i], k);
            }
        }
    }

    public int precursor(int k) {
        if (!search(k) || k == minimum(root)) throw new RuntimeException();
        return precursor(root, k);
    }

    private int precursor(BTreeNode x, int k) {
        int i = x.n - 1;
        if (x.isLeaf) {
            while (x.keys[i] >= k) {
                i--;
            }
            //i must no less than 0
            return x.keys[i];
        } else {
            while (i >= 0 && x.keys[i] >= k) {
                i--;
            }
            if (k <= minimum(x.children[i + 1])) {
                //i must large than 0
                return x.keys[i];
            } else {
                return precursor(x.children[i + 1], k);
            }
        }
    }
}


class TestBtree {
    public static void main(String[] args) {
        int n = 1000;
        BTree bTree = new BTree(6);
        for (int i = 0; i < n; i++) {
            System.out.println("insert " + i);
            bTree.insert(i);
        }

        for (int i = 0; i < n; i++) {
            if (!bTree.search(i)) throw new RuntimeException();
        }
        bTree.print();

        for (int i = 0; i < n - 1; i++) {
            int successor = bTree.successor(i);
            if (successor != i + 1) throw new RuntimeException();
        }

        for (int i = 1; i < n; i++) {
            int precursor = bTree.precursor(i);
            if (precursor != i - 1) throw new RuntimeException();
        }

        for (int i = 0; i < n; i++) {
            System.out.println("delete " + i);
            bTree.delete(i);
        }
    }
}
