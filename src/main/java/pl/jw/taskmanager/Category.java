package pl.jw.taskmanager;

public enum Category {
    HOUSEWORK("Zadania domowe"), WORK("Praca"), COURSE("Kursy");

    private String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

