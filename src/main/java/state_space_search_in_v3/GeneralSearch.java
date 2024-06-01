package lab211;

import java.io.IOException;
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
            //Uncomment for step-by-step output
            /*Scanner scan=new Scanner(System.in);
            scan.nextLine();
            System.out.println("Fringer: ");
            for (Node node : nodes) System.out.println(node.state + ", cost: " + node.finalCost + "\n");*/

            Node tmpNode = nodes.pop();
            //Uncomment for step-by-step output
            /*System.out.println("Current node: ");
            System.out.println(tmpNode.state);*/

            if (problem.goalTest(tmpNode.state)) return new Solution(tmpNode, startNode);
            queuing.setQueue(nodes, expend(tmpNode, problem.operations(tmpNode.state)));
        }
    }

    public List<Node> expend(Node node, List<State> newStates) {
        List<Node> newNodes = new LinkedList<>();
        for (State state: newStates) {
            Node newNode = new Node(node, state, node.depth + 1, this.problem.goalState, distance);
            if (checkerCycle.check(newNode, node.parent)) newNodes.add(newNode);
            //Uncomment for step-by-step output
            //else System.out.println("Cycle prevented: \n" + newNode.state);
        }
        //Uncomment for step-by-step output
        /*System.out.println("Added nodes: ");
        for (Node addedNode : newNodes)
            System.out.println(addedNode.state + "\n");*/
        return newNodes;
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
    Comparator<Node> evalFunc;

    public BestQueuing(Comparator<Node> evalFunc) {
        this.evalFunc = evalFunc;
    }

    public void setQueue(LinkedList<Node> queue, List<Node> nodes) {
        queue.addAll(nodes);
        queue.sort(evalFunc);
    }
}

class GreedyComparator implements Comparator<Node> {
    @Override
    public int compare(Node node1, Node node2) {
        if (node1.costToGoal > node2.costToGoal) return 1;
        else if (node1.costToGoal < node2.costToGoal) return -1;
        return 0;
    }
}

class AStarComparator implements Comparator<Node> {
    @Override
    public int compare(Node node1, Node node2) {
        if (node1.finalCost > node2.finalCost) return 1;
        else if (node1.finalCost < node2.finalCost) return -1;
        return 0;
    }
}

class Node {
    Node parent;
    State state;
    int depth;
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

interface State {
    int distance(State goal, Distance distance);
}

class StatePuzzleEight implements State {
    byte[] positionNumber = new byte[9];
    byte zeroPosition;

    StatePuzzleEight (byte ... positionNumber) {
        for (byte i = 0; i < 9; i++) this.positionNumber[i] = positionNumber[i];
        for (byte i = 0; i < 9; i++) if (positionNumber[i] == 0) zeroPosition = i;
    }

    @Override
    public int distance(State goal, Distance distance){
        return distance.distance(this, goal);
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

interface Distance {
    int distance(State state, State goalState);
}

class WrongPositionDistancePuzzleEight implements Distance {
    public int distance(State state, State goalState) {
        StatePuzzleEight statePuzzleEight = (StatePuzzleEight) state;
        StatePuzzleEight goalPuzzleState = (StatePuzzleEight) goalState;
        int count = 0;
        for (byte i = 0; i < 9; i++) {
            if (goalPuzzleState.positionNumber[i] != statePuzzleEight.positionNumber[i]) count++;
        }
        return count;
    }
}

class ManhattanDistancePuzzleEight implements Distance{
    public int distance(State state, State goalState) {
        StatePuzzleEight statePuzzleEight = (StatePuzzleEight) state;
        StatePuzzleEight goalPuzzleState = (StatePuzzleEight) goalState;
        int count = 0;
        for (byte i = 0; i < 9; i++) {
            int columnDiff = Math.abs(goalPuzzleState.positionNumber[i]%3 - statePuzzleEight.positionNumber[i]%3);
            int lineDiff = Math.abs(goalPuzzleState.positionNumber[i]/3 - statePuzzleEight.positionNumber[i]/3);
            count += columnDiff + lineDiff;
        }
        return count;
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
        return "Solution found. \nDepth: " + solutionNodes.get(solutionNodes.size()-1).depth + "\n" +
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
        return generalSearch.generalSearch(problem, checkerCycle, queuing, null);
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
        return generalSearch.generalSearch(problem, checkerCycle, queuing, null);
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
        return generalSearch.generalSearch(problem, checkerCycle, queuing, null);
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
            solution = generalSearch.generalSearch(problem, checkerCycle, new DFSLimQueuing(i), null);
            if (solution != null) return solution;
        }
        return null;
    }
}

class GreedyS {
    GeneralSearch generalSearch;
    CheckerCycle checkerCycle;
    Queuing queuing;
    Distance distance;

    GreedyS(Distance distance) {
        this.generalSearch = new GeneralSearch();
        this.checkerCycle = new CheckerCycleParents();
        this.queuing = new BestQueuing(new GreedyComparator());
        this.distance = distance;
    }

    GreedyS(CheckerCycle checkerCycle, Distance distance) {
        this.generalSearch = new GeneralSearch();
        this.checkerCycle = checkerCycle;
        this.queuing = new BestQueuing(new GreedyComparator());
        this.distance = distance;
    }

    public Solution search(Problem problem) {
        return generalSearch.generalSearch(problem, checkerCycle, queuing, distance);
    }
}

class AStarS {
    GeneralSearch generalSearch;
    CheckerCycle checkerCycle;
    Queuing queuing;
    Distance distance;

    AStarS(Distance distance) {
        this.generalSearch = new GeneralSearch();
        this.checkerCycle = new CheckerCycleParents();
        this.queuing = new BestQueuing(new AStarComparator());
        this.distance = distance;
    }

    AStarS(CheckerCycle checkerCycle, Distance distance) {
        this.generalSearch = new GeneralSearch();
        this.checkerCycle = checkerCycle;
        this.queuing = new BestQueuing(new AStarComparator());
        this.distance = distance;
    }

    public Solution search(Problem problem) {
        return generalSearch.generalSearch(problem, checkerCycle, queuing, distance);
    }
}
