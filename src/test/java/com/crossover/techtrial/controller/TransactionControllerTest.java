package com.crossover.techtrial.controller;

import static com.crossover.techtrial.controller.MemberControllerTest.API_MEMBER_URL;
import static com.crossover.techtrial.controller.MemberControllerTest.getHttpEntity;

import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.service.MemberService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {

    private static final String VALID_HTTP_REGISTER_REQUEST =
        "{\"name\": \"name\", \"email\": \"MEMBER_EMAIL\","
            + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }";
    private static final String MEMBER_EMAIL_REGEX = "MEMBER_EMAIL";

    @Mock
    private TransactionController controller;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private MemberService memberService;

    private final List<Long> membersToBeCleaned = new ArrayList<>();

    @Before
    public void setup() throws Exception {
        MockMvcBuilders.standaloneSetup(controller).build();
        prepareDatabase(0);
    }

    @After
    public void finalize() {
        for (Long memberId : membersToBeCleaned) {
            memberService.deleteById(memberId);
        }
    }

    private void prepareDatabase(int counter) {
        final HttpEntity<Object> member = getHttpEntity(VALID_HTTP_REGISTER_REQUEST
            .replace(MEMBER_EMAIL_REGEX, MEMBER_EMAIL_REGEX + Math.random()));
        final ResponseEntity<Member> response = template.postForEntity(
            API_MEMBER_URL, member, Member.class);
        if (response.getStatusCode().value() == HttpStatus.OK.value()) {
            membersToBeCleaned.add(response.getBody().getId());
        } else if (counter < 5) {
            prepareDatabase(++counter);
        } else {
            Assert.fail("Can not create members for testing");
        }
    }

    @Test
    public void givenValidParametersWhenIssueBookToMemberIsCalledThenReturnHttpStatusOk() {
        // Arrange

        // Act

        // Assert

    }
}
