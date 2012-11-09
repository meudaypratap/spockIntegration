package spockintegration;


import grails.plugin.spock.IntegrationSpec
import spock.lang.Unroll

public class TransactionServiceIntegrationSpec extends IntegrationSpec {

    def transactionService

    @Unroll('#sno ..Balance : #transactionAmount, #transactionType')
    def "balance of the user changes if the transaction is created for the amount less than the balance"() {
        setup:
        User user = new User(userName: "Test", password: "Test")
        user.save(flush: true)

        when:
        transactionService.saveTransaction(user.account, transactionAmount, transactionType)

        then:
        user.account.balance == accountBalance
        user.account.transactions.toList().size() == 1

        where:
        sno | transactionAmount | transactionType    | accountBalance
        1   | 200               | TransactionType.Dr | 600
        2   | 0                 | TransactionType.Dr | 800
        3   | 200               | TransactionType.Cr | 1000
        4   | 0                 | TransactionType.Cr | 800

    }

    @Unroll('#sno ..Balance : #transactionAmount, #transactionType')
    def "exception is thrown for transaction amount more than 800 "() {
        setup:
        User user = new User(userName: "Test", password: "Test")
        user.save(flush: true)

        when:
        transactionService.saveTransaction(user.account, 1000, transactionType)

        then:
        def exception = thrown(RuntimeException)
        exception.message == "Not enough balance to make this transaction"
        user.account.balance == 800

        where:
        sno | transactionType
        1   | TransactionType.Dr
        2   | TransactionType.Cr
    }

}