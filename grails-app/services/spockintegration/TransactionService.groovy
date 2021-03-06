package spockintegration

class TransactionService {

    static transactional = true

    Transaction saveTransaction(Account account, BigDecimal amount, TransactionType type) throws RuntimeException {
        if (account.hasBalance(amount)) {
            Transaction transaction = new Transaction(amount: amount, type: type, account: account).save(flush: true)
            account.addToTransactions(transaction)
            if (type == TransactionType.Dr) {
                account.balance = account.balance - amount
            }
            else {
                account.balance = account.balance + amount
            }
            return transaction
        }
        else {
            throw new RuntimeException("Not enough balance to make this transaction")
        }
    }

    Transaction transferAmount(Account transferer, Account transferee, BigDecimal amount) {
        saveTransaction(transferer, amount, TransactionType.Dr)
        saveTransaction(transferee, amount, TransactionType.Cr)
    }

    ResponseDTO grantLoan(Account applier, BigDecimal loanAmount) {
        ResponseDTO responseDTO
        if (applier.hasBalance(loanAmount)) {
            Transaction loanTransaction = saveTransaction(applier, loanAmount, TransactionType.Cr)
            new Loan(transaction: loanTransaction).save(flush: true)
            responseDTO = new ResponseDTO(status: true, message: "Loan granted")
        } else {
            responseDTO = new ResponseDTO(status: false, message: "Loan denied due to lack of account balance")
        }
        responseDTO
    }
}
