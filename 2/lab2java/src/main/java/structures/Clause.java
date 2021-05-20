package structures;

import java.util.*;

/**
 * Class represents a single CNF clause.
 */
public class Clause {

    /**
     * Used in output when numbering all clauses.
     */
    private static int startingClauseNumber = 1;

    /**
     * Clause literals.
     */
    private List<String> literals;

    /**
     * Clause in its pure form, a string.
     */
    private String clause;
    private int clauseNumber;
    private ClausePair parents;

    public Clause(String clause) {
        this(clause, null);
    }

    public Clause(String clause, ClausePair pair) {
        if(!clause.equals("NIL"))
            clause = clause.toLowerCase(Locale.ROOT);
        this.clause = clause;
        literals = new ArrayList<>(Arrays.asList(this.clause.split(" v ")));
        this.parents=pair;
    }

    public int getClauseNumber() {
        return clauseNumber;
    }

    public void setClauseNumber(int clauseNumber) {
        this.clauseNumber = clauseNumber;
    }

    public List<String> getLiterals() {
        return literals;
    }

    public String getClause() {
        return clause;
    }

    public ClausePair getParents() {
        return parents;
    }

    public void setParents(ClausePair parents) {
        this.parents = parents;
    }

    public void setClause(String clause) {
        this.clause = clause.toLowerCase(Locale.ROOT);
        literals = new ArrayList<>(Arrays.asList(this.clause.split(" v ")));
    }

    /**
     * Function negates the clause and returns a linked hash set which are all of negated clauses.
     * @param clause which will be negated
     * @return clauses that are made with negation of original clause
     */
    public static LinkedHashSet<Clause> negateClause(String clause) {
        LinkedHashSet<Clause> negatedClauses = new LinkedHashSet<>();
        clause = clause.toLowerCase(Locale.ROOT);
        if(clause.length() == 0)
            throw new IllegalArgumentException("Clause can not be empty!");
        String[] literals = clause.split(" v ");
        for (String literal : literals) {
            if(literal.startsWith("~")){
                negatedClauses.add(new Clause(literal.substring(1)));
                continue;
            }
            literal = "~" + literal;
            negatedClauses.add(new Clause(literal));
        }
        return negatedClauses;
    }

    @Override
    public String toString() {
        return clause;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clause clause1 = (Clause) o;
        return Objects.equals(literals, clause1.literals) && Objects.equals(clause, clause1.clause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(literals, clause);
    }

    public static void incrementStartingNumber() {startingClauseNumber++;}

    public static void resetStartingNumber() {startingClauseNumber = 1;}

    public static int getStartingClauseNumber() {return startingClauseNumber;}

    /**
     * This method will receive the result clause and result SOS, from which it will create a string that contains a
     * readable coherent guide that describes with what resolutions did algorithm finish.
     * @param resultClause result clause
     * @return string that is simply put a depiction of which resolutions lead to NIL clause
     */
    public static String writeRecursive(Clause resultClause) {
        // CREATE A LINKED HASH SET THAT CONTAINS, IN ORDER, RESOLVED CLAUSES AND ADDS THEM TO STRING BUILDER
        StringBuilder recursiveBuilder = new StringBuilder();
        flattenRecursively(resultClause, recursiveBuilder);
        return recursiveBuilder.toString();
    }

    /**
     * This method will flatten the parent child tree that has been made trough resolution by negation algorithm.
     * @param resultClause result clause that has the parent child tree
     * @param recursiveBuilder string builder that builds the result string
     */
    private static void flattenRecursively(Clause resultClause, StringBuilder recursiveBuilder) {
        //IF CLAUSE DOESN'T HAVE PARENTS THEN IT HAS NOT BEEN RESOLVED AND MUST ORIGINATE OUTSIDE OF SOS, THEREFORE IT IS THE END OF PARENT LIST
        if(resultClause.getParents()==null){
            return;
        }
        // FIND ALL PARENTS OF FIRST RESOLVED CLAUSE
        flattenRecursively(resultClause.getParents().getFirstClause(), recursiveBuilder);
        // FIND ALL PARENTS OF SECOND RESOLVED CLAUSE
        flattenRecursively(resultClause.getParents().getSecondClause(), recursiveBuilder);
        addToStringBuilder(resultClause, recursiveBuilder);
    }

    /**
     * Method builds a string entry from given clause.
     * @param clause given clause
     * @param recursiveBuilder builder
     */
    private static void addToStringBuilder(Clause clause, StringBuilder recursiveBuilder) {
        clause.setClauseNumber(Clause.startingClauseNumber);
        recursiveBuilder
                .append(Clause.startingClauseNumber)
                .append(". ")
                .append(clause)
                .append(" (")
                .append(clause.getParents().getFirstClause().getClauseNumber())
                .append(",")
                .append(clause.getParents().getSecondClause().getClauseNumber())
                .append(")\n");
        incrementStartingNumber();
    }
}
