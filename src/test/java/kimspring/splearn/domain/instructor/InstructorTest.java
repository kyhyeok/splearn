package kimspring.splearn.domain.instructor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import kimspring.splearn.domain.member.Member;
import kimspring.splearn.domain.member.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstructorTest {

    @Test
    void apply() {
        Member member = MemberFixture.createActiveMember();

        Instructor instructor = Instructor.apply(member);

        assertThat(instructor.member).isEqualTo(member);
        assertThat(instructor.status).isEqualTo(InstructorStatus.PENDING);
    }

    @Test
    void applyFailedMemberNotActive() {
        Member member = MemberFixture.createMember(); // PENDING

        assertThatThrownBy(() -> Instructor.apply(member))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void approve() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        instructor.approve();

        assertThat(instructor.status).isEqualTo(InstructorStatus.ACTIVE);
    }

    @Test
    void approveFailed() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);
        instructor.approve();

        assertThatThrownBy(() -> instructor.approve())
                  .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void reject() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        instructor.reject();

        assertThat(instructor.status).isEqualTo(InstructorStatus.REJECTED);
    }

    @Test
    void rejectFailed() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);
        instructor.reject();

        assertThatThrownBy(() -> instructor.reject())
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isActive() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);
        assertThat(instructor.isActive()).isFalse();

        instructor.approve();
        assertThat(instructor.isActive()).isTrue();
    }

    @Test
    void ensureActive() {
        Member member = MemberFixture.createActiveMember();
        Instructor instructor = Instructor.apply(member);

        assertThatThrownBy(() -> instructor.ensureActive())
            .isInstanceOf(IllegalStateException.class);

        instructor.approve();

        instructor.ensureActive();
    }
}