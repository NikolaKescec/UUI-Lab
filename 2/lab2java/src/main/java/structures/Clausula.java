package structures;

import java.util.*;
import java.util.stream.Collectors;

public class Clausula {

    private static int startingClausulaNumber = 1;

    private List<String> literals;
    private String clausula;
    private int clausulaNumber;
    private boolean resolved;
    private ClausulaPair parents;

    public Clausula(String clausula) {
        this(clausula, null);
    }

    public Clausula(String clausula, ClausulaPair pair) {
        if(!clausula.equals("NIL"))
            clausula = clausula.toLowerCase(Locale.ROOT);
        this.clausula = clausula;
        literals = new ArrayList<>(Arrays.asList(this.clausula.split(" v ")));
        this.resolved=false;
        this.parents=pair;
    }

    public int getClausulaNumber() {
        return clausulaNumber;
    }

    public void setClausulaNumber(int clausulaNumber) {
        this.clausulaNumber = clausulaNumber;
    }

    public List<String> getLiterals() {
        return literals;
    }

    public String getClausula() {
        return clausula;
    }

    public ClausulaPair getParents() {
        return parents;
    }

    public void setParents(ClausulaPair parents) {
        this.parents = parents;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public void setClausula(String clausula) {
        this.clausula = clausula.toLowerCase(Locale.ROOT);
        literals = new ArrayList<>(Arrays.asList(this.clausula.split(" v ")));
    }

    public static LinkedHashSet<Clausula> negateClausula(String clausula) {
        LinkedHashSet<Clausula> negatedClausulas = new LinkedHashSet<>();
        clausula = clausula.toLowerCase(Locale.ROOT);
        if(clausula.length() == 0)
            throw new IllegalArgumentException("Clausula can not be empty!");
        String[] literals = clausula.split(" v ");
        for (String literal : literals) {
            if(literal.startsWith("~")){
                negatedClausulas.add(new Clausula(literal.substring(1)));
                continue;
            }
            literal = "~" + literal;
            negatedClausulas.add(new Clausula(literal));
        }
        return negatedClausulas;
    }

    @Override
    public String toString() {
        return clausula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clausula clausula1 = (Clausula) o;
        return Objects.equals(literals, clausula1.literals) && Objects.equals(clausula, clausula1.clausula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(literals, clausula);
    }

    public static void incrementStartingNumber() {startingClausulaNumber++;}

    public static void resetStartingNumber() {startingClausulaNumber = 1;}

    public static int getStartingClausulaNumber() {return startingClausulaNumber;}

    public static String writeRecursive(Clausula resultClausula, LinkedHashSet<Clausula> resultSos) {
        LinkedHashSet<Clausula> resolvedList = new LinkedHashSet<>();
        flattenRecursevly(resultClausula, resolvedList, resultSos);
        StringBuilder recursiveBuilder = new StringBuilder();
        for (Clausula value : resolvedList) {
            if(value.getParents() == null) continue;
            value.setClausulaNumber(Clausula.startingClausulaNumber);
            recursiveBuilder
                    .append(Clausula.startingClausulaNumber)
                    .append(". ")
                    .append(value)
                    .append(" (")
                    .append(value.getParents().getFirstClausula().getClausulaNumber())
                    .append(",")
                    .append(value.getParents().getSecondClausula().getClausulaNumber())
                    .append(")\n");
            incrementStartingNumber();
        }
        return recursiveBuilder.toString();
    }

    private static void flattenRecursevly(Clausula resultClausula, LinkedHashSet<Clausula> resolvedList, LinkedHashSet<Clausula> resultSos) {
        if(resultClausula.getParents()==null){
            return;
        }
        flattenRecursevly(resultClausula.getParents().getFirstClausula(), resolvedList, resultSos);
        if(resultSos.contains(resultClausula.getParents().getFirstClausula()))
            resolvedList.add(resultClausula.getParents().getFirstClausula());
        flattenRecursevly(resultClausula.getParents().getSecondClausula(), resolvedList, resultSos);
        if(resultSos.contains(resultClausula.getParents().getSecondClausula()))
            resolvedList.add(resultClausula.getParents().getSecondClausula());
        resolvedList.add(resultClausula);
    }
}
