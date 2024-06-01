package lab22.search;

import java.util.*;

public class GeneralSearch {
    Problem problem;
    CheckerCycle checkerCycle;
    Distance distance;
    public Solution generalSearch(Problem problem, CheckerCycle checkerCycle, Queuing queuing, Distance distance) {
        this.checkerCycle = checkerCycle;
        this.problem = problem;
        this.distance = distance;
        LinkedList<Node> nodes = new LinkedList<>();
        Node startNode = new Node(null, problem.initState, 0, problem.goalState, distance);
        nodes.add(startNode);
        while (true) {
            if (nodes.isEmpty()) return null;
            Node tmpNode = nodes.pop();
            if (problem.goalTest(tmpNode.state)) return new Solution(tmpNode, startNode);
            queuing.setQueue(nodes, expend(tmpNode, problem.operations(tmpNode.state)));
        }
    }

    public List<Node> expend(Node node, List<State> newStates) {
        List<Node> newNodes = new LinkedList<>();
        for (State state: newStates) {
            Node newNode = new Node(node, state, node.depth + 1, this.problem.goalState, distance);
            if (checkerCycle.check(newNode, node.parent)) newNodes.add(newNode);
        }
        return newNodes;
    }
}

interface State {
    int distance(State goal, Distance distance);
}

interface Distance {
    int distance(State state, State goalState);
}

abstract class Problem {
    State initState;
    State goalState;

    public Problem(State initState) {
        this.initState = initState;
    }

    public Problem(State initState, State goalState) {
        this.initState = initState;
        this.goalState = goalState;
    }

    abstract boolean goalTest(State state);

    abstract LinkedList<State> operations(State state);
}


interface CheckerCycle {
    boolean check(Node node, Node parent);
}

class CheckerCycleLastParent implements CheckerCycle {
    public boolean check(Node node, Node parent) {
        return parent == null || !node.state.equals(parent.state);
    }
}

class CheckerCycleParents implements CheckerCycle {
    public boolean check(Node node, Node parent) {
        Node tmpNode = parent;
        while (tmpNode != null) {
            if (node.state.equals(tmpNode.state)) return false;
            tmpNode = tmpNode.parent;
        }
        return true;
    }
}

class CheckerCycleMemory implements CheckerCycle {
    Set<State> previous = new HashSet<>();

    public boolean check(Node node, Node parent) {
        if (previous.contains(node.state)) return false;
        previous.add(node.state);
        return true;
    }
}

class Solution {
    LinkedList<Node> solutionNodes;

    public Solution(Node goalNode, Node startNode) {
        this.solutionNodes = new LinkedList<>();
        Node tmpNode = goalNode;
        solutionNodes.add(tmpNode);
        while (tmpNode != startNode) {
            tmpNode = tmpNode.parent;
            solutionNodes.addFirst(tmpNode);
        }
    }

    @Override
    public String toString() {
        StringBuilder solutionString = new StringBuilder();
        for (Node node: solutionNodes) solutionString.append(node.state).append("\n\n");
        return "Depth: " + solutionNodes.get(solutionNodes.size()-1).depth + "\n" +
                "Solution: \n" + solutionString;
    }

    public String toString(int firstN, int lastN) {
        StringBuilder solutionString = new StringBuilder();

        solutionString.append("First ").append(firstN).append("\n");
        int i = 0;
        for (Node node: solutionNodes) {
            solutionString.append(node.state).append("\n\n");
            i++;
            if (i >= firstN) break;
        }

        solutionString.append("Last ").append(firstN).append("\n");
        List<Node> solutionNodesReversed = solutionNodes.subList(solutionNodes.size()-lastN, solutionNodes.size());
        i = 0;
        for (Node node: solutionNodesReversed) {
            solutionString.append(node.state).append("\n\n");
            i++;
            if (i >= lastN) break;
        }

        return "Depth: " + solutionNodes.get(solutionNodes.size()-1).depth + "\n" +
                "Solution: \n" + solutionString;
    }
}