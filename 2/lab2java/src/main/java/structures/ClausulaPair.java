package structures;

import java.util.Objects;

public class ClausulaPair {

    private Clausula firstClausula;
    private Clausula secondClausula;

    public ClausulaPair(Clausula firstClausula, Clausula secondClausula) {
        this.firstClausula = firstClausula;
        this.secondClausula = secondClausula;
    }

    public Clausula getFirstClausula() {
        return firstClausula;
    }

    public void setFirstClausula(Clausula firstClausula) {
        this.firstClausula = firstClausula;
    }

    public Clausula getSecondClausula() {
        return secondClausula;
    }

    public void setSecondClausula(Clausula secondClausula) {
        this.secondClausula = secondClausula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClausulaPair that = (ClausulaPair) o;
        return Objects.equals(firstClausula, that.firstClausula) && Objects.equals(secondClausula, that.secondClausula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstClausula, secondClausula);
    }
}
