import java.util.LinkedList;
import java.util.Queue;

public class MainClass {
    public static void main(String[] args) {

        GraphNode b = new GraphNode("B");
        GraphNode e = new GraphNode("E");
        GraphNode a = new GraphNode("A");
        GraphNode m = new GraphNode("M");
        GraphNode j = new GraphNode("J");

        b.addChild(a);
        a.addParent(e);
        e.addChild(a);
        a.addParent(b);
        a.addChild(m);
        m.addParent(a);
        a.addChild(j);
        j.addParent(a);
        a.setMarked(true);

        System.out.println(isDependent(m, j));
    }

    //

    /**
     * This is the algorithm I am using to scan the graph and see if there are
     * any dependencies between two nodes, I am using bfs to scan the graph.
     */

    public static boolean isDependent(GraphNode a, GraphNode b) {
        Queue<GraphNode> q = new LinkedList<GraphNode>();
        q.add(a);
        a.setVisited(true);
        // setting boolean flag, so I don't have an infinite while loop
        boolean flag = false;
        while (!q.isEmpty()) {
            GraphNode gn = q.remove();
            if (gn.getInfo().equals(b.getInfo())) {
                // we have found the right node return true
                return true;
            }
            if (!gn.isMarked() && !flag) {
                for (int i = 0; i < gn.getChildrens().size(); i++) {
                    if (!gn.getChildrens().get(i).isVisited()) {
                        gn.getChildrens().get(i).setVisited(true);
                        q.add(gn.getChildrens().get(i));
                    }
                }
            } else {
                flag = true;
                for (int i = 0; i < gn.getParents().size(); i++) {
                    if (!gn.getParents().get(i).isVisited()) {
                        gn.getParents().get(i).setVisited(true);
                        q.add(gn.getParents().get(i));
                    }
                }
            }

        }
        // the nodes are not connected return false
        return false;
    }
    /* what happens when compare M and J?? This needs to be fixed if A is Marked then
    M and J are dependant, and if A is not marked then they are independent
     */


}
