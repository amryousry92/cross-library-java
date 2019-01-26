package com.crossover.techtrial.service;

import com.crossover.techtrial.exception.TransactionException;
import com.crossover.techtrial.model.Transaction;
import java.util.List;

public interface TransactionService {

    Transaction save(Transaction transaction) throws TransactionException;

    Transaction findById(long transactionId);

    List<Transaction> findByMemberId(long memberId);

    Transaction closeTransaction(Long transactionId) throws TransactionException;

    Transaction issueBook(Long bookId, Long memberId) throws TransactionException;
}
