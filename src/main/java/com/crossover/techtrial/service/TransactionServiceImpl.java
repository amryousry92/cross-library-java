package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.TransactionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
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
}
