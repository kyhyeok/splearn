package kimspring.splearn.application.member.provided;

import jakarta.validation.Valid;
import kimspring.splearn.domain.member.Member;

/*
 * 회원 인증
 * - ACTIVE 상태인 히ㅗ원만 로그인할 수 있다
 */
public interface MemberAuthenticator {
  Member login(@Valid MemberLoginRequest loginRequest) throws LoginFailedException;
}
