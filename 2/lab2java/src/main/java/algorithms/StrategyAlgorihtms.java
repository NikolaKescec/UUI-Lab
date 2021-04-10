package algorithms;

import structures.Clausula;

import java.util.*;
import java.util.stream.Collectors;

public class StrategyAlgorihtms {

    public static void addToClausulaSet(String newClausula, Set<Clausula> clausulaSet) {

        newClausula = newClausula.toLowerCase(Locale.ROOT);
        newClausula = factorize(newClausula);
        if(unimportance(newClausula))
            return;

        Iterator<Clausula> iterator = clausulaSet.iterator();
        int replaceNumber = 0;
        while(iterator.hasNext()){
            Clausula current = iterator.next();
            if(subSet(current.getLiterals(), newClausula)){
                replaceNumber = current.getClausulaNumber();
                iterator.remove();
                clausulaSet.add(new Clausula(newClausula, replaceNumber));
                return;
            }
        }
        int additionNumber = Clausula.getTotalNumberOfClausula();
        clausulaSet.add(new Clausula(newClausula, additionNumber));
        Clausula.incrementTotalNumberOfClausula();
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
