import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class variableElimination {
    private ArrayList<Factor> hidden;
    private ArrayList<Factor> marked_list;
    private ArrayList<Factor> factor_list;
    private String qs;
    private Factor Q;
    private int add = 0;
    private int mul = 0;

    // main constructor for VE class, will get a map of our graph and a line from scanner
    public variableElimination(HashMap<String, GraphNode> map, String line) {
        this.qs = line;
        this.Q = makeQfactor(map, this.qs);
        this.hidden = make_hiddenList(map, this.qs);
        this.marked_list = make_markedList(map, this.qs);
        this.factor_list = make_factor_list(map);

    }

    /**
     * this is the join function for two factors
     */
    public Factor joinAll() {
        Factor prev = null;
        for (int i = 0; i < hidden.size(); i++) {
            char ch = hidden.get(i).getTable().get(hidden.get(i).getTable().size() - 2).get(0).charAt(0);
            for (int k = 0; k < factor_list.size(); k++) {
                if (factor_list.get(k).getInfo().contains(ch + "")) {
                    Factor current = factor_list.get(k);
                    if (prev != null) {

                        prev = join(prev, current);
                    } else {
                        prev = current;
                    }

                }

            }

        }
        return prev;
    }

    private Factor join(Factor a, Factor b) {
        HashSet unique_headers = new HashSet();
        for (int i = 0; i < a.getHeaderList().size() - 1; i++) {
            unique_headers.add(a.getHeaderList().get(i));
        }
        for (int i = 0; i < b.getHeaderList().size() - 1; i++) {
            unique_headers.add(b.getHeaderList().get(i));
        }
        boolean[][] permutations = a.getCombination(unique_headers.size());
        for(int i=0;i<permutations.length;i++){
            for(int j=0;j<permutations[0].length;j++){

            }
        }
    }

    // this functions removes booleans that we don't need int factor
    public void removeBool(){
        String temp = this.qs;
        int x = temp.indexOf(')');
        int y = temp.indexOf('|')+1;
        temp = temp.substring(y, x);
        String []arr = temp.split(",");
        for (String s : arr){
            String b = s.charAt(2)+"";
            String v = s.charAt(0)+"";
            for(Factor f : this.factor_list){
                if(f.getInfo().contains(v)){
                    int row = f.getHeaderList().indexOf(v);
                    for(int i = 1; i<f.getTable().get(row).size(); i++){
                        if(!Objects.equals(f.getTable().get(row).get(i), b)){
                            for(int j = 0; j<f.getTable().size(); j++){
                                f.getTable().get(j).remove(i);

                            }
                            i--;
                        }
                    }
                }
            }
        }



    }

    // function to remove irrelevant factors
    public void removeIrel(HashMap<String, GraphNode> map) {
        //ArrayList<Factor> all, ArrayList<Factor> hidden, ArrayList<Factor> marked
        // create array list for marked nodes with just their symbols
        ArrayList<String> marked_nodes = new ArrayList<>();
        // create arraylist with hidden nodes just their symbols
        ArrayList<String> hidden_nodes = new ArrayList<>();
        // loop over hidden factor list
        for (Factor h : this.hidden) {
            // get the index of where the brackets end
            int x = h.getInfo().indexOf(')');
            // add the char of the hidden node
            hidden_nodes.add(h.getInfo().charAt(x-1) + "");
        }
        // loop over marked factor list
        for (Factor m : this.marked_list) {
            // get the index of where the brackets close
            int x = m.getInfo().indexOf(')');
            // add the char of the marked node
            marked_nodes.add(m.getInfo().charAt(x-1) + "");
        }
        // loop over hidden nodes
        for (String h : hidden_nodes) {
            // first if, check if our node is an ancestor of marked or query
            if (isRelev(h, map, marked_nodes)) {
                // if true check dependencies with query;
                if (MainClass.isDependent(map.get(h), map.get(this.Q.getInfo().charAt(this.Q.getInfo().length() - 2) + ""))
                 || MainClass.isDependent(map.get(this.Q.getInfo().charAt(this.Q.getInfo().length() - 2) + "" ), map.get(h))){
                    // if true do nothing
                }
                else{
                    // if false for first question remove all factors involved with irrelevant factor h
                    for(int i = 0; i<this.factor_list.size(); i++) {
                        for (int j = 0; j < this.factor_list.get(i).getTable().size()-1; j++) {
                            if (this.factor_list.get(i).getTable().get(j).get(0).equals(h)) {
                                this.factor_list.remove(this.factor_list.get(i));
                                if(i == 0)
                                    i = 0;
                                else
                                    i--;
                            }
                        }
                    }
                    for(int i = 0; i<this.hidden.size(); i++) {
                        for (int j = 0; j < this.hidden.get(i).getTable().size() - 1; j++) {
                            if (this.hidden.get(i).getTable().get(j).get(0).equals(h)) {
                                this.hidden.remove(this.hidden.get(i));
                                if(i == 0)
                                    i = 0;
                                else {
                                    i--;
                                }
                            }
                        }
                    }

                }

            }
            else{
                // if false for second question remove all factors involved with irrelevant factor h
                for(int i = 0; i<this.factor_list.size(); i++) {
                    for (int j = 0; j < this.factor_list.get(i).getTable().size()-1; j++) {
                        if (this.factor_list.get(i).getTable().get(j).get(0).equals(h)) {
                            this.factor_list.remove(this.factor_list.get(i));
                            if(i == 0)
                                i = 0;
                            else
                                i--;
                        }
                    }
                }
                for(int i = 0; i<this.hidden.size(); i++) {
                    for (int j = 0; j < this.hidden.get(i).getTable().size() - 1; j++) {
                        if (this.hidden.get(i).getTable().get(j).get(0).equals(h)) {
                            this.hidden.remove(this.hidden.get(i));
                            if(i == 0)
                                i = 0;
                            else {
                                i--;
                            }
                        }
                    }
                }
            }


            }

        // void function changed the factor list as it goes along
        }


    public boolean isRelev(String s, HashMap<String, GraphNode> map, ArrayList<String> marked) {

        // make arraylist for all ancestors
        ArrayList<GraphNode> p = new ArrayList<>();
        // make arraylist for all parents
//        ArrayList<GraphNode> p1 = new ArrayList<>();
        // loop through the marked strings
        for(String ms : marked){
            // get node of string
            GraphNode temp = map.get(ms);
             // check if node has parents
            if(!temp.getParents().isEmpty()) {
                // add all his parents to list
                p.addAll(temp.getParents());
//                p1.addAll(temp.getParents());
            }
        }
        // loop over parents of query and marked
        for(int i = 0; i<p.size(); i++) {
            // add their parents to list
            if (! p.get(i).getParents().isEmpty()) {
                p.addAll(p.get(i).getParents());
            }
            // the for loop ends when all of their ancestors are in a list
        }
            // loop through list
            for (GraphNode graphNode : p) {
                // if one of the parents is our node return true
                if (Objects.equals(s, graphNode.getInfo())){
                    return true;
                }
            }
        // if none of the parents are our nodes return false
        return false;
    }










    // this is a list of all the factors we have
    public ArrayList<Factor> make_factor_list(HashMap<String, GraphNode> map) {
        ArrayList<Factor> ans = new ArrayList<>(this.marked_list);
        ans.addAll(this.hidden);
//        ans.add(this.Q);
//        int counter = 1;
//        for (GraphNode gn : map.values()) {
//            Factor a = new Factor(gn);
//            a.setInfo(a.getInfo().substring(0, 1) + counter + a.getInfo().substring(1));
//            ans.add(a);
//            counter++;
//
//        }
        return ans;
    }

    // this is a list of the factors that are marked true or false, none of them are hidden
    public ArrayList<Factor> make_markedList(HashMap<String, GraphNode> map, String line) {
        ArrayList<Factor> ans = new ArrayList<>();
        ans.add(this.Q);
        String temp = line;
        int x = line.indexOf(')');
        int y = line.indexOf('|') + 1;
        temp = temp.substring(y, x);
        String[] sarr = temp.split(",");
        for (String s : sarr) {
            map.get("" + s.charAt(0)).setHidden(false);
            map.get("" + s.charAt(0)).setMarked(s.charAt(2) == 'T');
            Factor a = new Factor(map.get("" + s.charAt(0)));
            ans.add(a);
        }
        return ans;
    }

    // this is a list of all the hidden factors, we dont know if true or false
    public ArrayList<Factor> make_hiddenList(HashMap<String, GraphNode> map, String line) {
        ArrayList<Factor> ans = new ArrayList<>();
        String temp = line;
        int x = line.indexOf(')') + 2;
        temp = temp.substring(x);
        String[] sarr = temp.split("-");
        for (String s : sarr) {
            Factor a = new Factor(map.get(s));
            ans.add(a);
        }
        return ans;
    }

    // this is out Q factor, the one we get asked on
    public Factor makeQfactor(HashMap<String, GraphNode> map, String line) {
        String temp = line;
        int x = temp.indexOf('(') + 1;
        int y = temp.indexOf('|');
        temp = temp.substring(x, y);
        /////////////////////// need to remove set hidden and add aftte
//        map.get("" + temp.charAt(0)).setHidden(false);
//        map.get("" + temp.charAt(0)).setMarked(temp.charAt(2) == 'T');
        Factor a = new Factor(map.get("" + temp.charAt(0)));
        map.get("" + temp.charAt(0)).setHidden(false);
        return a;
    }


//    private String joinInfo(String info, String info1) {
//        int x = info.indexOf('(')+1;
//        int y = info.indexOf(')');
//        String temp = info.substring(x,y);
//
//
//
//    }


    public int getAdd() {
        return add;
    }

    public void setAdd(int add) {
        this.add = add;
    }

    public int getMul() {
        return mul;
    }

    public void setMul(int mul) {
        this.mul = mul;
    }

    public ArrayList<Factor> getFactor_list() {
        return factor_list;
    }

    public void setFactor_list(ArrayList<Factor> factor_list) {
        this.factor_list = factor_list;
    }

    public Factor getQ() {
        return Q;
    }

    public void setQ(Factor q) {
        Q = q;
    }

    public String getQs() {
        return qs;
    }

    public void setQs(String qs) {
        this.qs = qs;
    }

    public ArrayList<Factor> getHidden() {
        return hidden;
    }

    public void setHidden(ArrayList<Factor> hidden) {
        this.hidden = hidden;
    }

    public ArrayList<Factor> getMarked_list() {
        return marked_list;
    }

    public void setMarked_list(ArrayList<Factor> marked_list) {
        this.marked_list = marked_list;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String alarm_path = "C:\\Users\\arieh\\IdeaProjects\\algo_for_AI\\src\\Ex1\\alarm_net.xml";
        String big_net_path = "C:\\Users\\arieh\\IdeaProjects\\algo_for_AI\\src\\Ex1\\big_net.xml";
        String input_file = "C:\\Users\\arieh\\IdeaProjects\\algo_for_AI\\src\\Ex1\\input.txt";
        HashMap<String, GraphNode> map = (HashMap<String, GraphNode>) xmlReader.convertToGraph(alarm_path).clone();
        ArrayList<Factor> tests = new ArrayList<>();
        String a = "P(B=T|J=T,M=T) A-E";
        String b = "P(B=T|J=T,M=T) E-A";
        String c = "P(J=T|B=T) A-E-M";
        String d = "P(J=T|B=T) M-E-A";
        variableElimination test = new variableElimination(map, d);
                for(String i : map.keySet()){
            System.out.println(map.get(i));
        }


//        System.out.println(map);
        test.removeIrel(map);
        System.out.println(test.getFactor_list());
        System.out.println();
        test.removeBool();
        System.out.println(test.getFactor_list());
//        System.out.println();
//        System.out.println(test.getQ());

//        System.out.println(test.getQ().getInfo().indexOf(')'));
//        System.out.println();
//        System.out.println(test.getHidden());
//        System.out.println();
//        System.out.println(test.getMarked_list());
//        System.out.println();
//        System.out.println(test.getQ());
//        Factor m = new Factor(map.get("M"));
//        System.out.println(m);
    }
}
