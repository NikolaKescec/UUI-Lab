package algorithms;

import structures.Clausula;

import java.util.*;
import java.util.stream.Collectors;

public class StrategyAlgorihtms {

    public static boolean addToClausulaSet(String newClausula, Set<Clausula> clausulaSet) {

        newClausula = newClausula.toLowerCase(Locale.ROOT);
        newClausula = factorize(newClausula);
        if(unimportance(newClausula))
            return false;

        removeAlreadyContained(newClausula, clausulaSet);
        Clausula.incrementTotalNumberOfClausula();
        clausulaSet.add(new Clausula(newClausula, Clausula.getTotalNumberOfClausula()));
        return true;
    }

    public static void removeAlreadyContained(String clausula, Set<Clausula> clausulaSet) {
        clausulaSet.removeIf(current -> subSet(current.getLiterals(), clausula));
    }

    private static boolean subSet(List<String> literals, String newClausula) {
        return literals.containsAll(Arrays.asList(newClausula.split(" v ")));
    }

    private static boolean unimportance(String newClausula) {
        String[] literals = newClausula.split(" v ");
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

    private static String factorize(String newClausula) {
        String[] literals = newClausula.split(" v ");
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
