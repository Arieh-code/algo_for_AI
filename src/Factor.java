import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Factor {
    private ArrayList<ArrayList<String>> table;
    private String info;
    private ArrayList<String> headerList;
    private int ASCII_val;
    private String factor_num;


    public Factor(GraphNode gn) {

        this.info = makeFactor_header(gn);
        this.ASCII_val = asciiValue(gn);
        this.headerList = make_headerList(gn, this.info);
        this.table = new ArrayList<ArrayList<String>>();
        createFactor(gn);
    }

    public Factor(String info, int sum){
        this.info = makeNewHeader(info);
        this.ASCII_val = sum;
        this.headerList = makenew_headerList(this.info);
        this.table = new ArrayList<ArrayList<String>>();
        createNewFactor(info);
    }

    public void createNewFactor(String info){
        for(int i=0;i<info.length()+1;i++){
            this.table.add(new ArrayList<String>());
        }
        boolean[][] all=getCombination(info.length());
        for (boolean[] booleans : all) {
            int cols = all[0].length;
            for (int j = 0; j < all[0].length; j++) {
                this.table.get(j).add(booleans[j] ? "T" : "F");
            }
        }
        for(int i = 0; i<Math.pow(2,info.length()); i++){
            this.table.get(this.table.size() - 1).add("-");
        }
        // adding the header to the graph
        for(int i = 0; i<this.headerList.size(); i++){
            this.table.get(i).add(0,this.headerList.get(i));
        }

    }

    public static String makeNewHeader(String info){
        StringBuilder title = new StringBuilder("f(");
        for(int i = 0; i<info.length();i++){
            title.append(info.charAt(i)).append(",");
        }
        title = new StringBuilder(title.substring(0, title.length()-1));
        title.append(")");
        return title.toString();
    }

    public static ArrayList<String> makenew_headerList(String info){
        ArrayList<String> ans = new ArrayList<>();
        info = info.replace("f(", "");
        info = info.replace(")", "");
        String []sarr = info.split(",");
        Collections.addAll(ans, sarr);
        ans.add("P(..)");
        return ans;
    }



    // generating the ascii value for each factor
    public int asciiValue(GraphNode gn) {
        int sum = 0;
        String s = gn.getInfo();
        sum += s.charAt(0);
        if (gn.getParents().size() > 1) {
            for (int i = 0; i < gn.getParents().size(); i++) {
                s = gn.getParents().get(i).getInfo();
                sum += s.charAt(0);
            }
        }
        return sum;
    }

    /**
     * generate the title for each factor
     */
    public String makeFactor_header(GraphNode gn){
        if(gn.getParents().isEmpty()){
            return "f("+gn.getInfo()+")";
        }
        String title = "f(";
        for(GraphNode p : gn.getParents()){
            title += p.getInfo()+",";
        }
        title = title.substring(0, title.length());
        title += gn.getInfo()+")";
        return title;
    }

    /** this function will generate a header that will go on top of each factor
     */
    public static ArrayList<String> make_headerList(GraphNode gn, String info){
        ArrayList<String> ans = new ArrayList<>();
        info = info.replace("f(", "");
        info = info.replace(")", "");
        String []sarr = info.split(",");
        Collections.addAll(ans, sarr);
        String temp = "P("+gn.getInfo();
        if(!gn.getParents().isEmpty()) {
            temp += "|";
            for (GraphNode p : gn.getParents())
                temp += p.getInfo()+",";
            temp = temp.substring(0, temp.length()-1);
        }
        temp  += ")";
        ans.add(temp);
        return ans;
    }

    public void createFactor(GraphNode gn){
        // first initialising the array list for each column
        for(int i=0;i<gn.getParents().size()+2;i++){
            this.table.add(new ArrayList<String>());
        }
        // if node doesn't have parents
        if(gn.getParents().size()==0){
            this.table.get(0).add("T");
            this.table.get(0).add("F");
            this.table.get(1).add(gn.getCptValue().get(0).toString());
            this.table.get(1).add(gn.getCptValue().get(1).toString());
        }else{
            // call the get combination function
            boolean[][] all=getCombination(gn.getParents().size()+1);
            for (boolean[] booleans : all) {
                int cols = all[0].length;
                for (int j = 0; j < all[0].length; j++) {
                    this.table.get(j).add(booleans[j] ? "T" : "F");
                }
            }
        }
        // adding the cpt value to the last column in the arraylist
        for (Double aDouble : gn.getCptValue()) {
            this.table.get(gn.getParents().size() + 1).add(aDouble.toString());
        }

        // checking if node had true or false variables
        if (!gn.isHidden()) {
            if (gn.isMarked()) {
                for (int i = 0; i < gn.getParents().size()+2; i++) {
                    for (int j = 0; j < gn.getCptValue().size(); j++) {
                        if (j % 2 == 1) {
                            this.table.get(i).set(j, "remove");
                        }
                    }
                }

                for(int i = 0; i<gn.getParents().size()+2; i++){
                    int size = gn.getCptValue().size();
                    for(int j = 0; j<size; j++){
                        if(this.table.get(i).get(j).equals("remove")){
                            this.table.get(i).remove(j);
                            j -= 1;
                            size --;
                        }
                    }
                }
            }
            else{
                for (int i = 0; i < gn.getParents().size()+2; i++) {
                    for (int j = 0; j < gn.getCptValue().size(); j++) {
                        if (j % 2 == 0) {
                            this.table.get(i).set(j, "remove");
                        }
                    }
                }

                for(int i = 0; i<gn.getParents().size()+2; i++){
                    int size = gn.getCptValue().size();
                    for(int j = 0; j<size; j++){
                        if(this.table.get(i).get(j).equals("remove")){
                            this.table.get(i).remove(j);
                            j -= 1;
                            size --;
                        }
                    }
                }
            }

            }
        // adding the header to the graph
        for(int i = 0; i<this.headerList.size(); i++){
            this.table.get(i).add(0,this.headerList.get(i));
        }
        }







    // function that sorts all the permutations over the True False combinations
    public  boolean[][] getCombination(int num) {
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

    public String getFactor_num() {
        return factor_num;
    }

    public void setFactor_num(String factor_num) {
        this.factor_num = factor_num;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ArrayList<String> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(ArrayList<String> headerList) {
        this.headerList = headerList;
    }

    public int getASCII_val() {
        return ASCII_val;
    }

    public void setASCII_val(int ASCII_val) {
        this.ASCII_val = ASCII_val;
    }

    public ArrayList<ArrayList<String>> getTable() {
        return table;
    }

    public void setTable(ArrayList<ArrayList<String>> table) {
        this.table = table;
    }

    public void addInfoAtCol(String info, int col) {
        table.get(col).add(info);
    }

    public void setCell(String info, int row, int col) {
        table.get(col).set(row, info);
    }

    public void print() {
        for (int i = 0; i < table.get(0).size(); i++) {//rows
            for (int j = 0; j < table.size(); j++) {//cols
                if (table.size() >= (j + 1) && table.get(j).size() >= (i + 1))
                    System.out.print(table.get(j).get(i) + "  |  ");
            }
            System.out.println();
        }
    }

    public String toString() {
        StringBuilder ans = new StringBuilder(""+this.info+"\n");
        ans.append("ASCII SUM: ").append(this.ASCII_val).append("\nFactor:\n| ");
        for(int i=0; i<this.table.get(0).size(); i++){
            for (ArrayList<String> strings : table)
                ans.append(strings.get(i)).append(" | ");
            ans.append("\n| ");
        }
        return ans.substring(0, ans.length()-2);
    }



    public static void main(String[] args) throws FileNotFoundException {
        String alarm_path = "C:\\Users\\arieh\\IdeaProjects\\algo_for_AI\\src\\Ex1\\alarm_net.xml";
        String big_net_path = "C:\\Users\\arieh\\IdeaProjects\\algo_for_AI\\src\\Ex1\\big_net.xml";
        String input_file = "C:\\Users\\arieh\\IdeaProjects\\algo_for_AI\\src\\Ex1\\input.txt";
        HashMap<String, GraphNode> map = (HashMap<String, GraphNode>) xmlReader.convertToGraph(alarm_path).clone();
        ArrayList<Factor> tests = new ArrayList<>();

//        int counter = 1;
//        for (GraphNode gn : map.values()){
//            Factor a = new Factor(gn);
//            a.setInfo(a.getInfo().substring(0,1)+counter+a.getInfo().substring(1));
//            tests.add(a);
//            counter ++;
//        }

//        System.out.println(tests);
        Factor t = new Factor("ABCD", 100);
        System.out.println(t);
//        String test = makeNewHeader("ABCD");
//        ArrayList<String> s = makenew_headerList("A,B,C,D");
//        System.out.println(s);
//        map.get("J").setMarked(true);
//        map.get("J").setHidden(false);

//        System.out.println(map.get("M"));
//        Factor a = new Factor(map.get("M"));
//        System.out.println();
//        System.out.println(a);
//        map.get("M").setMarked(false);
//        map.get("M").setHidden(false);
//        System.out.println();
//        System.out.println(map.get("M"));
//        Factor b = new Factor(map.get("M"));
//        System.out.println(b);

//        char a = map.get("A").getInfo().getChars(0,1);
//        System.out.println(tests);

    }

}
