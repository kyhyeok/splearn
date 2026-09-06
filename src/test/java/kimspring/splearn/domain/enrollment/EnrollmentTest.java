package kimspring.splearn.domain.enrollment;

import org.junit.jupiter.api.Test;

import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.course.CourseFixture;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.domain.member.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnrollmentTest {

    @Test
    void enroll() {
        Member member = MemberFixture.createActiveMember();
        Course course = CourseFixture.createPublishedCourse();

        Enrollment enroll = Enrollment.enroll(member, course);

        assertThat(enroll.getStatus()).isEqualTo(EnrollmentStatus.ENROLLED);
        assertThat(enroll.getEnrolledAt()).isNotNull();
    }

    @Test
    void enrollFailNotPublishedCourse() {
        Member member = MemberFixture.createActiveMember();
        Course course = CourseFixture.createCourse();

        assertThatThrownBy(() -> Enrollment.enroll(member, course)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void startStudying() {
        Enrollment enrollment = EnrollmentFixture.createEnrollment();

        enrollment.startStudying();

        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.STUDYING);

        assertThatThrownBy(() -> enrollment.startStudying()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void complete() {
        Enrollment enrollment = EnrollmentFixture.createEnrollment();
        enrollment.startStudying();

        enrollment.complete();

        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
        assertThat(enrollment.getCompletedAt()).isNotNull();

        assertThatThrownBy(() -> enrollment.complete()).isInstanceOf(IllegalStateException.class);
    }
}