/**
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.service.MemberService;
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
        "{\"name\": \"test 1\", \"email\": \"test10000000000001@gmail.com\","
            + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }";

    private static final String INVALID_HTTP_REGISTER_REQUEST =
        "{\"name\": \"1test 1\", \"email\": \"test10000000000001@gmail.com\","
            + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }";
    private static final String API_MEMBER_URL = "/api/member";

    private MockMvc mockMvc;

    @Mock
    private MemberController memberController;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    MemberService memberService;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
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

    private HttpEntity<Object> getHttpEntity(Object body) {
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
}
