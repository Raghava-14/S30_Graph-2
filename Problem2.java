//Time : O(N^2)
//Space : O(N)

class Solution {
    public int minMalwareSpread(int[][] graph, int[] initial) {
        int n = graph.length;
        // Union-Find structure with path compression and union by size
        int[] parent = new int[n];
        int[] size = new int[n];
        Arrays.fill(parent, -1);

        // Helper function to find the root of a component
        int find(int x) {
            if (parent[x] < 0) return x;
            parent[x] = find(parent[x]);  // Path compression
            return parent[x];
        }

        // Helper function to union two components
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                if (size[rootX] < size[rootY]) {
                    rootX = rootY;
                }
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }

        // Create components using Union-Find
        for (int i = 0; i < n; i++) {
            size[i] = 1;  // Initialize size of each component as 1
            for (int j = 0; j < n; j++) {
                if (graph[i][j] == 1) {
                    union(i, j);
                }
            }
        }

        // Count malware per component
        Map<Integer, Set<Integer>> componentMalware = new HashMap<>();
        for (int node : initial) {
            int root = find(node);
            componentMalware.computeIfAbsent(root, k -> new HashSet<>()).add(node);
        }

        // Determine best node to remove
        int[] componentSize = new int[n];
        Arrays.fill(componentSize, 0);
        for (int i = 0; i < n; i++) {
            int root = find(i);
            componentSize[root]++;
        }

        int minNode = Integer.MAX_VALUE;
        int minSize = Integer.MAX_VALUE;

        for (int node : initial) {
            int root = find(node);
            // Size of the component if we remove this node
            int sizeIfRemoved = componentSize[root];
            // Malware count in this component
            int malwareCount = componentMalware.get(root).size();

            if (malwareCount == 1) {
                // If removing this node removes all malware in the component
                if (sizeIfRemoved < minSize || (sizeIfRemoved == minSize && node < minNode)) {
                    minNode = node;
                    minSize = sizeIfRemoved;
                }
            }
        }

        return minNode == Integer.MAX_VALUE ? Arrays.stream(initial).min().orElse(-1) : minNode;
    }
}
