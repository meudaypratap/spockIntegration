package spockintegration

class Account {

    BigDecimal balance

    static belongsTo = [user: User]
    static hasMany = [transactions: Transaction]

    static constraints = {
    }
}
