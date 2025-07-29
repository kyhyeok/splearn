package kimspring.splearn.application.required;

import jakarta.persistence.EntityManager;
import kimspring.splearn.domain.Member;
import kimspring.splearn.domain.MemberFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static kimspring.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static kimspring.splearn.domain.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void registerMember() {
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

        assertThat(member.getId()).isNull();

        memberRepository.save(member);

        assertThat(member.getId()).isNotNull();

        entityManager.flush();
    }

    @Test
    void duplicateEmailFail() {
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        memberRepository.save(member);

        Member member2 = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}