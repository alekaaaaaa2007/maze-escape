/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
import java.util.*;

public class Escape {
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells();
        int cols = w.cols();
        int[][] g = Graphs.open(w, broken);

        int deadEnds = 0;
        for (int i = 0; i < n; i++) {
            if (g[i].length == 1) deadEnds++;
        }

        boolean[] vis = new boolean[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        int[] stack = new int[n];
        int[] edgeIdx = new int[n];
        int top = 0;

        stack[0] = 0;
        vis[0] = true;
        int visitedCount = 1;

        while (top >= 0) {
            int u = stack[top];

            if (u == n - 1) break;

            int[] candidates = {u - cols, u + 1, u + cols, u - 1};
            int[] ordered = new int[4];
            int count = 0;

            for (int cand : candidates) {
                for (int v : g[u]) {
                    if (v == cand) {
                        ordered[count++] = cand;
                        break;
                    }
                }
            }

            if (edgeIdx[u] < count) {
                int v = ordered[edgeIdx[u]++];
                if (!vis[v]) {
                    vis[v] = true;
                    visitedCount++;
                    parent[v] = u;
                    stack[++top] = v;
                }
            } else {
                top--;
            }
        }

        int curr = n - 1;
        int pathLen = 0;
        int[] tempPath = new int[n];
        while (curr != -1) {
            tempPath[pathLen++] = curr;
            curr = parent[curr];
        }

        int[] path = new int[pathLen];
        for (int i = 0; i < pathLen; i++) {
            path[i] = tempPath[pathLen - 1 - i];
        }

        return new Result(path, visitedCount, deadEnds);
    }
}
