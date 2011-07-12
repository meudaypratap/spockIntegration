package spockintegration

public enum TransactionType {

    Dr("Debit"), Cr("Credit")

    private final String name;

    TransactionType(String name) {
        this.name = name
    }

    public String toString() {
        return name;
    }

    public String getKey() { return name() }

    public String getValue() { return name }

    public static List<TransactionType> list() {
        return TransactionType.values()
    }

}