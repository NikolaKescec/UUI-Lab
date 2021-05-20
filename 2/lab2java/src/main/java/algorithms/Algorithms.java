package algorithms;

import structures.Clause;
import structures.ClausePair;
import structures.Command;
import structures.Result;

import java.util.*;

/**
 * Class contains all algorithms and helper algorithms. Main algorithm is resolution algorithm.
 */
public class Algorithms {

    /**
     * This resolution algorithm is an implementation of resolution by negation.
     * @param negatedGoalClause negated goal clause
     * @param clauseList list of all clauses
     * @param originalClause original goal clause (non negated)
     * @return result
     */
    public static Result resolutionAlgorithm(LinkedHashSet<Clause> negatedGoalClause, LinkedHashSet<Clause> clauseList, Clause originalClause){
        //RESETS THE CLAUSE NUMBERING
        Clause.resetStartingNumber();
        printClauses(clauseList);
        LinkedHashSet<Clause> sos = new LinkedHashSet<>(negatedGoalClause);
        LinkedHashSet<Clause> combinedSet = new LinkedHashSet<>(clauseList);
        HashMap<String, Set<String>> resolvedMaps = new HashMap<>();
        combinedSet.addAll(sos);
        printClauses(sos);
        System.out.print("===============\n");
        ClausePair pair;
        //While there are pairs that can be tested from combined and sos set, they are given to resolution algorithm
        while((pair = selectClauses(combinedSet, sos, resolvedMaps)) != null) {
            //Resolves first clause and second clause
            String resolvent = resolve(pair.getFirstClause(), pair.getSecondClause());

            //IF new clause is NIL clause then resolution algorithm is finished
            if (resolvent.equals("NIL")) {
                return new Result(true, originalClause.getClause(), new Clause(resolvent, pair), sos);
            }

            //Adds this new clause to combined set and SOS
            if(!resolvent.isEmpty()) {
                if(StrategyAlgorithms.addToClauseSet(resolvent, sos, pair)) {
                    for(Clause clause : sos) {
                        StrategyAlgorithms.removeAlreadyContained(clause.getClause(), combinedSet);
                        combinedSet.add(clause);
                    }
                }
            }
        }
        //NIL clause has not been resolved, resolution algorithm is finished and goal clause is inconclusive.
        return new Result(false, originalClause.getClause(), null, null);
    }

    /**
     * Selects a pair of clauses from given combined set and sos set.
     * @param combinedSet set that contains all clauses, starting clauses and sos clauses
     * @param sosSet sos set
     * @param testedPairs map that for each clause has a set of strings that contains already tested clauses so they are ignored
     * @return clause pair
     */
    private static ClausePair selectClauses(LinkedHashSet<Clause> combinedSet, LinkedHashSet<Clause> sosSet, HashMap<String, Set<String>> testedPairs) {
        for(Clause first : combinedSet) {
            for(Clause second : sosSet) {
                if(first.equals(second))
                    continue;

                if(testedPairs.get(first.getClause()) != null && testedPairs.get(first.getClause()).contains(second.getClause()))
                    continue;
                testedPairs.computeIfAbsent(first.getClause(), k -> new HashSet<>());
                testedPairs.get(first.getClause()).add(second.getClause());

                return new ClausePair(first, second);
            }
        }
        return null;
    }

    /**
     * Method which takes two clauses and then iterates trough both of clauses literals while searching for negations.
     * If it finds such literals that they are negations of each other, then it resolves the clauses. Resolves FIRST OCCURRENCE.
     * @param clause first clause
     * @param strategyClause second clause
     * @return a new resolved clause in string format
     */
    private static String resolve(Clause clause, Clause strategyClause) {
        LinkedHashSet<String> resolutions = new LinkedHashSet<>();

        // FIND A BIGGER CLAUSE
        List<String> biggerClause, smallerClause;
        if(clause.getLiterals().size() > strategyClause.getLiterals().size()) {
            biggerClause = new ArrayList<>(clause.getLiterals());
            smallerClause = new ArrayList<>(strategyClause.getLiterals());
        } else {
            biggerClause = new ArrayList<>(strategyClause.getLiterals());
            smallerClause = new ArrayList<>(clause.getLiterals());
        }

        Iterator<String> firstLiteralsIterator = biggerClause.listIterator();
        boolean resolved = false;
        while(firstLiteralsIterator.hasNext()) {
            String literal = firstLiteralsIterator.next();
            Iterator<String> secondLiteralIterator = smallerClause.listIterator();
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
            if(biggerClause.isEmpty() && smallerClause.isEmpty())
                return "NIL";

            List<String> newClause = new ArrayList<>(biggerClause);
            newClause.addAll(smallerClause);
            return String.join(" v ", newClause);
        }

        return "";
    }

    /**
     * Implementation of cooking algorithm. Does a set of instructions depending on given command.
     * @param commands list of commands that can be of TEST, ADD, REMOVE type.
     * @param clauseSet given clause set with which cooking algorithm works.
     */
    public static void cookingAlgorithm(List<Command> commands, LinkedHashSet<Clause> clauseSet){
        for(Command command : commands) {
            System.out.print("User's command: " + command + "\n");
            switch (command.getType()){
                //CALLS resolution algorithm with negated command clause.
                case TEST:
                    System.out.println(resolutionAlgorithm(Clause.negateClause(command.getClause().getClause()), clauseSet, command.getClause()));
                    break;
                //ADDS a new clause to the clause set.
                case ADD:
                    StrategyAlgorithms.addToClauseSet(command.getClause().toString(), clauseSet, null);
                    System.out.print("Added " + command.getClause().toString() + "\n");
                    break;
                //REMOVES a clause from the clause set.
                case REMOVE:
                    clauseSet.remove(command.getClause());
                    System.out.print("Removed " + command.getClause().toString() + "\n");
                    break;
                default:
                    throw new IllegalArgumentException("No such command type!");
            }
            System.out.print("\n");
        }
    }

    /**
     * Helper method that writes out all clauses from given linked hash set and numerates them, while internally counting
     * total number of clauses.
     * @param clauses linked hash set of given clauses
     */
    public static void printClauses(LinkedHashSet<Clause> clauses) {
        for(Clause clause : clauses) {
            System.out.print(Clause.getStartingClauseNumber() + "." + " " + clause.getClause() + "\n");
            clause.setClauseNumber(Clause.getStartingClauseNumber());
            Clause.incrementStartingNumber();
        }
    }
}
