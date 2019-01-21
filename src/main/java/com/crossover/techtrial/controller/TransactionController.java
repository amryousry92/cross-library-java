/**
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.service.BookService;
import com.crossover.techtrial.service.MemberService;
import com.crossover.techtrial.service.TransactionService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
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

    @Autowired
    TransactionService transactionService;

    @Autowired
    BookService bookService;

    @Autowired
    MemberService memberService;

    /*
     * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
     * Example Post Request :  { "bookId":1,"memberId":33 }
     */
    @PostMapping(path = "/api/transaction")
    public ResponseEntity<Transaction> issueBookToMember(@RequestBody Map<String, Long> params) {

        Long bookId = params.get("bookId");
        Long memberId = params.get("memberId");
        List<Transaction> previousTransactions = transactionService.findByMemberId(memberId);
        if (!CollectionUtils.isEmpty(previousTransactions) && previousTransactions.size() >= 5) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Transaction transaction = new Transaction();
        transaction.setBook(bookService.findById(bookId));
        transaction.setMember(memberService.findById(memberId));
        transaction.setDateOfIssue(LocalDateTime.now());
        return ResponseEntity.ok().body(transactionService.save(transaction));
    }

    /*
     * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @PatchMapping(path = "/api/transaction/{transaction-id}/return")
    public ResponseEntity<Transaction> returnBookTransaction(
        @PathVariable(name = "transaction-id") Long transactionId) {
        Transaction transaction = transactionService.findById(transactionId);
        if (transaction != null && transaction.getDateOfReturn() != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        transaction.setDateOfReturn(LocalDateTime.now());
        return ResponseEntity.ok().body(transaction);
    }

}
