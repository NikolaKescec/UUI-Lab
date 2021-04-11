package algorithms;

import structures.Clausula;
import structures.ClausulaPair;
import structures.Command;
import structures.Result;

import java.util.*;

public class Algorithms {

    public static Result resolutionAlgorithm(LinkedHashSet<Clausula> negatedProveClausulas,  LinkedHashSet<Clausula> clausulaList, Clausula originalClausula){
        printClausulas(clausulaList);
        LinkedHashSet<Clausula> sos = new LinkedHashSet<>(negatedProveClausulas);
        LinkedHashSet<Clausula> combinedSet = new LinkedHashSet<>(clausulaList);
        HashMap<String, Set<String>> resolvedMaps = new HashMap<>();
        combinedSet.addAll(sos);
        printClausulas(sos);
        System.out.println("===============");
        ClausulaPair pair;
        while((pair = selectClauses(combinedSet, sos, resolvedMaps)) != null) {
            String resolvent = resolve(pair.getFirstClausula(), pair.getSecondClausula());

            if (resolvent.equals("NIL")) {
                System.out.println(Clausula.getTotalNumberOfClausula() + 1 + ". NIL (" + pair.getFirstClausula().getClausulaNumber() + ", " + pair.getSecondClausula().getClausulaNumber() + ")");
                return new Result(true, originalClausula.getClausula());
            }

            if(!resolvent.isEmpty()) {
                System.out.println(Clausula.getTotalNumberOfClausula() + 1 + ". " + resolvent + " (" + pair.getFirstClausula().getClausulaNumber() + ", " + pair.getSecondClausula().getClausulaNumber() + ")");
                StrategyAlgorihtms.addToClausulaSet(resolvent, sos);
                combinedSet.addAll(sos);
            }
        }
        return new Result(false, originalClausula.getClausula());
    }



    private static ClausulaPair selectClauses(LinkedHashSet<Clausula> combinedSet, LinkedHashSet<Clausula> sosSet, HashMap<String, Set<String>> testedPairs) {
        for(Clausula first : combinedSet) {
            for(Clausula second : sosSet) {
                if(first.equals(second))
                    continue;

                if(testedPairs.get(first.getClausula()) != null && testedPairs.get(first.getClausula()).contains(second.getClausula()))
                    continue;
                testedPairs.computeIfAbsent(first.getClausula(), k -> new HashSet<>());
                testedPairs.get(first.getClausula()).add(second.getClausula());

                return new ClausulaPair(first, second);
            }
        }
        return null;
    }

    private static String resolve(Clausula clausula, Clausula strategyClausula) {
        LinkedHashSet<String> resolutions = new LinkedHashSet<>();

        List<String> biggerClausula, smallerClausula;
        if(clausula.getLiterals().size() > strategyClausula.getLiterals().size()) {
            biggerClausula = new ArrayList<>(clausula.getLiterals());
            smallerClausula = new ArrayList<>(strategyClausula.getLiterals());
        } else {
            biggerClausula = new ArrayList<>(strategyClausula.getLiterals());
            smallerClausula = new ArrayList<>(clausula.getLiterals());
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
            if(biggerClausula.isEmpty() && smallerClausula.isEmpty())
                return "NIL";

            List<String> newClausula = new ArrayList<>(biggerClausula);
            newClausula.addAll(smallerClausula);
            return String.join(" v ", newClausula);
        }

        return "";
    }

    public static void cookingAlgorithm(List<Command> commands, LinkedHashSet<Clausula> clausulaSet){
        for(Command command : commands) {
            switch (command.getType()){
                case TEST:
                    System.out.println("User's command: " + command);
                    Clausula.resetClausulaNumber(clausulaSet.size());
                    System.out.println(resolutionAlgorithm(Clausula.negateClausula(command.getClausula().getClausula()), clausulaSet, command.getClausula()));
                    System.out.println();
                    break;
                case ADD:
                    System.out.println("User's command: " + command);
                    Clausula.resetClausulaNumber(clausulaSet.size());
                    StrategyAlgorihtms.addToClausulaSet(command.getClausula().toString(), clausulaSet);
                    System.out.println("Added " + command.getClausula().toString());
                    System.out.println();
                    break;
                case REMOVE:
                    System.out.println("User's command: " + command);
                    clausulaSet.remove(command.getClausula());
                    Clausula.resetClausulaNumber(clausulaSet.size());
                    Clausula.fixNumbers(clausulaSet);
                    System.out.println("Removed " + command.getClausula().toString());
                    System.out.println();
                    break;
                default:
                    throw new IllegalArgumentException("No such command type!");
            }
        }
    }

    public static void printClausulas(LinkedHashSet<Clausula> clausulas) {
        for(Clausula clausula : clausulas)
            System.out.println(clausula.getClausulaNumber() + "." + " " + clausula.getClausula());
    }
}
