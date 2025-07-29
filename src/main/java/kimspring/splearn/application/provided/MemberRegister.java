package kimspring.splearn.application.provided;

import kimspring.splearn.domain.Member;
import kimspring.splearn.domain.MemberRegisterRequest;

/**
 * 회원의 등록과 돤련된 기능을 제공한다
 */
public interface MemberRegister {
    Member register(MemberRegisterRequest registerRequest);
}
