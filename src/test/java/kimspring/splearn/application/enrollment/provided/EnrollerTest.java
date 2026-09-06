package kimspring.splearn.application.enrollment.provided;


import org.junit.jupiter.api.Test;

import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.enrollment.Enrollment;
import kimspring.splearn.domain.enrollment.EnrollmentStatus;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.support.stereotype.ApplicationService;
import kimspring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationService
@RequiredArgsConstructor
class EnrollerTest extends BaseApplicationServiceTest {
    final Enroller enroller;

    @Test
    void enroll() {
        Member member = prepareActiveMember();
        Course course = preparePublishedCourse();

        Enrollment enrollment = enroller.enroll(new EnrollRequest((member.getId()), course.getId()));

        assertThat(enrollment.getId()).isNotNull();
    }

    @Test
    void startStudying() {
        prepareEnrollment();

        Enrollment enrollmentStudying = enroller.startStudying(enrollment.getId());

        assertThat(enrollmentStudying.getStatus()).isEqualTo(EnrollmentStatus.STUDYING);
    }

    @Test
    void complete() {
        prepareEnrollment();
        enroller.startStudying(enrollment.getId());

        Enrollment enrollmentComplete = enroller.complete(enrollment.getId());

        assertThat(enrollmentComplete.getStatus()).isEqualTo(EnrollmentStatus.COMPLETED);
    }
}