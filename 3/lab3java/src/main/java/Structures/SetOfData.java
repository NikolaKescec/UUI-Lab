package Structures;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetOfData {

    /**A list of prepared rows**/
    private List<DataEntry> entries;
    /**Map that maps a attribute to its set of distinct values**/
    private static Map<String, Set<String>> attributes;
    /**Set that contains goal values**/
    public static Set<String> trainingGoals;
    /**Set that contains goal values**/
    public static Set<String> testingGoals;
    /**Value contains goal attribute name**/
    public static String goalName;

    public SetOfData(List<DataEntry> data) {
        this.entries = data;
    }

    public static Map<String, Set<String>> getAttributes() {
        return attributes;
    }

    public List<DataEntry> getEntries() {
        return entries;
    }

    public static Set<String> getTrainingGoals() {
        return trainingGoals;
    }

    public static void setTrainingGoals(Set<String> trainingGoals) {
        SetOfData.trainingGoals = trainingGoals;
    }

    public static String getGoalName() {
        return goalName;
    }


    public static Set<String> getTestingGoals() {
        return testingGoals;
    }

    public static void setTestingGoals(Set<String> testingGoals) {
        SetOfData.testingGoals = testingGoals;
    }

    public static void setGoalName(String goal) {
        SetOfData.goalName = goal;
    }

    public static void setAttributes(Map<String, Set<String>> attributes) {
        SetOfData.attributes = attributes;
    }
}
