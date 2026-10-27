/**
 * Missions 4 and 5. The slides: a directed graph. Cells are 0 .. n-1, slide i goes from s.from()[i] to s.to()[i]
 * and takes s.time()[i]. There are no slides from a cell to itself, and the exit can always be reached from the start.
 *
 * Ready for you: Graphs.slidesFrom(s)[u] is the list of the slides that leave cell u, and Graphs.slidesInto(s)[v] is the list
 * of the slides that enter cell v (reversed graph). The lists hold slide numbers i; the cell at the other end is s.to()[i]
 * or s.from()[i].
 */

import java.util.Arrays;

public class Zones {

    // Миссия 4: Алгоритм Косараджу
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();
        int[][] out = Graphs.slidesFrom(s);
        int[][] in = Graphs.slidesInto(s);

        boolean[] vis = new boolean[n];
        int[] order = new int[n];
        int orderIdx = 0;

        int[] stack = new int[n];
        int[] edgeIdx = new int[n];

        // Первый проход
        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                int top = 0;
                stack[0] = i;
                vis[i] = true;

                while (top >= 0) {
                    int u = stack[top];
                    if (edgeIdx[u] < out[u].length) {
                        int slideIdx = out[u][edgeIdx[u]++];
                        int v = s.to()[slideIdx];
                        if (!vis[v]) {
                            vis[v] = true;
                            stack[++top] = v;
                        }
                    } else {
                        order[orderIdx++] = u;
                        top--;
                    }
                }
            }
        }

        Arrays.fill(vis, false);
        int[] zoneOf = new int[n];
        int currentZone = 0;
        Arrays.fill(edgeIdx, 0);

        // Второй проход
        for (int i = n - 1; i >= 0; i--) {
            int startNode = order[i];
            if (!vis[startNode]) {
                int top = 0;
                stack[0] = startNode;
                vis[startNode] = true;
                zoneOf[startNode] = currentZone;

                while (top >= 0) {
                    int u = stack[top];
                    if (edgeIdx[u] < in[u].length) {
                        int slideIdx = in[u][edgeIdx[u]++];
                        int v = s.from()[slideIdx];
                        if (!vis[v]) {
                            vis[v] = true;
                            zoneOf[v] = currentZone;
                            stack[++top] = v;
                        }
                    } else {
                        top--;
                    }
                }
                currentZone++;
            }
        }
        return zoneOf;
    }

    // Миссия 5: Маршруты и поток
    public static long[] routes(Input.Slides s, int[] zoneOf, int zones) {
        long[] stages = new long[zones];
        Arrays.fill(stages, 1);
        long INF = Long.MAX_VALUE / 2;
        long[] cost = new long[zones];
        Arrays.fill(cost, INF);
        int startZone = zoneOf[0];
        int exitZone = zoneOf[s.n() - 1];
        cost[startZone] = 0;

        int[] head = new int[zones];
        int[] next = new int[s.n()];
        Arrays.fill(head, -1);
        for (int i = 0; i < s.n(); i++) {
            int z = zoneOf[i];
            next[i] = head[z];
            head[z] = i;
        }

        int[][] out = Graphs.slidesFrom(s);
        int[] minWeight = new int[zones];
        Arrays.fill(minWeight, Integer.MAX_VALUE);
        int[] adjZones = new int[s.n()];

        long maxStages = 0;

        for (int z = 0; z < zones; z++) {
            maxStages = Math.max(maxStages, stages[z]);

            int adjCount = 0;
            for (int u = head[z]; u != -1; u = next[u]) {
                for (int slideIdx : out[u]) {
                    int v = s.to()[slideIdx];
                    int zV = zoneOf[v];
                    if (zV != z) {
                        int w = s.time()[slideIdx];
                        if (minWeight[zV] == Integer.MAX_VALUE) {
                            adjZones[adjCount++] = zV;
                        }
                        minWeight[zV] = Math.min(minWeight[zV], w);
                    }
                }
            }

            for (int i = 0; i < adjCount; i++) {
                int zV = adjZones[i];
                int w = minWeight[zV];
                stages[zV] = Math.max(stages[zV], stages[z] + 1);
                if (cost[z] != INF) {
                    cost[zV] = Math.min(cost[zV], cost[z] + w);
                }
                minWeight[zV] = Integer.MAX_VALUE;
            }
        }

        for (int z = 0; z < zones; z++) {
            maxStages = Math.max(maxStages, stages[z]);
        }

        return new long[]{maxStages, cost[exitZone]};
    }
}