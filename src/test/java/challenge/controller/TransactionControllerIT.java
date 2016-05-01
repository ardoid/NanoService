package challenge.controller;

import challenge.Application;
import challenge.domain.ResponseSumDTO;
import challenge.domain.Transaction;
import challenge.domain.TransactionDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class TransactionControllerIT {
    @Value("${local.server.port}")
    private int port;
    private URL transactionUrl, typeUrl, sumUrl;
    private RestTemplate restTemplate;
    private Transaction transaction;

    @Before
    public void setUp() throws MalformedURLException {
        transactionUrl = new URL("http://localhost:"+ port + "/transactionservice/transaction/");
        typeUrl = new URL("http://localhost:"+ port + "/transactionservice/types/");
        sumUrl = new URL("http://localhost:"+ port + "/transactionservice/sum/");
        restTemplate = new TestRestTemplate();
        transaction = new Transaction(1,100.0,"car",null);
    }

    @Test
    public void testShouldPutAndGetTransactionById()  {
        restTemplate.put(transactionUrl.toString()+"1", new TransactionDTO(transaction));
        ResponseEntity<TransactionDTO> responseEntity = restTemplate.getForEntity(transactionUrl.toString()+"1", TransactionDTO.class);
        assertEquals(100.0, responseEntity.getBody().getAmount(), 0.0);
        assertEquals("car", responseEntity.getBody().getType());
    }

    @Test
    public void testShouldGetTransactionByType()  {
        restTemplate.put(transactionUrl.toString()+"1", new TransactionDTO(transaction));
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(typeUrl.toString()+"car", String.class);
        assertEquals("[1]", responseEntity.getBody());
    }

    @Test
    public void testShouldGetTransactionSum()  {
        restTemplate.put(transactionUrl.toString()+"1", new TransactionDTO(transaction));
        transaction = new Transaction(2,200.0,"car",1L);
        restTemplate.put(transactionUrl.toString()+"2", new TransactionDTO(transaction));
        ResponseEntity<ResponseSumDTO> responseEntity = restTemplate.getForEntity(sumUrl.toString()+"2", ResponseSumDTO.class);
        assertEquals(300.0, responseEntity.getBody().getSum(), 0.0);
    }
}
