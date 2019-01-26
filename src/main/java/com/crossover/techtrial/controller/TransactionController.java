/**
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.exception.TransactionException;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.service.BookService;
import com.crossover.techtrial.service.MemberService;
import com.crossover.techtrial.service.TransactionService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kshah
 */
@RestController
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    TransactionService transactionService;

    /*
     * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
     * Example Post Request :  { "bookId":1,"memberId":33 }
     */
    @PostMapping(path = "/api/transaction")
    public ResponseEntity<Transaction> issueBookToMember(@RequestBody Map<String, Long> params) {

        Long bookId = params.get("bookId");
        Long memberId = params.get("memberId");
        try {
            return ResponseEntity.ok().body(transactionService.issueBook(bookId, memberId));
        } catch (TransactionException exception) {
            logger.error(exception.getMessage());
            return ResponseEntity.status(exception.getStatus()).build();
        }
    }

    /*
     * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @PatchMapping(path = "/api/transaction/{transaction-id}/return")
    public ResponseEntity<Transaction> returnBookTransaction(
        @PathVariable(name = "transaction-id") Long transactionId) {
        try {
            return ResponseEntity.ok().body(transactionService.closeTransaction(transactionId));
        } catch (TransactionException exception) {
            logger.error(exception.getMessage());
            return ResponseEntity.status(exception.getStatus()).build();
        }
    }
}
