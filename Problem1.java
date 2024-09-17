//Time = O(V+E), Vertices and Edges
//Space = O(V+E)

class Solution {
    // Instance variables to store graph, discovery times, low values, and visited status
    private Map<Integer, List<Integer>> graph;
    private int[] discoveryTime;
    private int[] low;
    private boolean[] visited;
    private List<List<Integer>> bridges;
    private int time = 0; // Global time counter for discovery times

    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        // Initialize the graph as an adjacency list
        graph = new HashMap<>();
        discoveryTime = new int[n];
        low = new int[n];
        visited = new boolean[n];
        bridges = new ArrayList<>();

        // Build the graph from the connections
        for (List<Integer> connection : connections) {
            int u = connection.get(0);
            int v = connection.get(1);
            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
        }

        // Perform DFS from each node if not already visited
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs(i, -1); // Start DFS with parent as -1 for the root node
            }
        }

        // Return the list of bridges
        return bridges;
    }

    // Method to perform DFS and find bridges
    private void dfs(int node, int parent) {
        visited[node] = true; // Mark the node as visited
        discoveryTime[node] = low[node] = ++time; // Set discovery time and low value

        // Explore all adjacent nodes
        for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (neighbor == parent) {
                // Skip the edge back to parent node
                continue;
            }

            if (!visited[neighbor]) {
                // Recur for the neighbor
                dfs(neighbor, node);

                // Check if the subtree rooted at neighbor has a connection back to one of ancestors of node
                low[node] = Math.min(low[node], low[neighbor]);

                // If the lowest reachable vertex from neighbor is only the node itself, it's a bridge
                if (low[neighbor] > discoveryTime[node]) {
                    bridges.add(Arrays.asList(node, neighbor));
                }
            } else {
                // Update the low value of node for back edge (not directly a bridge)
                low[node] = Math.min(low[node], discoveryTime[neighbor]);
            }
        }
    }
}
