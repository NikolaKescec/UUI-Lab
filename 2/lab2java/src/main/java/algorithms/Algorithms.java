package algorithms;

import structures.Clausula;
import structures.ClausulaPair;
import structures.Result;

import java.util.*;

public class Algorithms {

    public static Result resolutionAlgorithm(LinkedHashSet<Clausula> negatedProveClausulas,  LinkedHashSet<Clausula> clausulaList, Clausula originalClausula){
        printClausulas(clausulaList);
        LinkedHashSet<Clausula> sos = new LinkedHashSet<>(negatedProveClausulas);
        LinkedHashSet<Clausula> combinedSet = new LinkedHashSet<>(clausulaList);
        combinedSet.addAll(sos);
        printClausulas(sos);
        System.out.println("===============");
        ClausulaPair pair;
        while((pair = selectClauses(combinedSet, sos)) != null) {
            String resolvent = resolve(pair.getFirstClausula(), pair.getSecondClausula());

            if (resolvent.equals("NIL")) {
                System.out.println(Clausula.getTotalNumberOfClausula() + ". NIL (" + pair.getFirstClausula().getClausulaNumber() + ", " + pair.getSecondClausula().getClausulaNumber() + ")");
                return new Result(true, originalClausula.getClausula());
            }

            if(!resolvent.isEmpty()) {
                System.out.println(Clausula.getTotalNumberOfClausula() + ". " + resolvent + "(" + pair.getFirstClausula().getClausulaNumber() + ", " + pair.getSecondClausula().getClausulaNumber() + ")");
                StrategyAlgorihtms.addToClausulaSet(resolvent, sos);
            }
        }
        return new Result(false, originalClausula.getClausula());
    }



    private static ClausulaPair selectClauses(LinkedHashSet<Clausula> combinedSet, LinkedHashSet<Clausula> sosSet) {
        for(Clausula first : combinedSet) {
            if(first.isResolved())
                continue;
            for(Clausula second : sosSet) {
                if(second.isResolved())
                    continue;

                if(first.equals(second))
                    continue;

                return new ClausulaPair(first, second);
            }
        }
        return null;
    }

    private static String resolve(Clausula clausula, Clausula strategyClausula) {
        LinkedHashSet<String> resolutions = new LinkedHashSet<>();

        List<String> biggerClausula, smallerClausula;
        if(clausula.getLiterals().size() > strategyClausula.getLiterals().size()) {
            biggerClausula = clausula.getLiterals();
            smallerClausula = strategyClausula.getLiterals();
        } else {
            biggerClausula = strategyClausula.getLiterals();
            smallerClausula = clausula.getLiterals();
        }

        Iterator<String> firstLiteralsIterator = biggerClausula.listIterator();
        boolean resolved = false;
        while(firstLiteralsIterator.hasNext()) {
            String literal = firstLiteralsIterator.next();
            Iterator<String> secondLiteralIterator = smallerClausula.listIterator();
            while(secondLiteralIterator.hasNext()) {
                String otherLiteral = secondLiteralIterator.next();
                if((literal.startsWith("~") && literal.substring(1).equals(otherLiteral))
                        || (otherLiteral.startsWith("~") && otherLiteral.substring(1).equals(literal))){
                    resolved = true;
                    firstLiteralsIterator.remove();
                    secondLiteralIterator.remove();
                    break;
                }
            }
            if(resolved)
                break;
        }

        if(resolved) {
            smallerClausula.addAll(biggerClausula);
            return String.join(" v ", smallerClausula);
        }

        return "";
    }

    public static Result cookingAlgorithm(){return null;}

    public static void printClausulas(LinkedHashSet<Clausula> clausulas) {
        for(Clausula clausula : clausulas)
            System.out.println(clausula.getClausulaNumber() + "." + " " + clausula.getClausula());
    }
}
