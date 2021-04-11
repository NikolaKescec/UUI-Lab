package structures;

import java.util.*;
import java.util.stream.Collectors;

public class Clausula {

    private static int totalNumberOfClausula = 0;

    private List<String> literals;
    private String clausula;
    private int clausulaNumber;
    private boolean resolved;

    public Clausula(String clausula) {
        this(clausula, 0);
    }

    public Clausula(String clausula, int clausulaNumber) {
        this.clausula = clausula.toLowerCase(Locale.ROOT);
        literals = new ArrayList<>(Arrays.asList(this.clausula.split(" v ")));
        this.clausulaNumber = clausulaNumber;
        this.resolved=false;
    }

    public List<String> getLiterals() {
        return literals;
    }

    public String getClausula() {
        return clausula;
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
            Clausula.incrementTotalNumberOfClausula();
            if(literal.startsWith("~")){
                negatedClausulas.add(new Clausula(literal.substring(1),  Clausula.totalNumberOfClausula));
                continue;
            }
            literal = "~" + literal;
            negatedClausulas.add(new Clausula(literal, Clausula.totalNumberOfClausula));
        }
        return negatedClausulas;
    }

    public static int getTotalNumberOfClausula() {
        return totalNumberOfClausula;
    }

    public static void incrementTotalNumberOfClausula() {
        totalNumberOfClausula++;
    }

    public static void decrementTotalNumberOfClausula() {
        totalNumberOfClausula--;
    }

    public static void resetClausulaNumber(int number) {
        totalNumberOfClausula = number;
    }

    public static void fixNumbers(LinkedHashSet<Clausula> clausulas) {
        int i = 1;
        for(Clausula clausula : clausulas) {
            clausula.setClausulaNumber(i);
            i++;
        }
    }

    @Override
    public String toString() {
        return clausula;
    }

    public int getClausulaNumber() {
        return clausulaNumber;
    }

    public void setClausulaNumber(int clausulaNumber) {
        this.clausulaNumber = clausulaNumber;
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
}
