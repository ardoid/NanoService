package challenge.service;

import challenge.domain.exception.DuplicateTransactionException;
import challenge.domain.exception.ParentTransactionNotFoundException;
import challenge.domain.exception.TransactionNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TransactionServiceTest {
    TransactionService transactionService;

    @Before
    public void setUp()  {
        transactionService = new TransactionServiceImpl();
    }

    @Test
    public void testShouldAddTransaction() {
        transactionService.save(1, 100.0, "car", null);
        assertEquals(100.0, transactionService.get(1L).getAmount(), 0.0);
    }

    @Test(expected = DuplicateTransactionException.class)
    public void testShouldNotAddDuplicateTransaction() {
        transactionService.save(1, 100.0, "car", null);
        transactionService.save(1, 100.0, "car", null);
    }

    @Test
    public void testShouldAddTransactionWithParentWhenParentExist() {
        transactionService.save(1, 100.0, "car", null);
        transactionService.save(2, 200.0, "car", 1L);
        assertEquals(200.0, transactionService.get(2L).getAmount(), 0.0);
    }

    @Test(expected = ParentTransactionNotFoundException.class)
    public void testShouldNotAddTransactionWithParentWhenParentNotExist() {
        transactionService.save(2, 200.0, "car", 1L);
    }

    @Test(expected = TransactionNotFoundException.class)
    public void testShouldThrowExceptionWhenTransactionNotFound() {
        transactionService.get(1L);
    }

    @Test
    public void testShouldReturnTransactionByType()  {
        transactionService.save(1, 100.0, "car", null);
        transactionService.save(2, 200.0, "car", 1L);
        List<Long> transactionList = transactionService.getByType("car");
        assertEquals(transactionList.size(), 2);
        assertEquals(1,transactionList.get(0).longValue());
        assertEquals(2,transactionList.get(1).longValue());
    }

    @Test
    public void testShouldReturnSumOfConnectedTransactions()  {
        transactionService.save(1, 100.0, "car", null);
        transactionService.save(2, 200.0, "bike", 1L);
        transactionService.save(3, 300.0, "bike", 2L);
        assertEquals(600.0, transactionService.getSum(3L), 0.0);
    }
}
