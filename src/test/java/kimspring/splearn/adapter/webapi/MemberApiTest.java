package kimspring.splearn.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.io.UnsupportedEncodingException;

import kimspring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import kimspring.splearn.application.member.provided.MemberRegister;
import kimspring.splearn.application.member.provided.MemberRegisterRequest;
import kimspring.splearn.application.member.required.MemberRepository;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.domain.member.MemberFixture;
import kimspring.splearn.domain.member.MemberStatus;
import kimspring.splearn.support.stereotype.WebApiAdapterTest;
import lombok.RequiredArgsConstructor;

import static kimspring.splearn.AssertThatUtils.equalsTo;
import static kimspring.splearn.AssertThatUtils.notNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebApiAdapterTest
@RequiredArgsConstructor
class MemberApiTest {
    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;
    final MemberRepository memberRepository;
    final MemberRegister memberRegister;

    @Test
    void register() throws JsonProcessingException, UnsupportedEncodingException {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);


        MvcTestResult result = mvcTester.post()
                                        .uri("/api/members")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestJson)
                                        .exchange();

        assertThat(result).hasStatusOk()
                          .bodyJson()
                          .hasPathSatisfying("$.memberId", notNull())
                          .hasPathSatisfying("$.email", equalsTo(request));

        MemberRegisterResponse response =
            objectMapper.readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);

        Member foundMember = memberRepository.findById(response.memberId()).orElseThrow();

        assertThat(foundMember.getEmail().address()).isEqualTo(request.email());
        assertThat(foundMember.getNickname()).isEqualTo(request.nickname());
        assertThat(foundMember.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmail() throws JsonProcessingException {
        MemberRegisterRequest memberRegisterRequest = MemberFixture.createMemberRegisterRequest();
        memberRegister.register(memberRegisterRequest);

        String requestJson = objectMapper.writeValueAsString(memberRegisterRequest);

        MvcTestResult result = mvcTester.post()
                                        .uri("/api/members")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(requestJson)
                                        .exchange();

        assertThat(result).apply(print()).hasStatus(HttpStatus.CONFLICT);
    }
}
