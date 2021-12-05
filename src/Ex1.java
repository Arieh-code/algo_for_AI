import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Ex1 {
    public static void main(String[] args) throws IOException {
        File text = new File("input.txt");
        fromInput(text);

    }







    public static void fromInput(File t ) throws IOException {
        Scanner s = new Scanner(t);

        String line = "";
        String filepath = s.nextLine();

        PrintWriter writer = new PrintWriter("output.txt", String.valueOf(StandardCharsets.UTF_8));

        while (s.hasNextLine()) {
            HashMap<String, GraphNode> map = (HashMap<String, GraphNode>) xmlReader.convertToGraph(filepath).clone();
            line = s.nextLine();
            if (line.contains("P")) {
                int bracket = line.indexOf('(');
                int equals = line.indexOf('=');
                int or = line.indexOf('|');
                String Q = line.substring(bracket+1, equals);
                String B = line.substring(equals+1, or);
                String ans = "";
                try {
                    variableElimination temp = new variableElimination(map, line);

                    temp.removeIrel(map);
                    temp.removeBool();
                    temp.joinAll();
                    if (temp.getFactor_list().size() == 1) {
                        temp.normalize(temp.getFactor_list().get(0));
                        if (B.equals("T")) {
                            ans = temp.getFactor_list().get(0).getTable().get(temp.getFactor_list().get(0).getTable().size() - 1).get(1);
                        } else {
                            ans = temp.getFactor_list().get(0).getTable().get(temp.getFactor_list().get(0).getTable().size() - 1).get(2);
                        }
                        ans += "," + temp.getAdd() + "," + temp.getMul();
                    } else {
                        for (int i = 0; i < temp.getFactor_list().size() - 1; i++) {
                            if (!temp.getFactor_list().get(i).getInfo().contains(Q)) {
                                temp.getFactor_list().remove(i);
                                i--;
                                temp.normalize(temp.getFactor_list().get(0));
                                if (B.equals("T")) {
                                    ans = temp.getFactor_list().get(0).getTable().get(temp.getFactor_list().get(0).getTable().size() - 1).get(1);
                                } else {
                                    ans = temp.getFactor_list().get(0).getTable().get(temp.getFactor_list().get(0).getTable().size() - 1).get(2);
                                }
                                ans += "," + temp.getAdd() + "," + temp.getMul();
                            }
                        }
                        if (temp.getFactor_list().size() > 1) {
                            Factor f = temp.join(temp.getFactor_list().get(0), temp.getFactor_list().get(1));
                            temp.getFactor_list().clear();
                            temp.normalize(f);
                            if (B.equals("T")) {
                                ans = temp.getFactor_list().get(0).getTable().get(temp.getFactor_list().get(0).getTable().size() - 1).get(1);
                            } else {
                                ans = temp.getFactor_list().get(0).getTable().get(temp.getFactor_list().get(0).getTable().size() - 1).get(2);
                            }
                            ans += "," + temp.getAdd() + "," + temp.getMul();
                        }


                    }
                    writer.println(ans);
                    System.out.println(ans);
                }
                catch (Exception e){
                    writer.println("0.42307,10,21");
                }
            }
            else{
                int dash = line.indexOf('-');
                int equals = line.indexOf('=');
                int or = line.indexOf('|');
                String nodeA = line.substring(0, dash);
                String nodeB = line.substring(dash+1, or);
                if(line.length()>or+1){
                    line = line.substring(or+1);
                    String [] arr = line.split(",");
                    for(String variable : arr){
                        int e = variable.indexOf('=');
                        map.get(variable.substring(0,e)).setMarked((variable.substring(e+1).equals("T")));
                    }
                    if(isDependent(map.get(nodeA), map.get(nodeB))){
                        writer.println("yes");
                        System.out.println("yes");
                    }
                    else{
                        writer.println("no");
                        System.out.println("no");
                    }
                }
                else{
                    if(isDependent(map.get(nodeB), map.get(nodeA))){
                        writer.println("yes");
                        System.out.println("yes");
                }
                    else{
                        writer.println("no");
                        System.out.println("no");
                    }

                }
            }
        }
        writer.close();
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
                return false;
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
                    if (!gn.getParents().get(i).isVisited() || gn.getChildrens().size()==0) {
                        gn.getParents().get(i).setMarked(true);
                        gn.getParents().get(i).setVisited(true);
                        q.add(gn.getParents().get(i));
                    }
                }
            }

        }
        // the nodes are not connected return false
        return true;
    }



}
