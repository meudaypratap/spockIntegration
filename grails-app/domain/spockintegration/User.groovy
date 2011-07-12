package spockintegration

class User {

    String userName
    String password
    Account account

    static constraints = {
        account(nullable: true)
    }

    def afterInsert = {
        User.withNewSession {
            Account account = new Account(balance: 800, user: this)
            account.save(flush: true)
            this.account = account
        }
    }
}
