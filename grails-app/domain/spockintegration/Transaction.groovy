package spockintegration

class Transaction {

    BigDecimal amount
    TransactionType type

    static belongsTo = [account: Account]

    static constraints = {
    }
}
