package challenge.service;

import challenge.domain.TransactionDTO;

import java.util.List;

public interface TransactionService {

    public void save(long id, double amount, String type, Long parentId);
    public TransactionDTO get(Long transactionId);
    public List<Long> getByType(String type);
    public double getSum(Long transactionId);

}
