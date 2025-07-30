package kimspring.splearn.application;

import jakarta.transaction.Transactional;
import kimspring.splearn.application.provided.MemberFinder;
import kimspring.splearn.application.required.MemberRepository;
import kimspring.splearn.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {
    private final MemberRepository memberRepository;

    @Override
    public Member find(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("회원을 찾을 수 없습니다. id: " + memberId));
    }
}
