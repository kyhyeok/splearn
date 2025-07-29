package kimspring.splearn.application.provided;

import jakarta.transaction.Transactional;
import kimspring.splearn.SplearnTestConfiguration;
import kimspring.splearn.domain.DuplicateEmailException;
import kimspring.splearn.domain.Member;
import kimspring.splearn.domain.MemberFixture;
import kimspring.splearn.domain.MemberStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
@Import(SplearnTestConfiguration.class)
public record MemberRegisterTest(MemberRegister memberRegister) {
    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail() {
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }
}
