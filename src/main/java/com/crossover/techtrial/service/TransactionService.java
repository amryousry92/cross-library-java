package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Transaction;
import java.util.List;
import java.util.Map;

public interface TransactionService {

    Transaction save(Transaction transaction);

    Transaction findById(long transactionId);

    List<Transaction> findByMemberId(long memberId);
}
