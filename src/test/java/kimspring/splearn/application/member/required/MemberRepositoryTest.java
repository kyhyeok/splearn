package kimspring.splearn.application.member.required;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.persistence.EntityManager;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.domain.member.MemberStatus;
import lombok.RequiredArgsConstructor;

import static kimspring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static kimspring.splearn.domain.member.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@RequiredArgsConstructor
class MemberRepositoryTest {
    final MemberRepository memberRepository;

    final EntityManager entityManager;

    @Test
    void registerMember() {
        Member member = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());

        assertThat(member.getId()).isNull();

        memberRepository.save(member);

        assertThat(member.getId()).isNotNull();

        entityManager.flush();
        entityManager.clear();

        var found = memberRepository.findById(member.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(found.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void duplicateEmailFail() {
        Member member = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());
        memberRepository.save(member);

        Member member2 = Member.register(createMemberRegisterRequest().toInfo(), createPasswordEncoder());
        assertThatThrownBy(() -> memberRepository.save(member2)).isInstanceOf(DataIntegrityViolationException.class);
    }
}