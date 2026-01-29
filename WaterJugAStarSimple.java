import java.util.*;

class Node {
    int a, b;     // water in jug1 and jug2
    int cost;     // steps taken
    Node parent;

    Node(int a, int b, int cost, Node parent) {
        this.a = a;
        this.b = b;
        this.cost = cost;
        this.parent = parent;
    }
}

public class WaterJugAStarSimple {

    static int JUG1 = 4;s
    static int JUG2 = 3;
    static int TARGET = 2;

    // simple heuristic
    static int h(int a, int b) {
        return Math.min(Math.abs(a - TARGET), Math.abs(b - TARGET));
    }

    static void printPath(Node node) {
        Stack<Node> s = new Stack<>();
        while (node != null) {
            s.push(node);
            node = node.parent;
        }
        while (!s.isEmpty()) {
            Node n = s.pop();
            System.out.println("(" + n.a + ", " + n.b + ")");
        }
    }

    public static void main(String[] args) {

        PriorityQueue<Node> pq = new PriorityQueue<>(
                (x, y) -> (x.cost + h(x.a, x.b)) - (y.cost + h(y.a, y.b))
        );

        Set<String> visited = new HashSet<>();

        pq.add(new Node(0, 0, 0, null));

        while (!pq.isEmpty()) {

            Node cur = pq.poll();

            if (cur.a == TARGET || cur.b == TARGET) {
                System.out.println("Solution:");
                printPath(cur);
                return;
            }

            visited.add(cur.a + "," + cur.b);

            // Fill jug1
            addState(pq, visited, new Node(JUG1, cur.b, cur.cost + 1, cur));

            // Fill jug2
            addState(pq, visited, new Node(cur.a, JUG2, cur.cost + 1, cur));

            // Empty jug1
            addState(pq, visited, new Node(0, cur.b, cur.cost + 1, cur));

            // Empty jug2
            addState(pq, visited, new Node(cur.a, 0, cur.cost + 1, cur));

            // Pour jug1 -> jug2
            int p1 = Math.min(cur.a, JUG2 - cur.b);
            addState(pq, visited, new Node(cur.a - p1, cur.b + p1, cur.cost + 1, cur));

            // Pour jug2 -> jug1
            int p2 = Math.min(cur.b, JUG1 - cur.a);
            addState(pq, visited, new Node(cur.a + p2, cur.b - p2, cur.cost + 1, cur));
        }

        System.out.println("No solution found");
    }

    static void addState(PriorityQueue<Node> pq, Set<String> visited, Node n) {
        String key = n.a + "," + n.b;
        if (!visited.contains(key)) {
            pq.add(n);
        }
    }
}