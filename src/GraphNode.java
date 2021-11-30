import java.util.ArrayList;
import java.util.Arrays;

/**
 * this class represents the nodes from a graph as an object
 */

public class GraphNode {
    private ArrayList<GraphNode> childrens;
    private ArrayList<GraphNode> parents;
    private String info;
    private boolean visited;
    private boolean marked;
    private ArrayList<String> boolValue;
    private ArrayList<Double> cptValue;
    private Factor factor;
    private boolean hidden;

    // GraphNode constructor
    public GraphNode(String info) {
        childrens = new ArrayList<>();
        parents = new ArrayList<>();
        cptValue = new ArrayList<>();
        boolValue = new ArrayList<>();
        this.info = info;
        marked = false;
//        factor=new Factor();
        hidden = true;
    }




    /**
     * the rest of the function are all getters and setters for the class variables
     * @return
     */

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }



    // returns the factor object that is in Node object
    public Factor getFactor() {
        return factor;
    }

    // creating a factor for each node
    public void createFactor(){
        // first initialising the array list for each column
        for(int i=0;i<parents.size()+2;i++){
            factor.getTable().add(new ArrayList<String>());
        }
        // if node doesn't have parents
        if(parents.size()==0){
            factor.getTable().get(0).add("T");
            factor.getTable().get(0).add("F");
            factor.getTable().get(1).add(cptValue.get(0).toString());
            factor.getTable().get(1).add(cptValue.get(1).toString());
        }else{
            // call the get combination function
            boolean[][] all=getCombination(parents.size()+1);
            for (boolean[] booleans : all) {
                int cols = all[0].length;
                for (int j = 0; j < all[0].length; j++) {
                    factor.getTable().get(j).add(booleans[j] ? "T" : "F");
                }
            }
        }
        // adding the cpt value to the last column in the arraylist
        for (Double aDouble : cptValue) {
            factor.getTable().get(parents.size() + 1).add(aDouble.toString());
        }
    }

    // function that sorts all the permutations over the True False combinations
    private  boolean[][] getCombination(int num) {
        // rows is the size of the different combinations and columns is the size of different variables
        boolean[][] result = new boolean[(int) Math.pow(2, num)][num];

        int permuteLen = (int) Math.pow(2, num);
        boolean[] b = new boolean[num];
        Arrays.fill(b, true);

        for (int j = 0; j < permuteLen; j++) {
            System.arraycopy(b, 0, result[j], 0, num);

            for (int i = num - 1; i >= 0; i--) {
                if (b[i]) {
                    b[i] = false;
                    break;
                } else
                    b[i] = true;
            }
        }
        return result;
    }

    // function to update the Factor of a node if is marked
    public void updateFactor() {
        if (!this.isHidden()) {
            if (this.isMarked()) {
                for (int i = 0; i < parents.size()+2; i++) {
                    for (int j = 0; j < cptValue.size(); j++) {
                        if (j % 2 == 1) {
                            factor.getTable().get(i).set(j, "remove");
                        }
                    }
                }

                for(int i = 0; i<parents.size()+2; i++){
                    int size = cptValue.size();
                    for(int j = 0; j<size; j++){
                        if(factor.getTable().get(i).get(j).equals("remove")){
                            factor.getTable().get(i).remove(j);
                            j -= 1;
                            size --;
                        }
                    }
                }
            }
            else{
                for (int i = 0; i < parents.size()+1; i++) {
                    for (int j = 0; j < cptValue.size(); j++) {
                        if (j % 2 == 0) {
                            factor.getTable().get(i).remove(j);
                        }
                    }
                }

            }
        }
    }

    public ArrayList<Double> getCptValue() {
        return cptValue;
    }

    public void setCptValue(ArrayList<Double> cptValue) {
        this.cptValue = cptValue;
    }

    public void addCptValue(double v){
        cptValue.add(v);
    }
    public void addBoolValue(String value){
        boolValue.add(value);
    }
    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }


    public void addParent(GraphNode node){
        parents.add(node);
    }
    public void addChild(GraphNode node){
        childrens.add(node);
    }
    public ArrayList<GraphNode> getChildrens() {
        return childrens;
    }

    public void setChildrens(ArrayList<GraphNode> childrens) {
        this.childrens = childrens;
    }

    public ArrayList<GraphNode> getParents() {
        return parents;
    }

    public void setParents(ArrayList<GraphNode> parents) {
        this.parents = parents;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public String toString(){
        ArrayList<String> parentList = new ArrayList<>();
        for(int i=0; i<this.parents.size(); i++)
            parentList.add(this.parents.get(i).info);
        ArrayList<String> childList = new ArrayList<>();
        for(int i=0; i<this.childrens.size(); i++)
            childList.add(this.childrens.get(i).info);
        return "Node: "+ this.info+", Evidence: "+this.marked+", HIDDEN: "+this.hidden+", OUTCOME: "+this.boolValue.toString()+", Parent: "+parentList.toString()+
                ", CHILD: "+childList.toString()+", CPT VALUE: "+cptValue.toString();
    }
}
