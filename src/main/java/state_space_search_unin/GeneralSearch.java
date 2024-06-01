package lab2;

import java.util.*;

public class GeneralSearch {
    Problem problem;
    CheckerCycle checkerCycle;
    public Solution generalSearch(Problem problem, CheckerCycle checkerCycle, Queuing queuing) {
        this.checkerCycle = checkerCycle;
        this.problem = problem;
        LinkedList<Node> nodes = new LinkedList<>();
        Node startNode = new Node(null, problem.initState, 0, problem.goalState);
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
            Node newNode = new Node(node, state, node.depth + 1, this.problem.goalState);
            if (checkerCycle.check(newNode, node.parent)) newNodes.add(newNode);
        }
        return newNodes;
    }

    public Solution generalSearchPrint(Problem problem, CheckerCycle checkerCycle, Queuing queuing) {
        this.checkerCycle = checkerCycle;
        this.problem = problem;
        Scanner scanner = new Scanner(System.in);
        LinkedList<Node> nodes = new LinkedList<>();
        Node startNode = new Node(null, problem.initState, 0, problem.goalState);
        nodes.add(startNode);
        while (true) {
            if (nodes.isEmpty()) return null;
            Node tmpNode = nodes.pop();
            System.out.println("Temporary node\n" + tmpNode);
            if (problem.goalTest(tmpNode.state)) return new Solution(tmpNode, startNode);
            queuing.setQueue(nodes, expend(tmpNode, problem.operations(tmpNode.state)));
            scanner.nextLine();
        }
    }
}

interface Queuing {
    void setQueue(LinkedList<Node> queue, List<Node> nodes);
}

class DFSQueuing implements Queuing{
    public void setQueue(LinkedList<Node> queue, List<Node> nodes) {
        for (Node node : nodes) {
            queue.add(0, node);
        }
    }
}

class BFSQueuing implements Queuing{
    public void setQueue(LinkedList<Node> queue, List<Node> nodes) {
        queue.addAll(nodes);
    }
}

class DFSLimQueuing implements Queuing{
    int depthMax;

    public DFSLimQueuing(int depthMax) {
        this.depthMax = depthMax;
    }

    public void setQueue(LinkedList<Node> queue, List<Node> nodes) {
        for (Node node : nodes) {
            if (node.depth < depthMax)
                queue.add(0, node);
            else
                break;
        }
    }
}

class BestQueuing implements Queuing {
    public void setQueue(LinkedList<Node> queue, List<Node> nodes) {
        queue.addAll(nodes);
        Collections.sort(queue);
//        for (Node node : nodes) {
//            int index = Collections.binarySearch(queue, node);
//            if (index >= 0) queue.add(index, node);
//            else if (index == -1) queue.add(0, node);
//            else queue.add(index * (-1) - 1, node);
//        }
    }
}

class Node implements Comparable<Node> {
    Node parent;
    State state;
    int depth;
    int costToGoal;

    public Node(Node parent, State state, int depth, State goalState) {
        this.parent = parent;
        this.state = state;
        this.depth = depth;
        this.costToGoal = state.distance(goalState);
    }

    @Override
    public int compareTo(Node node) {
        if (this.costToGoal > node.costToGoal) return 1;
        else if (this.costToGoal < node.costToGoal) return -1;
        return 0;
    }

    @Override
    public String toString() {
        return "Node{" +
                "depth=" + depth +
                ", costToGoal=" + costToGoal +
                ", state=\n" + state +
                '}';
    }
}

interface State {
    int distance(State state);
}

class StatePuzzleEight implements State {
    byte[] positionNumber = new byte[9];
    byte zeroPosition;

    StatePuzzleEight (byte ... positionNumber) {
        for (byte i = 0; i < 9; i++) this.positionNumber[i] = positionNumber[i];
        for (byte i = 0; i < 9; i++) if (positionNumber[i] == 0) zeroPosition = i;
    }

    @Override
    public int distance(State state) {
        StatePuzzleEight statePuzzleEight =  (StatePuzzleEight) state;
        int count = 0;
        for (byte i = 0; i < 9; i++) {
            if (this.positionNumber[i] != statePuzzleEight.positionNumber[i]) count++;
        }
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatePuzzleEight state = (StatePuzzleEight) o;

        if (zeroPosition != state.zeroPosition) return false;
        return Arrays.equals(positionNumber, state.positionNumber);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(positionNumber);
        result = 31 * result + (int) zeroPosition;
        return result;
    }

