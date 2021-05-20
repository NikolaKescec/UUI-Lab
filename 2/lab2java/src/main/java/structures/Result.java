package structures;

import java.util.LinkedHashSet;

/**
 * Class represents the result of resolution by negation algorithm.
 */
public class Result {

    /**
     * Describes if NIL has been proved, specifically, if negated goal clause has been disapproved.
     */
    private boolean conclusion;

    /**
     * Original clause that had to be approved.
     */
    private String originalClause;

    /**
     * NIL clause that has parent child tree structure.
     */
    private Clause resultClause;

    /**
     * Resulting SOS that contains all resolved clauses.
     */
    private LinkedHashSet<Clause> resultSos;

    public Result(boolean conclusion, String originalClause, Clause resultClause, LinkedHashSet<Clause> linkedHashSet) {
        this.conclusion = conclusion;
        this.originalClause = originalClause;
        this.resultClause = resultClause;
        this.resultSos=linkedHashSet;
    }

    public boolean isConclusion() {
        return conclusion;
    }

    public void setConclusion(boolean conclusion) {
        this.conclusion = conclusion;
    }

    public String getOriginalClause() {
        return originalClause;
    }

    public void setOriginalClause(String originalClause) {
        this.originalClause = originalClause;
    }

    @Override
    public String toString() {
        String output = "";
        if(!conclusion)
            return "[CONCLUSION]: " + originalClause + " is unknown";
        output += Clause.writeRecursive(resultClause) + "===============\n[CONCLUSION]: " + originalClause + " is true";
        return output;
    }
}
