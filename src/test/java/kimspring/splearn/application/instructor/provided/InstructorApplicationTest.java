package kimspring.splearn.application.instructor.provided;

import org.junit.jupiter.api.Test;

import kimspring.splearn.application.instructor.required.InstructorRepository;
import kimspring.splearn.application.member.required.MemberRepository;
import kimspring.splearn.domain.instructor.Instructor;
import kimspring.splearn.domain.instructor.InstructorFixture;
import kimspring.splearn.domain.instructor.InstructorStatus;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.domain.member.MemberFixture;
import kimspring.splearn.support.stereotype.ApplicationServiceTest;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ApplicationServiceTest
@RequiredArgsConstructor
class InstructorApplicationTest {
    final InstructorApplication instructorApplication;
    final InstructorRepository instructorRepository;
    final MemberRepository memberRepository;

    @Test
    void apply() {
        Member member = MemberFixture.createActiveMember();
        memberRepository.save(member);

        Instructor instructor = instructorApplication.apply(InstructorFixture.createApplyRequest(member));

        assertThat(instructor).isNotNull();
        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.PENDING);

        instructorRepository.findById(instructor.getId()).orElseThrow();
    }

    @Test
    void duplicateApply() {
        Member member = MemberFixture.createActiveMember();
        memberRepository.save(member);

        instructorApplication.apply(InstructorFixture.createApplyRequest(member));

        assertThatThrownBy(
            () -> instructorApplication.apply(InstructorFixture.createApplyRequest(member))).isInstanceOf(
            DuplicationInstructorApplicationException.class);
    }

    @Test
    void approve() {
        Instructor instructor = instructorApplication.approve(preparePendingInstructor().getId());

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void reject() {
        Instructor instructor = instructorApplication.reject(preparePendingInstructor().getId());

        assertThat(instructor.getStatus()).isEqualTo(InstructorStatus.REJECTED);
    }

    private Instructor preparePendingInstructor() {
        Member member = MemberFixture.createActiveMember();
        memberRepository.save(member);
        return instructorApplication.apply(new InstructorApplyRequest(member.getId()));
    }

}