    @Override
    public String toString() {
        return positionNumber[0] + " " + positionNumber[1] + " " + positionNumber[2] + "\n" +
                positionNumber[3] + " " + positionNumber[4] + " " + positionNumber[5] + "\n" +
                positionNumber[6] + " " + positionNumber[7] + " " + positionNumber[8];
    }
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

class ProblemPuzzleEight extends Problem {

    public ProblemPuzzleEight(State initState) {
        super(initState);
        this.goalState = new StatePuzzleEight(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 0});
    }

    public ProblemPuzzleEight(State initState, State goalState) {
        super(initState, goalState);
    }

    public boolean goalTest(State state) {
        return state.equals(this.goalState);
    }

    @Override
    public LinkedList<State> operations(State state) {
        StatePuzzleEight statePuzzleEight = (StatePuzzleEight) state;
        byte zeroPosition = statePuzzleEight.zeroPosition;
        byte[] positionNumber = statePuzzleEight.positionNumber;

        LinkedList<State> newStates = new LinkedList<>();

        //swap zero and left number
        if (zeroPosition % 3 != 0)
            newStates.add(new StatePuzzleEight(swap(positionNumber.clone(), zeroPosition, (byte) (zeroPosition - 1))));

        if (zeroPosition % 3 != 2)
            newStates.add(new StatePuzzleEight(swap(positionNumber.clone(), zeroPosition, (byte) (zeroPosition + 1))));

        //swap zero and top number
        if (zeroPosition / 3 != 0)
            newStates.add(new StatePuzzleEight(swap(positionNumber.clone(), zeroPosition, (byte) (zeroPosition - 3))));

        //swap zero and bottom number
        if (zeroPosition / 3 != 2)
            newStates.add(new StatePuzzleEight(swap(positionNumber.clone(), zeroPosition, (byte) (zeroPosition + 3))));

        return newStates;
    }

    public byte[] swap(byte[] positionNumber, byte firstPosition, byte secondPosition) {
        byte tmp = positionNumber[firstPosition];
        positionNumber[firstPosition] = positionNumber[secondPosition];
        positionNumber[secondPosition] = tmp;
        return positionNumber;
    }


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

class BFS {
    GeneralSearch generalSearch;
    CheckerCycle checkerCycle;
    Queuing queuing;

    BFS() {
        this.generalSearch = new GeneralSearch();
        this.checkerCycle = new CheckerCycleMemory();
        this.queuing = new BFSQueuing();
    }

    BFS(CheckerCycle checkerCycle) {
        this.generalSearch = new GeneralSearch();
        this.checkerCycle = checkerCycle;
        this.queuing = new BFSQueuing();
    }

    public Solution search(Problem problem) {
        return generalSearch.generalSearch(problem, checkerCycle, queuing);
    }
}

class DFS {
    GeneralSearch generalSearch;
    CheckerCycle checkerCycle;
    Queuing queuing;

    DFS() {
        this.generalSearch = new GeneralSearch();
        this.checkerCycle = new CheckerCycleLastParent();
        this.queuing = new DFSQueuing();
    }

    DFS(CheckerCycle checkerCycle) {
        this.generalSearch = new GeneralSearch();
        this.checkerCycle = checkerCycle;
        this.queuing = new DFSQueuing();
    }

    public Solution search(Problem problem) {
        return generalSearch.generalSearch(problem, checkerCycle, queuing);
    }
}

class DFSLim {
    GeneralSearch generalSearch;
    CheckerCycle checkerCycle;
    Queuing queuing;

    DFSLim(int depthMax) {
        this.generalSearch = new GeneralSearch();
        this.checkerCycle = new CheckerCycleParents();
        this.queuing = new DFSLimQueuing(depthMax);
    }

    DFSLim(CheckerCycle checkerCycle, int depthMax) {
        this.generalSearch = new GeneralSearch();
        this.checkerCycle = checkerCycle;
        this.queuing = new DFSLimQueuing(depthMax);
    }

    public Solution search(Problem problem) {
        return generalSearch.generalSearch(problem, checkerCycle, queuing);
    }
}

class DFSIterative{
    GeneralSearch generalSearch;
    CheckerCycle checkerCycle;

    DFSIterative() {
        this.generalSearch = new GeneralSearch();
        this.checkerCycle = new CheckerCycleParents();
    }

    DFSIterative(CheckerCycle checkerCycle) {
        this.generalSearch = new GeneralSearch();
        this.checkerCycle = checkerCycle;
    }

    public Solution search(Problem problem) {
        Solution solution;
        for (int i = 1; i < 100; i++) {
            solution = generalSearch.generalSearch(problem, checkerCycle, new DFSLimQueuing(i));
            if (solution != null) return solution;
        }
        return null;
    }
}