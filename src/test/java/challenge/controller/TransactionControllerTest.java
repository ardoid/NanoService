package challenge.controller;

import challenge.controllers.ExceptionController;
import challenge.controllers.TransactionController;
import challenge.service.TransactionService;
import challenge.service.TransactionServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class TransactionControllerTest {
    private static final String transactionUrl = "/transactionservice/transaction/";
    private static final String typeUrl = "/transactionservice/types/";
    private static final String sumUrl = "/transactionservice/sum/";

    private MockMvc mockMvc;

    private TransactionService transactionService;

    private final String t1 = "{ \"amount\": 5000, \"type\": \"cars\" }";
    private final String t2 = "{ \"amount\": 10000, \"type\": \"shopping\" , \"parent_id\": \"10\" }";
    private final String t3 = "{ \"amount\": 3000, \"type\": \"shopping\" , \"parent_id\": \"11\" }";

    @Before
    public void setUp() throws Exception {
        transactionService = new TransactionServiceImpl();
        mockMvc = MockMvcBuilders.standaloneSetup(new TransactionController(transactionService))
                .setControllerAdvice(new ExceptionController())
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
        mockMvc.perform(MockMvcRequestBuilders.put(transactionUrl +"10").contentType(MediaType.APPLICATION_JSON)
                .content(t1)).andExpect(status().isOk());
    }

    @Test
    public void testShouldPutNewTransaction() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(transactionUrl +"2").contentType(MediaType.APPLICATION_JSON)
                .content(t2)).andExpect(status().isOk());
    }

    @Test
    public void testShouldGetNotFoundWhenTransactionNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(transactionUrl +"2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testShouldGetTransactionWhenExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(transactionUrl +"10").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("amount", is(5000.0)))
                .andExpect(jsonPath("type", is("cars")));
    }

    @Test
    public void testShouldGetByType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(transactionUrl +"11").contentType(MediaType.APPLICATION_JSON)
                .content(t2)).andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.put(transactionUrl +"12").contentType(MediaType.APPLICATION_JSON)
                .content(t3)).andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get(typeUrl +"shopping")).andExpect(status().isOk())
                .andExpect(content().string(is("[11,12]")));
    }

    @Test
    public void testShouldGetSum() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(transactionUrl +"11").contentType(MediaType.APPLICATION_JSON)
                .content(t2)).andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get(sumUrl +"10").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("sum", is(5000.0)));
        mockMvc.perform(MockMvcRequestBuilders.get(sumUrl +"11").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("sum", is(15000.0)));
    }
}
