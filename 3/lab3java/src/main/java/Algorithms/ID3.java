package Algorithms;

import Structures.DataEntry;
import Structures.ID3TreeNode;
import Structures.SetOfData;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ID3 {

    /** Set tree depth **/
    private Integer treeDepth;

    /** Trained tree after training **/
    private ID3TreeNode trainedTree;

    public ID3(Integer treeDepth){
        this.treeDepth = treeDepth;
    }

    /**
     * Method will train this algorithm and produce a ID3Tree.
     * @param setOfData
     */
    public void fit(SetOfData setOfData){
        //Get ROWS, ATTRIBUTES, GOAL NAME AND DEPTH
        trainedTree = id3(setOfData.getEntries(),
                                setOfData.getEntries(),
                                new TreeSet<>(SetOfData.getAttributes().keySet()),
                                SetOfData.getGoalName(), 0);
    }

    /**
     * Training algorithm for this id3 machine learning algorithm.
     * @param inputData list of data entry rows
     * @param parentData parent list of data entry rows
     * @param attributes sorted set of attributes
     * @param y name of goal attribute
     * @param depth depth
     * @return trained tree
     */
    private ID3TreeNode id3(List<DataEntry> inputData, List<DataEntry> parentData, Set<String> attributes, String y, int depth){
        //IF DEPTH LIMIT IS REACHED RETURN A LEAF THAT IS MADE BY FINDING MOST COMMON GOAL VALUE FROM INPUT DATA
        //IF INPUT DATA IS EMPTY SEARCH FOR MOST COMMON GOAL VALUE FROM PARENT DATA
        if(treeDepth != null && treeDepth == depth){
            return new ID3TreeNode(null, inputData.isEmpty() ? argmaxMostCommon(parentData) : argmaxMostCommon(inputData), null);
        }
        ++depth;

        //DECLARE MOST COMMON AND CHECK IF INPUT DATA IS EMPTY, IF IT IS EMPTY RETURN MOST COMMON VALUE FROM PARENT DATA
        String mostCommon;
        if(inputData.isEmpty()){
            mostCommon = argmaxMostCommon(parentData);
            return new ID3TreeNode(null, mostCommon, null);
        }

        //FIND MOST COMMON GOAL VALUE FROM INPUT DATA AND THEN CHECK IF ATTRIBUTES LIST IS EMPTY OR IF FILTERED INPUT DATA BY MOST COMMON GOAL VALUE IS SAME AS GIVEN INPUT DATA
        //IN CASE OF TRUE, RETURN LEAF WITH FOUND MOST COMMON AS VALUE
        mostCommon = argmaxMostCommon(inputData);
        if(attributes.isEmpty() || inputData.equals(inputData.stream().filter(dataEntry -> dataEntry.getValue(SetOfData.getGoalName()).equals(mostCommon)).collect(Collectors.toList()))){
            return new ID3TreeNode(null, mostCommon, null);
        }

        //FIND MOST DISCRIMINATIVE ATTRIBUTE
        String mostDiscriminative = argmaxMostDiscriminative(inputData, attributes);

        //ITERATE TROUGH DISTINCT VALUES OF MOST DISCRIMINATIVE VALUE
        //FILTER INPUT DATA BY EACH DISTINCT VALUE AND CALL RECURSIVELY THIS METHOD AGAIN WITH SPLICED ENTRY AS A NEW INPUT DATA LIST
        //AND OLD INPUT DATA LIST AS PARENT
        Map<String, ID3TreeNode> nodes = new HashMap<>();
        for(String value : SetOfData.getAttributes().get(mostDiscriminative)){
            List<DataEntry> splicedEntries = inputData.stream().filter(dataEntry -> dataEntry.getValue(mostDiscriminative).equals(value)).collect(Collectors.toList());
            ID3TreeNode t = id3(splicedEntries, inputData, attributes.stream().filter(attribute -> !attribute.equals(mostDiscriminative)).collect(Collectors.toCollection(TreeSet::new)),y, depth);
            nodes.put(value, t);
        }
        return new ID3TreeNode(mostDiscriminative, null, nodes, mostCommon);
    }

    /**
     * Method will find most discriminative attribute by calculating the highest information gain for every attribute
     * Attribute with highest information gain is returned.
     * In short, search for attribute that leads to most decisive (entropy = 0) segregated sets.
     * @param inputSet
     * @param attributes
     * @return most discriminative attribute
     */
    private String argmaxMostDiscriminative(List<DataEntry> inputSet, Set<String> attributes) {
        String discriminatedAttribute = null;
        double ig = 0;
        for(String attribute : attributes) {
            //IN CASE THAT CALCULATED INFORMATION GAIN IS INDEED 0 (IT MEANS THAT SEPARATED SETS OF DATA BY VALUE ARE OF MAXIMUM ENTROPY - chance to pick out element is uniform 1/n)
            //IN CASE THAT CALCULATED INFORMATION GAIN IS INDEED 1 (IT MEANS THAT SEPARATED SETS OF DATA BY VALUE ARE OF MINIMUM ENTROPY - chance to pick out element if constant 1)
            if(discriminatedAttribute == null) discriminatedAttribute = attribute;
            //STARTING INFORMATION GAIN IS ENTROPY OF WHOLE SET OF DATA
            double startingIG = calculateEntropy(inputSet, SetOfData.getGoalName(), SetOfData.getTrainingGoals());
            //FOR EACH VALUE OF CURRENT ATTRIBUTE SEGREGATE INPUT SET BY VALUE AND CALCULATE ITS INFORMATION GAIN
            //IF IT IS LARGER THAN THE LARGEST YET, SET IT AS LARGEST YET AND REMEMBER ATTRIBUTE AS MOST DISCRIMINATED
            for(String value : SetOfData.getAttributes().get(attribute)){
                List<DataEntry> filteredEntries = inputSet.stream().filter(dataEntry -> dataEntry.getValue(attribute).equals(value)).collect(Collectors.toList());
                startingIG -=
                        ((double) filteredEntries.size())/inputSet.size()
                                *calculateEntropy(filteredEntries, SetOfData.getGoalName(), SetOfData.getTrainingGoals());
            }
            if(startingIG > ig){
                ig = startingIG;
                discriminatedAttribute = attribute;
            }
            System.out.print("IG(" + attribute + ")=" + String.format("%.4g", startingIG) + " ");
        }
        System.out.print("\n");
        return discriminatedAttribute;
    }

    /**
     * Method calculates entropy of given data entries.
     * @param dataEntries
     * @param attribute
     * @param values
     * @return calculated entropy
     */
    private double calculateEntropy(List<DataEntry> dataEntries, String attribute, Set<String> values) {
        //IF ENTRIES ARE EMPTY, IT MEANS THAT IS ENTROPY IS 0
        if(dataEntries.isEmpty()) return 0;
        double entropy = 0;
        //CALCULATING ENTROPY BY FORMULA
        for(String value : values){
            double probability = ((double)countEntries(dataEntries, attribute, value))/dataEntries.size();
            entropy -= probability*log2(probability);
        }
        return entropy;
    }

    /**
     * Method finds most common common goal value from given input set
     * @param inputSet
     * @return most common goal value
     */
    private String argmaxMostCommon(List<DataEntry> inputSet) {
        long count = 0;
        String mostCommon = null;
        for(String value : SetOfData.getTrainingGoals()){
            long newCount =  countEntries(inputSet, SetOfData.getGoalName(), value);
            if(count < newCount){
                count = newCount;
                mostCommon = value;
            }
        }
        return mostCommon;
    }

    /**
     * Method counts entries by searched attribute and given value
     * @param dataEntryList
     * @param searchedAttribute
     * @param value
     * @return count
     */
    private long countEntries(List<DataEntry> dataEntryList, String searchedAttribute, String value){
        return dataEntryList.stream().filter(dataEntry -> dataEntry.getValue(searchedAttribute).equals(value)).count();
    }

    private double log2(double n){
        if(n == 0) return 0;
        return (Math.log(n) / Math.log(2));
    }

    /**
     * Method will predict and write out results of predictions for each given testing row.
     * Also will calculate confusion matrix and accuracy.
     * @param setOfData
     */
    public void predict(SetOfData setOfData){
        if(trainedTree == null) throw new RuntimeException("Algorithm is not trained yet!");

        //PREPARE CONFUSION MATRIX, PREPARE GOAL ATTRIBUTE INDEXES FOR EASIER MANIPULATION OVER MATRIX
        int correct = 0;
        Set<String> rowsAndColumns = new TreeSet<>(SetOfData.getTestingGoals());
        rowsAndColumns.addAll(SetOfData.getTrainingGoals());
        List<String> indexes = new ArrayList<>(rowsAndColumns);
        int[][] confusionMatrix = new int[indexes.size()][indexes.size()];

        //FIND PREDICTIONS FOR EACH ROW OF GIVEN TEST DATA SET
        System.out.print("[PREDICTIONS]: ");
        int i;
        for(i = 0; i < setOfData.getEntries().size(); i++){
            DataEntry dataEntry = setOfData.getEntries().get(i);
            //CALCULATE PREDICTION FOR DATA ENTRY
            String prediction = prediction(trainedTree, dataEntry);
            //FILL CONFUSION MATRIX
            confusionMatrix[indexes.indexOf(dataEntry.getValue(SetOfData.getGoalName()))][indexes.indexOf(prediction)]++;
            if(prediction.equals(dataEntry.getValue(SetOfData.getGoalName()))) correct++;
            System.out.print(prediction + (i == setOfData.getEntries().size()-1 ? "" : " "));
        }

        //CALCULATE ACCURACY AND WRITE OUT CALCULATED DATA AND MATRIX
        double accuracy = ((double)correct)/setOfData.getEntries().size();
        System.out.print("\n[ACCURACY]: " + String.format("%.5f", accuracy) + "\n");
        System.out.print("[CONFUSION_MATRIX]:\n");
        List<Integer> forbiddenIndex = new ArrayList<>();
        //CHECK IF COLUMN AND ROW IS ZERO, DO NOT WRITE THOSE INPUTS
        for(i = 0; i < indexes.size(); i++){
            if(zeroRow(confusionMatrix, i, indexes.size()))
                if(zeroColumn(confusionMatrix, i, indexes.size()))
                    forbiddenIndex.add(i);
        }

        //WRITE OUT MATRIX CORRECTLY, CHECK FOR FORBIDDEN INDEXES
        for(i = 0; i < indexes.size(); i++){
            if(forbiddenIndex.contains(i)) continue;
            for(int j = 0; j < indexes.size(); j++){
                if(forbiddenIndex.contains(j)) continue;
                System.out.print(confusionMatrix[i][j] + (j == indexes.size()-1 ? "\n" : " "));
            }
        }
    }

    private boolean zeroRow(int[][] confusionMatrix, int i, int size) {
        for(int j = 0; j < size; j++ )
            if(confusionMatrix[i][j] != 0)
                return false;
        return true;
    }

    private boolean zeroColumn(int[][] confusionMatrix, int i, int size) {
        for(int j = 0; j < size; j++ )
            if(confusionMatrix[j][i] != 0)
                return false;
        return true;
    }

    /**
     * Method will traverse the trained tree until it hits a leaf or a new unknown value is encountered.
     * In that case it will return most common value in remembered list of entries in that tree node.
     * @param threeNode
     * @param entry
     * @return prediction
     */
    private String prediction(ID3TreeNode threeNode, DataEntry entry) {
        if(threeNode.getNodeValue() != null)
            return threeNode.getNodeValue();
        String value = entry.getValue(threeNode.getAttribute());
        ID3TreeNode node = threeNode.getNodes().get(value);
        //IF THERE IS NO SUCH VALUE, UNKNOWN VALUE FOR EXAMPLE
        if(node == null){
            return threeNode.getMostCommonValue();
        }
        return prediction(node, entry);
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
