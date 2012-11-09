package spockintegration;


import grails.plugin.spock.IntegrationSpec
import spock.lang.Unroll

public class TransactionServiceIntegrationSpec extends IntegrationSpec {

    def transactionService
    User user

    def setup() {
        user = new User(userName: "Test", password: "Test").save(flush: true)
    }

    @Unroll('#sno ..Balance : #transactionAmount, #transactionType')
    def "balance of the user changes if the transaction is created for the amount less than the balance"() {
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

    //Exercise
    @Unroll('#sno ..transactionAmount : #transfererBalance, #transfereeBalance')
    def "balance changes for both accounts when transferred amount is less than the balance limit for both accounts"() {
        setup:
        User transferer = user
        User transferee = new User(userName: "Test2", password: "Test").save(flush: true)

        when:
        transactionService.transferAmount(transferer.account, transferee.account, transactionAmount)

        then:
        transferer.account.balance == transfererBalance
        transferee.account.balance == transfereeBalance
        transferer.account.transactions.toList().size() == 1
        transferee.account.transactions.toList().size() == 1
        Transaction.list().size() == 2
        notThrown(RuntimeException)

        where:
        sno | transactionAmount | transfererBalance | transfereeBalance
        1   | 200               | 600               | 1000
        2   | 202.50            | 597.50            | 1002.50
        3   | 0                 | 800               | 800
    }

    //Exercise
    @Unroll('#sno ..transactionAmount : #transfererBalance, #transfereeBalance')
    def "exception is thrown when amount for transfer is more than 800 "() {
        setup:
        User transferer = user
        User transferee = new User(userName: "Test2", password: "Test").save(flush: true)

        when:
        transactionService.transferAmount(transferer.account, transferee.account, transactionAmount)

        then:
        transferer.account.balance == transfererBalance
        transferee.account.balance == transfereeBalance
        transferer.account.transactions == null
        transferee.account.transactions == null
        Transaction.count() == 0
        thrown(RuntimeException)
        where:
        sno | transactionAmount | transfererBalance | transfereeBalance
        1   | 800.1             | 800               | 800
        2   | 801               | 800               | 800
        3   | 1000              | 800               | 800
    }

    //Exercise
    @Unroll('#sno ..loanAmount')
    def "grant loan on the basis of available balance"() {
        when:
        ResponseDTO responseDTO = transactionService.grantLoan(user.account, loanAmount)

        then:
        user.account.balance == accountBalance
        Loan.count() == loanCount
        responseDTO.status == responseStatus
        responseDTO.message == responseMessage

        where:
        sno | loanAmount | responseStatus | responseMessage                              | accountBalance | loanCount
        1   | 799        | true           | "Loan granted"                               | 1599           | 1
        2   | 800        | false          | "Loan denied due to lack of account balance" | 800            | 0
        3   | 801        | false          | "Loan denied due to lack of account balance" | 800            | 0
        4   | 1          | true           | "Loan granted"                               | 801            | 1
        5   | 500        | true           | "Loan granted"                               | 1300           | 1
    }


}