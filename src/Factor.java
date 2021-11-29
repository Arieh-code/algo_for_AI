import java.util.ArrayList;

public class Factor {
    private ArrayList<ArrayList<String>> table;

    public Factor() {
        table = new ArrayList<ArrayList<String>>();
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

    public void joinFactor(Factor a, Factor b){

    }
}
