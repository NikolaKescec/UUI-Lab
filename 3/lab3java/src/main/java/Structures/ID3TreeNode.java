package Structures;

import java.util.Map;

public class ID3TreeNode {

    private String attribute;
    private String nodeValue;
    private Map<String, ID3TreeNode> nodes;

    public ID3TreeNode() {
    }

    public ID3TreeNode(String attribute, String value, Map<String, ID3TreeNode> nodes) {
        this.attribute = attribute;
        this.nodes = nodes;
        this.nodeValue = value;
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

    public static String printTree(ID3TreeNode tree) {
        System.out.println("[BRANCHES]:");
        StringBuilder stringBuilder = new StringBuilder();
        printRecursive(tree, "", 0);
        return stringBuilder.toString();
    }

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

    @Override
    public String toString() {
        return ID3TreeNode.printTree(this);
    }
}
