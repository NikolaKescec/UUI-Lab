package Structures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetOfData {

    private List<DataEntry> entries;
    private static Map<String, Set<String>> attributes;
    public static Set<String> goals;
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

    public static SetOfData getASubSet(SetOfData setOfData, DataValue value){return null;}

    public static Set<String> getGoals() {
        return goals;
    }

    public static void setGoals(Set<String> goals) {
        SetOfData.goals = goals;
    }

    public static String getGoalName() {
        return goalName;
    }

    public static void setGoalName(String goal) {
        SetOfData.goalName = goal;
    }

    public static void setAttributes(Map<String, Set<String>> attributes) {
        SetOfData.attributes = attributes;
    }
}
