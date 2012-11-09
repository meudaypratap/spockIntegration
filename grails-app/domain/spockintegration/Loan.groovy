package spockintegration

class Loan {

    Transaction transaction
    Boolean isRepaid = false

    static constraints = {
    }

    Account getBeneficiaryAccount() {
        transaction?.account
    }
}
