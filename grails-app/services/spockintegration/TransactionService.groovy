package spockintegration

class TransactionService {

    static transactional = true

    Transaction saveTransaction(Account account, BigDecimal amount, TransactionType type) throws RuntimeException {
        if (account.hasBalance(amount)) {
            Transaction transaction = new Transaction(amount: amount, type: type, account: account)
            account.addToTransactions(transaction)
            return transaction
        }
        else {
            throw new RuntimeException("Not enough balance to make this transaction")
        }
    }
}
