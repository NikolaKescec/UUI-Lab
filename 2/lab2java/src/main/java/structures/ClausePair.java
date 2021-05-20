package structures;

import java.util.Objects;

/**
 * Class represents a pair of clauses.
 */
public class ClausePair {

    private Clause firstClause;
    private Clause secondClause;

    public ClausePair(Clause firstClause, Clause secondClause) {
        this.firstClause = firstClause;
        this.secondClause = secondClause;
    }

    public Clause getFirstClause() {
        return firstClause;
    }

    public void setFirstClause(Clause firstClause) {
        this.firstClause = firstClause;
    }

    public Clause getSecondClause() {
        return secondClause;
    }

    public void setSecondClause(Clause secondClause) {
        this.secondClause = secondClause;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClausePair that = (ClausePair) o;
        return Objects.equals(firstClause, that.firstClause) && Objects.equals(secondClause, that.secondClause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstClause, secondClause);
    }
}
