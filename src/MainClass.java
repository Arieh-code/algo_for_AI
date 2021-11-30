import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) throws FileNotFoundException {
        String alarm_path = "C:\\Users\\arieh\\IdeaProjects\\algo_for_AI\\src\\Ex1\\alarm_net.xml";
        String big_net_path = "C:\\Users\\arieh\\IdeaProjects\\algo_for_AI\\src\\Ex1\\big_net.xml";
        String input_file = "C:\\Users\\arieh\\IdeaProjects\\algo_for_AI\\src\\Ex1\\input.txt";
        HashMap<String, GraphNode> map = (HashMap<String, GraphNode>) xmlReader.convertToGraph(alarm_path).clone();
//        fromInput(input_file, map);

        map.get("A").createFactor();
        map.get("E").createFactor();
        map.get("B").createFactor();
        map.get("J").createFactor();
        map.get("M").createFactor();
        map.get("J").getFactor().print();
        System.out.println();
        map.get("J").setHidden(false);
        map.get("J").setMarked(true);
        map.get("J").updateFactor();
        map.get("J").getFactor().print();

//        for(String i : map.keySet()){
//            System.out.println(map.get(i));
//        }


//        map.get("B").getFactor().print();
//        System.out.println();
//        map.get("E").getFactor().print();
//        System.out.println();
//        map.get("J").getFactor().print();
//        System.out.println();
//        map.get("M").getFactor().print();
//        System.out.println();
//        map.get("A").getFactor().print();
//        System.out.println(isDependent(map.get("B"), map.get("E")));
//        GraphNode b = new GraphNode("B");
//        GraphNode e = new GraphNode("E");
//        GraphNode a = new GraphNode("A");
//        GraphNode m = new GraphNode("M");
//        GraphNode j = new GraphNode("J");
//
//        b.addChild(a);
//        a.addParent(e);
//        e.addChild(a);
//        a.addParent(b);
//        a.addChild(m);
//        m.addParent(a);
//        a.addChild(j);
//        j.addParent(a);
//        a.setMarked(true);

       // printCombination(3);
    }

    //





    public static void fromInput(String file,HashMap<String, GraphNode> map ) throws FileNotFoundException {
        File text = new File(file);

        Scanner s = new Scanner(text);

        String line = s.nextLine();

        while(s.hasNextLine()){
            line = s.nextLine();
            if(line.contains("P")){
                break;
            }
            else{
                if(line.length()>4){
                    for(int i = 4; i<line.length(); i+=4){
                        if(line.charAt(i+2) == 'T')
                            map.get(""+line.charAt(i)).setMarked(true);
                        map.get(""+line.charAt(i)).setMarked(false);
                    }

                }
                System.out.println(isDependent(map.get(""+line.charAt(0)),map.get(""+line.charAt(2))));
            }

        }
        for(String i : map.keySet())
            System.out.println(map.get(i));
    }

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
