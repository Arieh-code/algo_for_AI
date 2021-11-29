import java.util.ArrayList;

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

    // GraphNode constructor
    public GraphNode(String info) {
        childrens = new ArrayList<>();
        parents = new ArrayList<>();
        cptValue = new ArrayList<>();
        boolValue = new ArrayList<>();
        this.info = info;
        marked = false;
        factor=new Factor();
    }



    /**
     * the rest of the function are all getters and setters for the class variables
     * @return
     */

    public Factor getFactor() {
        return factor;
    }


    public void createFactor(){
        for(int i=0;i<parents.size()+2;i++){
            factor.getTable().add(new ArrayList<String>());
        }
        if(parents.size()==0){
            factor.getTable().get(0).add("T");
            factor.getTable().get(0).add("F");
            factor.getTable().get(1).add(cptValue.get(0).toString());
            factor.getTable().get(1).add(cptValue.get(1).toString());
        }else{
            //int numOfOptions=(int)Math.pow(2, parents.size()+1);
            boolean[][] all=getCombination(parents.size()+1);
            for(int i=0;i<all.length;i++){
                int cols=all[0].length;
                for(int j=0;j<all[0].length;j++){
                    factor.getTable().get(j).add(all[i][j]?"T":"F");
                }
            }
        }
        for (Double aDouble : cptValue) {
            factor.getTable().get(parents.size() + 1).add(aDouble.toString());
        }
    }

    private  boolean[][] getCombination(int num) {
        boolean[][] result = new boolean[(int) Math.pow(2, num)][num];

        int permuteLen = (int) Math.pow(2, num);
        boolean b[] = new boolean[num];
        for (int i = 0; i < b.length; i++)
            b[i] = true;

        for (int j = 0; j < permuteLen; j++) {
            for (int i = 0; i < num; i++){
                result[j][i]=b[i];
            }
               // System.out.print("  " + b[i] + "  ");
          //  System.out.println(" ");

            for (int i = num - 1; i >= 0; i--) {
                if (b[i] == true) {
                    b[i] = false;
                    break;
                } else
                    b[i] = true;
            }
        }
        return result;
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
        return info;
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
        return "Node: "+ this.info+", Evidence: "+this.marked+", OUTCOME: "+this.boolValue.toString()+", Parent: "+parentList.toString()+
                ", CHILD: "+childList.toString()+", CPT VALUE: "+cptValue.toString();
    }
}
