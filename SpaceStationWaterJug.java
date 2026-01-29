import java.util.*;

class Node {
    int x, y;          // water in containers X and Y
    int cost;          // number of steps
    Node parent;

    Node(int x, int y, int cost, Node parent) {
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.parent = parent;
    }
}

public class SpaceStationWaterJug {

    static int X = 7;       // capacity of container X
    static int Y = 4;       // capacity of container Y
    static int TARGET = 6;  // required water in X

    // Heuristic function
    static int heuristic(int x) {
        return Math.abs(x - TARGET);
    }

    // Print solution path
    static void printPath(Node node) {
        Stack<Node> stack = new Stack<>();
        while (node != null) {
            stack.push(node);
            node = node.parent;
        }
        System.out.println("Solution Path:");
        while (!stack.isEmpty()) {
            Node n = stack.pop();
            System.out.println("(" + n.x + ", " + n.y + ")");
        }
    }

    public static void main(String[] args) {

        PriorityQueue<Node> pq = new PriorityQueue<>(
                (a, b) -> (a.cost + heuristic(a.x)) - (b.cost + heuristic(b.x))
        );

        Set<String> visited = new HashSet<>();

        // Initial state
        pq.add(new Node(0, 0, 0, null));

        while (!pq.isEmpty()) {

            Node cur = pq.poll();

            // Goal test
            if (cur.x == TARGET) {
                printPath(cur);
                return;
            }

            visited.add(cur.x + "," + cur.y);

            // 1. Fill container X
            add(pq, visited, new Node(X, cur.y, cur.cost + 1, cur));

            // 2. Fill container Y
            add(pq, visited, new Node(cur.x, Y, cur.cost + 1, cur));

            // 3. Empty container X
            add(pq, visited, new Node(0, cur.y, cur.cost + 1, cur));

            // 4. Empty container Y
            add(pq, visited, new Node(cur.x, 0, cur.cost + 1, cur));

            // 5. Pour X → Y
            int pourXY = Math.min(cur.x, Y - cur.y);
            add(pq, visited, new Node(cur.x - pourXY, cur.y + pourXY,
                    cur.cost + 1, cur));

            // 6. Pour Y → X
            int pourYX = Math.min(cur.y, X - cur.x);
            add(pq, visited, new Node(cur.x + pourYX, cur.y - pourYX,
                    cur.cost + 1, cur));
        }

        System.out.println("No solution found.");
    }

    static void add(PriorityQueue<Node> pq, Set<String> visited, Node n) {
        String key = n.x + "," + n.y;
        if (!visited.contains(key)) {
            pq.add(n);
        }
    }
}
