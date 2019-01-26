/**
 *
 */
package com.crossover.techtrial.service;

import com.crossover.techtrial.exception.TransactionException;
import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.repositories.BookRepository;
import com.crossover.techtrial.repositories.TransactionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author crossover
 *
 */
@Service
public class BookServiceImpl implements BookService {

    private static final String BOOK_NOT_FOUND = "Book not found with id";

    @Autowired
    BookRepository bookRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Book> getAll() {
        List<Book> personList = new ArrayList<>();
        bookRepository.findAll().forEach(personList::add);
        return personList;

    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book findById(Long bookId) throws TransactionException {
        Optional<Book> dbPerson = bookRepository.findById(bookId);
        if (dbPerson == null) {
            throw new TransactionException(HttpStatus.NOT_FOUND.value(), BOOK_NOT_FOUND + bookId);
        }
        return dbPerson.orElse(null);
    }

}
