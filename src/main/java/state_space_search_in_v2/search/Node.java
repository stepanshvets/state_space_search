package lab22.search;

public class Node {
    Node parent;
    State state;
    public int depth;
    int costToGoal;
    int finalCost;

    public Node(Node parent, State state, int depth, State goalState, Distance distance) {
        this.parent = parent;
        this.state = state;
        this.depth = depth;
        this.costToGoal = state.distance(goalState, distance);
        this.finalCost = this.depth + this.costToGoal;
    }
}
