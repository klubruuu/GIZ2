package com.klubru.giz;

import java.util.*;
import java.util.LinkedList;

public class Graph
{
    private int V;
    private LinkedList<Integer> adj[];
    private int time = 0;

    private Graph(int v) {
        V = v;
        adj = new LinkedList[v];
        for (int i=0; i<v; ++i)
            adj[i] = new LinkedList();
    }

    private void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
    }


    void findBridges() {
        time = 0;
        boolean visited[] = new boolean[V];
        int disc[] = new int[V];
        int low[] = new int[V];
        int parent[] = new int[V];


        for (int i = 0; i < V; i++) {
            parent[i] = -1;
            visited[i] = false;
        }

        for (int i = 0; i < V; i++)
            if (!visited[i])
                bridgeUtil(i, visited, disc, low, parent);
    }
    private void bridgeUtil(
            int u,
            boolean visited[],
            int disc[],
            int low[],
            int parent[]
    ) {
        visited[u] = true;

        disc[u] = low[u] = ++time;
        for (Integer v : adj[u]) {
            if (!visited[v]) {
                parent[v] = u;
                bridgeUtil(v, visited, disc, low, parent);

                low[u] = Math.min(low[u], low[v]);
                if (low[v] > disc[u])
                    System.out.println((u + 1) + " " + (v + 1));
            } else if (v != parent[u])
                low[u] = Math.min(low[u], disc[v]);
        }
    }

    void findArticulationPoints() {
        time = 0;
        boolean visited[] = new boolean[V];
        int disc[] = new int[V];
        int low[] = new int[V];
        int parent[] = new int[V];
        boolean ap[] = new boolean[V];

        for (int i = 0; i < V; i++) {
            parent[i] = -1;
            visited[i] = false;
            ap[i] = false;
        }

        for (int i = 0; i < V; i++)
            if (!visited[i])
                APUtil(i, visited, disc, low, parent, ap);

        for (int i = 0; i < V; i++)
            if (ap[i])
                System.out.print((i+1)+" ");
    }

    private void APUtil(
            int u,
            boolean visited[],
            int disc[],
            int low[],
            int parent[],
            boolean ap[]
    ) {
        int children = 0;
        visited[u] = true;

        disc[u] = low[u] = ++time;

        for (Integer v : adj[u]) {
            if (!visited[v]) {
                children++;
                parent[v] = u;
                APUtil(v, visited, disc, low, parent, ap);

                low[u] = Math.min(low[u], low[v]);

                if (parent[u] == -1 && children > 1)
                    ap[u] = true;

                if (parent[u] != -1 && low[v] >= disc[u])
                    ap[u] = true;
            } else if (v != parent[u])
                low[u] = Math.min(low[u], disc[v]);
        }
    }

    public static Graph buildGraph(int nodesCount, List<String> data) {
        Graph graph = new Graph(nodesCount);
        processGraphData(graph, data);

        return graph;
    }

    private static void processGraphData(Graph graph, List<String> data) {
        for (int i = 0; i < data.size(); i++)
            processNodeDef(graph, data.get(i), i);
    }

    private static void processNodeDef(Graph graph, String nodeDef, int nodeIndex) {
        if (nodeDef.isEmpty())
            return;

        String[] indices = nodeDef.split(" ");
        for (int i = 0; i < indices.length; i++) {
            int index = Integer.parseInt(indices[i]) - 1;
            graph.addEdge(nodeIndex, index);
        }
    }
}
