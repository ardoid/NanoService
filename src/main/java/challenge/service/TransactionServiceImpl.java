package challenge.service;

import challenge.domain.Transaction;
import challenge.domain.TransactionDTO;
import challenge.domain.exception.DuplicateTransactionException;
import challenge.domain.exception.ParentTransactionNotFoundException;
import challenge.domain.exception.TransactionNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionServiceImpl implements TransactionService{
    private volatile HashMap<Long, Transaction> transactionTable = new LinkedHashMap<>();
    private volatile HashMap<String, List<Transaction>> typeTable = new LinkedHashMap<>();

    @Override
    public void save(long id, double amount, String type, Long parentId) {
        if(transactionTable.containsKey(id)) {
            throw new DuplicateTransactionException();
        }

        if(parentId != null && !transactionTable.containsKey(parentId)) {
            throw new ParentTransactionNotFoundException();
        }

        Transaction transaction = new Transaction(id, amount, type, parentId);
        transactionTable.put(id, transaction);

        if(typeTable.containsKey(type))  {
            typeTable.get(type).add(transaction);
        } else {
            List<Transaction> list = new LinkedList();
            list.add(transaction);
            typeTable.put(type, list);
        }
    }

    @Override
    /**
     * This service use a hash table to maintain reference to transaction id
     *  Complexity is O(1) for transaction id retrieval
     *
     */
    public TransactionDTO get(Long transactionId) {
        if(!transactionTable.containsKey(transactionId))  {
            throw new TransactionNotFoundException();
        }
        return new TransactionDTO(transactionTable.get(transactionId));
    }

    @Override
    /**
     * This service use second hash table to maintain reference to transaction type
     *  The resulting complexity is O(1) for type retrieval, with a little bit of extra memory space
     *  Here I don't just save the transaction id in which incur additional memory space, instead saving the reference
     *  to Transaction object already saved at Transaction Hash Table.
     *
     */
    public List<Long> getByType(String type) {
        if(!typeTable.containsKey(type))  {
            throw new TransactionNotFoundException();
        }
        return typeTable.get(type).stream().map(t -> t.getId()).collect(Collectors.toList());
    }

    @Override
    /**
     * Here transaction must be traced back using parent id sequentially
     * Hence the complexity is O(n)
     *
     */
    public double getSum(Long transactionId) {
        if(!transactionTable.containsKey(transactionId))  {
            throw new TransactionNotFoundException();
        }
        Transaction transaction = transactionTable.get(transactionId);
        Long parentId = transaction.getParentId();
        double sum = transaction.getAmount();
        while(parentId != null)  {
            Transaction nextTransaction = transactionTable.get(parentId);
            parentId = nextTransaction.getParentId();
            sum += nextTransaction.getAmount();
        }
        return sum;
    }
}
