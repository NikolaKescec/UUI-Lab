package structures;

import java.util.LinkedHashSet;

public class Result {

    private boolean conclusion;
    private String originalClausula;
    private Clausula resultClausula;
    private LinkedHashSet<Clausula> resultSos;

    public Result(boolean conclusion, String originalClausula, Clausula resultClausula, LinkedHashSet<Clausula> linkedHashSet) {
        this.conclusion = conclusion;
        this.originalClausula = originalClausula;
        this.resultClausula = resultClausula;
        this.resultSos=linkedHashSet;
    }

    public boolean isConclusion() {
        return conclusion;
    }

    public void setConclusion(boolean conclusion) {
        this.conclusion = conclusion;
    }

    public String getOriginalClausula() {
        return originalClausula;
    }

    public void setOriginalClausula(String originalClausula) {
        this.originalClausula = originalClausula;
    }

    @Override
    public String toString() {
        String output = "";
        if(!conclusion)
            return "===============\n[CONCLUSION]: " + originalClausula + " is unknown";
        output += Clausula.writeRecursive(resultClausula, resultSos) + "===============\n[CONCLUSION]: " + originalClausula + " is true";
        return output;
    }
}
