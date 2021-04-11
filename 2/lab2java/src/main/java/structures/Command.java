package structures;

public class Command {

    private Clausula clausula;
    private CommandType type;

    public Command(String clausulaCommand, CommandType type) {
        this.clausula = new Clausula(clausulaCommand);
        this.type = type;
    }

    public Clausula getClausula() {
        return clausula;
    }

    public void setClausula(Clausula clausula) {
        this.clausula = clausula;
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
        return clausula.toString() + command;
    }
}
