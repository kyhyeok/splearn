package kimspring.splearn.application.member;

import kimspring.splearn.application.member.provided.LoginFailedException;
import kimspring.splearn.application.member.provided.MemberAuthenticator;
import kimspring.splearn.application.member.provided.MemberLoginRequest;
import kimspring.splearn.application.member.required.MemberRepository;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.domain.member.PasswordEncoder;
import kimspring.splearn.domain.shared.Email;
import kimspring.splearn.support.stereotype.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class MemberAuthenticationService implements MemberAuthenticator {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member login(MemberLoginRequest loginRequest) throws LoginFailedException {
        Member member =
            memberRepository.findByEmail(new Email(loginRequest.email())).orElseThrow(LoginFailedException::new);

        if (!member.isActive()) {
            throw new LoginFailedException();
        }

        if (!member.verifyPassword(loginRequest.password(), passwordEncoder)) {
            throw new LoginFailedException();
        }

        return member;
    }
}
