package algorithms;

import structures.Clause;
import structures.ClausePair;

import java.util.*;

/**
 * Strategy algorithms class.
 */
public class StrategyAlgorithms {

    /**
     * Method adds a clause to given set, but first will check if its unimportant and if its already contained in another
     * clause. Will factorize the clause as well.
     * @param newClause clause to be added
     * @param clauseSet clause set
     * @param pair clause pair
     * @return true if added, false if not added
     */
    public static boolean addToClauseSet(String newClause, Set<Clause> clauseSet, ClausePair pair) {

        newClause = newClause.toLowerCase(Locale.ROOT);
        newClause = factorize(newClause);
        if(unimportance(newClause))
            return false;

        removeAlreadyContained(newClause, clauseSet);
        clauseSet.add(new Clause(newClause, pair));
        return true;
    }

    public static void removeAlreadyContained(String clause, Set<Clause> clauseSet) {
        clauseSet.removeIf(current -> subSet(current.getLiterals(), clause));
    }

    /**
     * Method checks if new clause is contained in given literals.
     * @param literals
     * @param newClause
     * @return true if contained, false if not
     */
    private static boolean subSet(List<String> literals, String newClause) {
        return literals.containsAll(Arrays.asList(newClause.split(" v ")));
    }

    /**
     * Method will check if clause contains contradicting literals.
     * @param newClause
     * @return true if not important, false if important
     */
    private static boolean unimportance(String newClause) {
        String[] literals = newClause.split(" v ");
        for(int i = 0; i < literals.length; i++) {
            for(int j = i + 1; j < literals.length; j++) {
                if(literals[j].startsWith("~") && !literals[i].startsWith("~") && literals[j].substring(1).equals(literals[i]))
                    return true;
                if(!literals[j].startsWith("~") && literals[i].startsWith("~") && literals[i].substring(1).equals(literals[j]))
                    return true;
            }

        }
        return false;
    }

    /**
     * Method factorizes a given clause.
     * @param newClause
     * @return
     */
    private static String factorize(String newClause) {
        String[] literals = newClause.split(" v ");
        for(int i = 0; i < literals.length; i++) {
            for(int j = i+1; j < literals.length; j++) {
                if(literals[i].equals(literals[j]))
                    literals[j] = "";
            }
        }
        StringBuilder factorizedBuilder = new StringBuilder();
        for(int i = 0; i < literals.length; i++) {
            if(!literals[i].equals("")) {
                factorizedBuilder.append(literals[i]).append(i == literals.length - 1 ? "" : " v ");
            }
        }
        return factorizedBuilder.toString();
    }

}
