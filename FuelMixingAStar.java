import java.util.*;

class Node {
    int a, b;        // fuel in tank A and tank B
    int cost;        // number of steps
    Node parent;

    Node(int a, int b, int cost, Node parent) {
        this.a = a;
        this.b = b;
        this.cost = cost;
        this.parent = parent;
    }
}

public class FuelMixingAStar {

    static int TANK_A = 7;
    static int TANK_B = 4;
    static int TARGET = 6;

    // Heuristic function
    static int heuristic(int a) {
        return Math.abs(a - TARGET);
    }

    // Print solution path
    static void printPath(Node node) {
        Stack<Node> stack = new Stack<>();
        while (node != null) {
            stack.push(node);
            node = node.parent;
        }
        System.out.println("Fuel Mixing Steps:");
        while (!stack.isEmpty()) {
            Node n = stack.pop();
            System.out.println("(" + n.a + ", " + n.b + ")");
        }
    }

    public static void main(String[] args) {

        PriorityQueue<Node> pq = new PriorityQueue<>(
                (x, y) -> (x.cost + heuristic(x.a)) - (y.cost + heuristic(y.a))
        );

        Set<String> visited = new HashSet<>();

        // Initial state
        pq.add(new Node(0, 0, 0, null));

        while (!pq.isEmpty()) {

            Node cur = pq.poll();

            // Goal test
            if (cur.a == TARGET) {
                printPath(cur);
                return;
            }

            visited.add(cur.a + "," + cur.b);

            // Fill Tank A
            add(pq, visited, new Node(TANK_A, cur.b, cur.cost + 1, cur));

            // Fill Tank B
            add(pq, visited, new Node(cur.a, TANK_B, cur.cost + 1, cur));

            // Empty Tank A
            add(pq, visited, new Node(0, cur.b, cur.cost + 1, cur));

            // Empty Tank B
            add(pq, visited, new Node(cur.a, 0, cur.cost + 1, cur));

            // Pour A → B
            int pourAB = Math.min(cur.a, TANK_B - cur.b);
            add(pq, visited, new Node(cur.a - pourAB, cur.b + pourAB,
                    cur.cost + 1, cur));

            // Pour B → A
            int pourBA = Math.min(cur.b, TANK_A - cur.a);
            add(pq, visited, new Node(cur.a + pourBA, cur.b - pourBA,
                    cur.cost + 1, cur));
        }

        System.out.println("No valid fuel mix found.");
    }

    static void add(PriorityQueue<Node> pq, Set<String> visited, Node n) {
        String key = n.a + "," + n.b;
        if (!visited.contains(key)) {
            pq.add(n);
        }
    }
}
