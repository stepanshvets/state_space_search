package lab22.uninformed;

import lab22.search.*;

import java.util.LinkedList;
import java.util.List;

public class DFSQueuing implements Queuing {
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
