/**
 * Mission 0. Disjoint set union (DSU).
 * Cells are numbered 0 .. n-1. Write the whole class here.
 * Needed: union by size (the smaller tree goes under the larger one) and path compression in find.
 */
public class DisjointSet {
    private int[] parent;
    private int[] size;
    private int groups;

    public DisjointSet(int n) {
        parent = new int[n];
        size = new int[n];
        groups = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int i) {
        if (parent[i] == i) {
            return i;
        }
        parent[i] = find(parent[i]);
        return parent[i];
    }

    public boolean union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI == rootJ) {
            return false;
        }

        if (size[rootI] < size[rootJ]) {
            parent[rootI] = rootJ;
            size[rootJ] += size[rootI];
        } else {
            parent[rootJ] = rootI;
            size[rootI] += size[rootJ];
        }
        groups--;
        return true;
    }

    public boolean connected(int i, int j) {
        return find(i) == find(j);
    }

    public int count() {
        return groups;
    }
}