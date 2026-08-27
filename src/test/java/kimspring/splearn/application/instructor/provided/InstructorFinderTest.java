package kimspring.splearn.application.instructor.provided;

import org.junit.jupiter.api.Test;

import kimspring.splearn.application.member.provided.MemberRegister;
import kimspring.splearn.domain.instructor.Instructor;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.domain.member.MemberFixture;
import kimspring.splearn.support.stereotype.ApplicationServiceTest;
import kimspring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationServiceTest
@RequiredArgsConstructor
class InstructorFinderTest extends BaseApplicationServiceTest {
    final InstructorFinder instructorFinder;
    final InstructorApplication instructorApplication;

    @Test
    void findByMember() {
        prepareMember();

        Instructor instructor = instructorApplication.apply(new InstructorApplyRequest(member.getId()));

        Instructor found = instructorFinder.findByMember(member.getId()).orElseThrow();

        assertThat(instructor).isEqualTo(found);

        assertThat(instructorFinder.findByMember(Long.MAX_VALUE).isPresent()).isFalse();
    }
}