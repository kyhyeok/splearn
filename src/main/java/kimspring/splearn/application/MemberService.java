package kimspring.splearn.application;

import kimspring.splearn.application.provided.MemberRegister;
import kimspring.splearn.application.required.EmailSender;
import kimspring.splearn.application.required.MemberRepository;
import kimspring.splearn.domain.Member;
import kimspring.splearn.domain.MemberRegisterRequest;
import kimspring.splearn.domain.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        // check
        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클릭해서 등록을 완료해주세요.");

        return member;
    }
}
