package challenge.controllers;

import challenge.domain.ResponseStatusDTO;
import challenge.domain.ResponseSumDTO;
import challenge.domain.TransactionDTO;
import challenge.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactionservice")
public class TransactionController {

    TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.PUT)
    public ResponseEntity putTransaction(@RequestBody TransactionDTO transaction, @PathVariable Long transactionId)  {
        transactionService.save(transactionId, transaction.getAmount(), transaction.getType(), transaction.getParent_id());
        return ResponseEntity.ok(new ResponseStatusDTO("ok"));
    }

    @RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.GET)
    public @ResponseBody TransactionDTO getTransaction(@PathVariable Long transactionId)  {
        return transactionService.get(transactionId);
    }

    @RequestMapping(value = "/types/{type}", method = RequestMethod.GET)
    public @ResponseBody List<Long> getType(@PathVariable String type)  {
        return transactionService.getByType(type);
    }

    @RequestMapping(value = "/sum/{transactionId}", method = RequestMethod.GET)
    public @ResponseBody ResponseSumDTO getSum(@PathVariable Long transactionId)  {
        return new ResponseSumDTO(transactionService.getSum(transactionId));
    }
}
