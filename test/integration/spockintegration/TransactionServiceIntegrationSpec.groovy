package spockintegration;
import grails.plugin.spock.IntegrationSpec


public class TransactionServiceIntegrationSpec extends IntegrationSpec {

    def transactionService

    def "balance of the user changes if the transaction is created for the amount less than the balance"() {
        setup:
        User user = new User(userName: "Test", password: "Test")
        user.save(flush: true)
        BigDecimal balance = user.account.balance

        when:
        transactionService.saveTransaction(user.account, 200, TransactionType.Dr)

        then:
        (balance - user.account.balance) == 200
        user.account.transactions.toList().size() == 1

    }

    def "exception is thrown for transaction amount more than 800 "() {
        setup:
        User user = new User(userName: "Test", password: "Test")
        user.save(flush: true)

        when:
        transactionService.saveTransaction(user.account, 1000, TransactionType.Dr)

        then:
        def exception = thrown(RuntimeException)
        exception.message == "Not enough balance to make this transaction"

    }

}