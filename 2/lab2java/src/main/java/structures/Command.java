package structures;

/**
 * Class represents a command.
 */
public class Command {

    /**
     * Clause that command has to modify.
     */
    private Clause clause;

    /**
     * COmmand type.
     */
    private CommandType type;

    public Command(String clauseCommand, CommandType type) {
        this.clause = new Clause(clauseCommand);
        this.type = type;
    }

    public Clause getClause() {
        return clause;
    }

    public void setClause(Clause clause) {
        this.clause = clause;
    }

    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String command = "";
        switch (this.type){
            case ADD: command = " +"; break;
            case TEST: command = " ?"; break;
            case REMOVE: command = " -"; break;
        }
        return clause.toString() + command;
    }
}
