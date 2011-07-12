package spockintegration;
import grails.plugin.spock.IntegrationSpec

public class TransactionControllerIntegrationSpec extends IntegrationSpec {

    def transactionService
    TransactionController controller = new TransactionController()

    def setup() {
        controller.transactionService = transactionService
    }

    def "transactions are created only if account has enough balance"() {
        setup:
        User user = new User(userName: "Test", password: "Test")
        user.save(flush: true)

        when:
        controller.params.amount = 200
        controller.params.type = TransactionType.Dr.key
        controller.params['account.id'] = user.accountId

        controller.save()

        then:
        Transaction.count() == 1
        user.account.transactions.toList().size() == 1

    }

    def "transaction is not created because of insufficient balance"() {
        setup:
        User user = new User(userName: "Test", password: "Test")
        user.save(flush: true)

        when:
        controller.params.amount = 1000
        controller.params.type = TransactionType.Dr.key
        controller.params['account.id'] = user.accountId

        controller.save()

        then:
        controller.flash.message == "Not enough balance to make this transaction"
        Transaction.count() == 0
        !user.account.transactions

    }

}