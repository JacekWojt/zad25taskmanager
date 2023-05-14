package pl.jw.taskmanager;

public enum Phase {
    TODO("Do zrobienia"), DONE("Zrobione"), ALL("Wszystkie");

    private String displayName;

    Phase(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

