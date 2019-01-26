package com.crossover.techtrial.service;

import com.crossover.techtrial.exception.TransactionException;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.TransactionRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final String BOOK_ALREADY_ISSUED = "Book already issued with transaction ";
    private static final String MEMBER_HAS_MANY_TRANSACTIONS = "Too many transactions present for member ";
    private static final String BOOK_ALREADY_RETURNED = "Book already returned";

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BookService bookService;

    @Autowired
    MemberService memberService;

    @Override
    public Transaction save(Transaction transaction) throws TransactionException {
        final Transaction presentTransaction = transactionRepository
            .findByBookAndDateOfReturn(transaction.getBook(), null);
        if (presentTransaction != null) {
            throw new TransactionException(HttpStatus.FORBIDDEN.value(),
                BOOK_ALREADY_ISSUED + presentTransaction.getId());
        }
        List<Transaction> previousTransactions = transactionRepository
            .findByMemberId(transaction.getMember().getId());
        if (!CollectionUtils.isEmpty(previousTransactions) && previousTransactions.size() >= 5) {
            throw new TransactionException(HttpStatus.FORBIDDEN.value(),
                MEMBER_HAS_MANY_TRANSACTIONS + transaction.getMember().getId());
        }
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findById(long transactionId) {
        return transactionRepository.findById(transactionId).get();
    }

    @Override
    public List<Transaction> findByMemberId(long memberId) {
        return transactionRepository.findByMemberId(memberId);
    }

    @Override
    public Transaction closeTransaction(Long transactionId) throws TransactionException {
        Transaction transaction = findById(transactionId);
        if (transaction != null && transaction.getDateOfReturn() != null) {
            throw new TransactionException(HttpStatus.FORBIDDEN.value(), BOOK_ALREADY_RETURNED);
        }
        transaction.setDateOfReturn(LocalDateTime.now());
        return transaction;
    }

    @Override
    public Transaction issueBook(Long bookId, Long memberId) throws TransactionException {

        final Transaction transaction = new Transaction();
        transaction.setBook(bookService.findById(bookId));
        transaction.setMember(memberService.findById(memberId));
        transaction.setDateOfIssue(LocalDateTime.now());
        return save(transaction);
    }
}
