package challenge.controllers;

import challenge.domain.ResponseStatusDTO;
import challenge.domain.exception.DuplicateTransactionException;
import challenge.domain.exception.ParentTransactionNotFoundException;
import challenge.domain.exception.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateTransactionException.class)
    public ResponseEntity handleDuplicateTransaction()  {
        return ResponseEntity.badRequest().body(new ResponseStatusDTO("duplicate transaction id"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParentTransactionNotFoundException.class)
    public ResponseEntity handleParentTransactionNotFound()  {
        return ResponseEntity.badRequest().body(new ResponseStatusDTO("parent transaction not found"));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TransactionNotFoundException.class)
    public void handleTransactionNotFound()  {
    }
}
