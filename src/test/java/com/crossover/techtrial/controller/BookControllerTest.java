package com.crossover.techtrial.controller;

import static com.crossover.techtrial.controller.MemberControllerTest.getHttpEntity;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.service.BookService;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    static final String API_BOOK_URL = "/api/book/";
    private static final String VALID_HTTP_REGISTER_BOOK_REQUEST =
        "{\"title\": \"book 1\"}";

    @Mock
    private BookController bookController;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private BookService bookService;

    private final List<Long> booksToBeCleaned = new ArrayList<>();

    @Before
    public void setup() throws Exception {
        MockMvcBuilders.standaloneSetup(bookController).build();
        prepareBookTable();
    }

    @After
    public void cleanBooks() {
        for (long bookId : booksToBeCleaned) {
            bookService.deleteById(bookId);
        }
    }

    private void prepareBookTable() {
        final HttpEntity<Object> book = getHttpEntity(VALID_HTTP_REGISTER_BOOK_REQUEST);
        final ResponseEntity<Book> response = template.postForEntity(
            API_BOOK_URL, book, Book.class);
        booksToBeCleaned.add(response.getBody().getId());
    }

    @Test
    public void givenInitialBookWhenSaveBookIsCalledThenReturnValidBook() {
        // Act & Assert
        Assert.assertNotNull(booksToBeCleaned.get(booksToBeCleaned.size() - 1));
    }

    @Test
    public void givenBooksAddedWhenGetBooksIsCalledThenReturnAllBooks() {
        // Act & Assert
        final ResponseEntity<ArrayList> response = template
            .getForEntity(API_BOOK_URL, ArrayList.class);

        // Assert
        Assert.assertTrue(response.getBody().size() > 0);
    }

    @Test
    public void givenInitialBookWhenGetRideByIdRunsThenRetrieveCorrectBook() {
        // Act & Assert
        Assert.assertEquals(template
                .getForEntity(API_BOOK_URL + booksToBeCleaned.get(booksToBeCleaned.size() - 1),
                    Book.class).getBody().getId(),
            booksToBeCleaned.get(booksToBeCleaned.size() - 1));
    }
}
