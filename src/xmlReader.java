import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class xmlReader {
    public static HashMap<String, GraphNode> convertToGraph(String file) throws FileNotFoundException {
        // creating hashmap for my nodes
        HashMap<String, GraphNode> graph = new HashMap<>();
        // formatting my file to text
        File text = new File(file);
        //using scanner to scan the text
        Scanner scnr = new Scanner(text);
        //getting the first line
        String xml_line = scnr.nextLine();
        /**
         * while loop until we reach the end of the text file
         */
        while(scnr.hasNextLine()){
            // if line contains VARIABLE then we are expecting to create a node
            if(xml_line.contains("VARIABLE")){
                // going to the next line
                xml_line = scnr.nextLine();
                // getting the node name
                String temp = removeLine(xml_line, "NAME");
                // creating the node, info == temp
                GraphNode curr = new GraphNode(temp);
                // adding node to hashmap
                graph.put(temp, curr);
                // adding the boolean values until we reach the word Variable again
                xml_line = scnr.nextLine();
                while(!xml_line.contains("/VARIABLE")){
                    temp = removeLine(xml_line, "OUTCOME");
                    curr.addBoolValue(temp);
                    xml_line = scnr.nextLine();
                }
            }
            // taking care of the DEFINITION now
            if(xml_line.contains("DEFINITION")){
                // going down a line
                xml_line = scnr.nextLine();
                // getting the name of the now
                String name = removeLine(xml_line, "FOR");
                // shallow copy of the node
                GraphNode temp = graph.get(name);
                // go down a line
                xml_line = scnr.nextLine();
                // check if line is GIVEN
                if(xml_line.contains("GIVEN")) {
                    // going until I find TABLE
                    while (!xml_line.contains("TABLE")) {
                        // getting the name of the node parent
                        name = removeLine(xml_line, "GIVEN");
                        // adding to the chile parent node
                        temp.addParent(graph.get(name));
                        // adding to the parent node child
                        graph.get(name).addChild(temp);
                        // going down a line
                        xml_line = scnr.nextLine();
                    }
                }
                // calling the addcpt function
                addCptTable(temp, xml_line);
                // going down a line
                xml_line = scnr.nextLine();
            }
            // going down a line
            xml_line = scnr.nextLine();
        }
        return graph;
    }

    /**
     * function to add cpt value to arraylist in node
     */
    private static void addCptTable(GraphNode temp, String xml_line) {
        xml_line = xml_line.replace("\t", "");
        xml_line = xml_line.replace("<TABLE>", "");
        xml_line = xml_line.replace("</TABLE>", "");
        String [] sarr = xml_line.split(" ");
        for(String p : sarr){
            temp.addCptValue(Double.parseDouble(p));
        }
    }

    /**
     * function to remove all things that I don't need in a line
     */
    public static String removeLine(String line, String header){
        line = line.replace("\t", "");
        line = line.replace("<"+header+">", "");
        line = line.replace("</"+header+">", "");
        return line;
    }
}
