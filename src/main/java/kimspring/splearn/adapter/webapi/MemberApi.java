package kimspring.splearn.adapter.webapi;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import kimspring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import kimspring.splearn.application.member.provided.MemberRegister;
import kimspring.splearn.application.member.provided.MemberRegisterRequest;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.support.stereotype.WebApiAdapter;
import lombok.RequiredArgsConstructor;

@WebApiAdapter
@RequiredArgsConstructor
public class MemberApi {
    private final MemberRegister memberRegister;

    @PostMapping("/api/members")
    public MemberRegisterResponse register(@RequestBody @Valid MemberRegisterRequest request) {
        Member member = memberRegister.register(request);

        return MemberRegisterResponse.of(member);
    }
}
