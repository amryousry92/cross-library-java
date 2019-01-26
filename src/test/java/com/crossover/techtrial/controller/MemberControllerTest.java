/**
 *
 */
package com.crossover.techtrial.controller;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author kshah
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

    private static final String VALID_HTTP_REGISTER_REQUEST =
        "{\"name\": \"test 1\", \"email\": \"MEMBER_EMAIL\","
            + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }";

    private static final String INVALID_HTTP_REGISTER_REQUEST =
        "{\"name\": \"1test 1\", \"email\": \"test10000000000001@gmail.com\","
            + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }";

    private static final String MEMBER_EMAIL_REGEX = "MEMBER_EMAIL";

    static final String API_MEMBER_URL = "/api/member/";

    private MockMvc mockMvc;

    @Mock
    private MemberController memberController;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private MemberService memberService;

    private final List<Long> membersToBeCleaned = new ArrayList<>();

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        prepareMembersTable(0);
    }

    @After
    public void cleanMembersList() {
        for (Long memberId : membersToBeCleaned) {
            memberService.deleteById(memberId);
        }
    }

    private void prepareMembersTable(int counter) {
        final HttpEntity<Object> member = getHttpEntity(VALID_HTTP_REGISTER_REQUEST
            .replace(MEMBER_EMAIL_REGEX, MEMBER_EMAIL_REGEX + Math.random()));
        final ResponseEntity<Member> response = template.postForEntity(
            API_MEMBER_URL, member, Member.class);
        if (response.getStatusCode().value() == HttpStatus.OK.value()) {
            membersToBeCleaned.add(response.getBody().getId());
        } else if (counter < 5) {
            prepareMembersTable(++counter);
        } else {
            Assert.fail("Can not create members for testing");
        }
    }

    @Test
    public void testMemberRegsitrationsuccessful() throws Exception {
        final HttpEntity<Object> member = getHttpEntity(VALID_HTTP_REGISTER_REQUEST);

        final ResponseEntity<Member> response = template.postForEntity(
            API_MEMBER_URL, member, Member.class);

        Assert.assertEquals("test 1", response.getBody().getName());
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());

        //cleanup the user
        memberService.deleteById(response.getBody().getId());
    }

    static HttpEntity<Object> getHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    @Test
    public void givenFalseMemberNameWhenRegisterIsCalledThenFail() {
        // Arrange
        final HttpEntity<Object> member = getHttpEntity(INVALID_HTTP_REGISTER_REQUEST);

        // Act
        final ResponseEntity<Member> response = template.postForEntity(
            API_MEMBER_URL, member, Member.class);

        // Assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
    }

    @Test
    public void whenGetAllRunsThenReturnMembersList() {
        // Act & Assert
        final ResponseEntity<ArrayList> response = template
            .getForEntity(API_MEMBER_URL, ArrayList.class);

        // Assert
        Assert.assertTrue(response.getBody().size() > 0);
    }

    @Test
    public void givenInitialMemberWhenGetMemberByIdThenReturnMember() {
        // Act && Assert
        long id = membersToBeCleaned.get(membersToBeCleaned.size() - 1);
        ResponseEntity<Member> member = memberController.getMemberById(id);
        Assert.assertEquals(membersToBeCleaned.get(membersToBeCleaned.size() - 1), template
            .getForEntity(API_MEMBER_URL + id, Member.class).getBody().getId());
//        Assert.assertEquals(member.getBody().getId(), membersToBeCleaned.get(membersToBeCleaned.size() - 1));
    }
}
