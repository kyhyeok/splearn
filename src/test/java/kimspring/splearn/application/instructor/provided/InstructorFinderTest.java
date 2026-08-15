package kimspring.splearn.application.instructor.provided;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kimspring.splearn.application.member.provided.MemberRegister;
import kimspring.splearn.domain.instructor.Instructor;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.domain.member.MemberFixture;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
class InstructorFinderTest {
    final InstructorFinder instructorFinder;
    final InstructorApplication instructorApplication;
    final MemberRegister memberRegister;

    @Test
    void findByMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        member = memberRegister.activate(member.getId());

        Instructor instructor = instructorApplication.apply(new InstructorApplyRequest(member.getId()));

        Instructor found = instructorFinder.findByMember(member.getId()).orElseThrow();

        assertThat(instructor).isEqualTo(found);

        assertThat(instructorFinder.findByMember(Long.MAX_VALUE).isPresent()).isFalse();
    }
}