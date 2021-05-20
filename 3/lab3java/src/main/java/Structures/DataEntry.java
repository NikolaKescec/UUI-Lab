package Structures;

import java.util.*;

public class DataEntry {

    //DATA VALUES
    Map<String, String> values;

    //CONSTRUCTOR
    public DataEntry(Map<String, String> values) {
        this.values = values;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public String getValue(String attribute) {
        return values.get(attribute);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataEntry)) return false;
        DataEntry dataEntry = (DataEntry) o;
        return Objects.equals(values, dataEntry.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
