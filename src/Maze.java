/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
import java.util.*;

public class Maze {

    // Миссия 1: Проходим по стенам в порядке файла и возвращаем массив индексов сломанных стен
    public static int[] breakWalls(Input.Walls walls) {
        DisjointSet dsu = new DisjointSet(walls.cells());
        int[] temp = new int[walls.count()];
        int count = 0;

        for (int i = 0; i < walls.count(); i++) {
            if (dsu.union(walls.a()[i], walls.b()[i])) {
                temp[count++] = i;
            }
        }
        return Arrays.copyOf(temp, count);
    }

    // Миссия 2: Краскал. Сортируем индексы стен по их стоимости и возвращаем индексы сломанных стен
    public static int[] cheapestMaze(Input.Walls walls) {
        DisjointSet dsu = new DisjointSet(walls.cells());
        Integer[] indices = new Integer[walls.count()];
        for (int i = 0; i < walls.count(); i++) {
            indices[i] = i;
        }

        // Сортируем индексы по возрастанию стоимости (cost)
        Arrays.sort(indices, Comparator.comparingInt(i -> walls.cost()[i]));

        int[] temp = new int[walls.count()];
        int count = 0;
        for (int i : indices) {
            if (dsu.union(walls.a()[i], walls.b()[i])) {
                temp[count++] = i;
            }
        }
        return Arrays.copyOf(temp, count);
    }

    // Миссия 2: Прим. Ищем стоимость MST с помощью PriorityQueue
    public static long primCost(Input.Walls walls) {
        int n = walls.cells();
        // Строим список смежности для алгоритма Прима
        List<List<int[]>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int i = 0; i < walls.count(); i++) {
            int u = walls.a()[i];
            int v = walls.b()[i];
            int c = walls.cost()[i];
            adj.get(u).add(new int[]{v, c});
            adj.get(v).add(new int[]{u, c});
        }

        boolean[] visited = new boolean[n];
        // Очередь с приоритетом сортирует по стоимости (индекс 1)
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(edge -> edge[1]));

        visited[0] = true;
        for (int[] edge : adj.get(0)) {
            pq.add(edge);
        }

        long cost = 0;
        int connectedCount = 1;

        while (!pq.isEmpty() && connectedCount < n) {
            int[] edge = pq.poll();
            int nextNode = edge[0];
            int weight = edge[1];

            if (!visited[nextNode]) {
                visited[nextNode] = true;
                cost += weight;
                connectedCount++;

                for (int[] nextEdge : adj.get(nextNode)) {
                    if (!visited[nextEdge[0]]) {
                        pq.add(nextEdge);
                    }
                }
            }
        }
        return cost;
    }
}