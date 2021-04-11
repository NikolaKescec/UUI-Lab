package structures;

public class Result {

    private boolean conclusion;
    private String originalClausula;

    public Result(boolean conclusion, String originalClausula) {
        this.conclusion = conclusion;
        this.originalClausula = originalClausula;
    }

    public boolean isConclusion() {
        return conclusion;
    }

    public void setConclusion(boolean conclusion) {
        this.conclusion = conclusion;
    }

    public String getOriginalClausula() {
        return originalClausula;
    }

    public void setOriginalClausula(String originalClausula) {
        this.originalClausula = originalClausula;
    }

    @Override
    public String toString() {
        return "===============\n[CONCLUSION]: " + originalClausula + " is " + (conclusion ? "true" : "unknown");
    }
}
