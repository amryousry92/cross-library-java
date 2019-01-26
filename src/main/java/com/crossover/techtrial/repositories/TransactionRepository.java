/**
 * 
 */
package com.crossover.techtrial.repositories;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author crossover
 *
 */
@RestResource(exported = false)
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findByMemberId(long memberId);

    Transaction findByBookAndDateOfReturn(Book book, LocalDateTime localDateTime);
}
