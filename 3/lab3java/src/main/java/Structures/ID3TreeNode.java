package Structures;

import java.util.List;
import java.util.Map;

public class ID3TreeNode {

    /**Attribute for tree node**/
    private String attribute;
    /**Concrete value for leaf**/
    private String nodeValue;
    /**Node children, map maps attribute values to concrete nodes**/
    private Map<String, ID3TreeNode> nodes;
    /** List of remembered values for that attribute**/
    private String mostCommonValue;

    public ID3TreeNode() {
    }

    public ID3TreeNode(String attribute, String value, Map<String, ID3TreeNode> nodes) {
        if(attribute != null && value != null) throw new IllegalArgumentException("A three node can not be a knot and leaf at the same time!");
        this.attribute = attribute;
        this.nodes = nodes;
        this.nodeValue = value;
    }

    public ID3TreeNode(String attribute, String value, Map<String, ID3TreeNode> nodes, String mostCommonValue) {
        this(attribute, value, nodes);
        this.mostCommonValue = mostCommonValue;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Map<String, ID3TreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, ID3TreeNode> nodes) {
        this.nodes = nodes;
    }

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public String getMostCommonValue() {
        return mostCommonValue;
    }

    public void setMostCommonValue(String mostCommonValue) {
        this.mostCommonValue = mostCommonValue;
    }

    /**
     * Method will print out given tree.
     * @param tree
     */
    public static void printTree(ID3TreeNode tree) {
        System.out.print("[BRANCHES]:\n");
        printRecursive(tree, "", 0);
    }

    /**
     * Recursively prints nodes of this tree.
     * @param tree
     * @param base
     * @param level
     */
    private static void printRecursive(ID3TreeNode tree, String base, int level) {
        level++;
        if(tree.attribute == null){
            System.out.print(base + tree.nodeValue + "\n");
            return;
        }
        for(Map.Entry<String, ID3TreeNode> child : tree.nodes.entrySet()){
            String baseString = base + level + ":" + tree.attribute + "=" + child.getKey() + " ";
            printRecursive(child.getValue(), baseString , level);
        }
    }
}
