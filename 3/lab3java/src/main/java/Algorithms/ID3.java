package Algorithms;

import Structures.DataEntry;
import Structures.ID3TreeNode;
import Structures.SetOfData;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ID3 {

    private Integer treeDepth;
    private ID3TreeNode trainedTree;

    public ID3(Integer treeDepth){
        this.treeDepth = treeDepth;
    }

    public void fit(SetOfData setOfData){
        trainedTree = id3(setOfData.getEntries(),
                                setOfData.getEntries(),
                                new TreeSet<>(SetOfData.getAttributes().keySet()),
                                SetOfData.getGoalName());
    }

    private ID3TreeNode id3(List<DataEntry> inputData, List<DataEntry> parentData, Set<String> attributes, String y){
        String mostCommon;
        if(inputData.isEmpty()){
            mostCommon = argmaxMostCommon(parentData);
            return new ID3TreeNode(null, mostCommon, null);
        }
        mostCommon = argmaxMostCommon(inputData);
        if(attributes.isEmpty() || inputData.equals(inputData.stream().filter(dataEntry -> dataEntry.getValue(SetOfData.getGoalName()).equals(mostCommon)).collect(Collectors.toList()))){
            return new ID3TreeNode(null, mostCommon, null);
        }
        String mostDiscriminative = argmaxMostDiscriminative(inputData, attributes);

        Map<String, ID3TreeNode> nodes = new HashMap<>();
        for(String value : SetOfData.getAttributes().get(mostDiscriminative)){
            List<DataEntry> splicedEntries = inputData.stream().filter(dataEntry -> dataEntry.getValue(mostDiscriminative).equals(value)).collect(Collectors.toList());
            ID3TreeNode t = id3(splicedEntries, inputData, attributes.stream().filter(attribute -> !attribute.equals(mostDiscriminative)).collect(Collectors.toCollection(TreeSet::new)),y);
            nodes.put(value, t);
        }
        return new ID3TreeNode(mostDiscriminative, null, nodes);
    }

    private String argmaxMostDiscriminative(List<DataEntry> inputSet, Set<String> attributes) {
        String discriminatedAttribute = null;
        double ig = 0;
        for(String attribute : attributes) {
            double startingIG = calculateEntropy(inputSet, SetOfData.getGoalName(), SetOfData.getGoals());
            for(String value : SetOfData.getAttributes().get(attribute)){
                List<DataEntry> filteredEntries = inputSet.stream().filter(dataEntry -> dataEntry.getValue(attribute).equals(value)).collect(Collectors.toList());
                startingIG -=
                        ((double) filteredEntries.size())/inputSet.size()
                                *calculateEntropy(filteredEntries, SetOfData.getGoalName(), SetOfData.getGoals());
            }
            if(startingIG > ig){
                ig = startingIG;
                discriminatedAttribute = attribute;
            }
            System.out.print("IG(" + attribute + ")=" + startingIG + " ");
        }
        System.out.println();
        return discriminatedAttribute;
    }

    private double calculateEntropy(List<DataEntry> dataEntries, String attribute, Set<String> values) {
        if(dataEntries.isEmpty()) return 0;
        double entropy = 0;
        for(String value : values){
            double probability = ((double)countEntries(dataEntries, attribute, value))/dataEntries.size();
            entropy -= probability*log2(probability);
        }
        return entropy;
    }

    private String argmaxMostCommon(List<DataEntry> inputSet) {
        long count = 0;
        String mostCommon = null;
        for(String value : SetOfData.getGoals()){
            long newCount =  countEntries(inputSet, SetOfData.getGoalName(), value);
            if(count < newCount){
                count = newCount;
                mostCommon = value;
            }
        }
        return mostCommon;
    }

    private long countEntries(List<DataEntry> dataEntryList, String searchedAttribute, String value){
        return dataEntryList.stream().filter(dataEntry -> dataEntry.getValue(searchedAttribute).equals(value)).count();
    }

    private double log2(double n){
        if(n == 0) return 0;
        return (Math.log(n) / Math.log(2));
    }

    public void predict(SetOfData setOfData){

    }

    public Integer getTreeDepth() {
        return treeDepth;
    }

    public void setTreeDepth(Integer treeDepth) {
        this.treeDepth = treeDepth;
    }

    public ID3TreeNode getTrainedTree() {
        return trainedTree;
    }

    public void setTrainedTree(ID3TreeNode trainedTree) {
        this.trainedTree = trainedTree;
    }
}
