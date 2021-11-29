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
    private ArrayList<GraphNode> boolValue;
    private ArrayList<GraphNode> cptValue;

    // GraphNode constructor
    public GraphNode(String info) {
        childrens = new ArrayList<>();
        parents = new ArrayList<>();
        cptValue = new ArrayList<>();
        boolValue = new ArrayList<>();
        this.info = info;
        marked = false;
    }

    /**
     * the rest of the function are all getters and setters for the class variables
     * @return
     */



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
