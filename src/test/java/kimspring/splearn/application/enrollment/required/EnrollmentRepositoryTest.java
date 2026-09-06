package kimspring.splearn.application.enrollment.required;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.enrollment.Enrollment;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.support.test.BaseRepositoryTest;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor
class EnrollmentRepositoryTest extends BaseRepositoryTest {
    final EnrollmentRepository enrollmentRepository;

    @Test
    void saveAndFIndId() {
        Member member = prepareActiveMember();
        Course course = preparePublishedCourse();

        Enrollment enroll = Enrollment.enroll(member, course);

        enroll = enrollmentRepository.save(enroll);

        assertThat(enroll.getId()).isNotNull();

        entityManager.flush();
        entityManager.clear();

        Enrollment found = enrollmentRepository.findById(enroll.getId()).orElseThrow();

        assertThat(found).isEqualTo(enroll);
    }

    @Test
    void findByMemberId() {
        Member member1 = prepareActiveMember();
        Member member2 = prepareActiveMember();

        Enrollment enrollment1_1 = prepareEnrollment(member1, preparePublishedCourse());
        Enrollment enrollment1_2 = prepareEnrollment(member1, preparePublishedCourse());
        Enrollment enrollment2 = prepareEnrollment(member2, preparePublishedCourse());

        List<Enrollment> enrollments1 = enrollmentRepository.findByMemberId(member1.getId());
        assertThat(enrollments1).hasSize(2).containsExactly(enrollment1_1, enrollment1_2);

        List<Enrollment> enrollments2 = enrollmentRepository.findByMemberId(member2.getId());
        assertThat(enrollments2).hasSize(1).containsExactly(enrollment2);
    }

    @Test
    void findByMemberIdAndCourseId() {
        Member member1 = prepareActiveMember();
        Member member2 = prepareActiveMember();

        Course course1 = preparePublishedCourse();
        Course course2 = preparePublishedCourse();

        Enrollment enrollment1 = prepareEnrollment(member1, course1);
        Enrollment enrollment2 = prepareEnrollment(member2, course2);

        assertThat(
            enrollmentRepository.findByMemberIdAndCourseId(member1.getId(), course1.getId()).orElseThrow()).isEqualTo(
            enrollment1);

        assertThat(
            enrollmentRepository.findByMemberIdAndCourseId(member2.getId(), course2.getId()).orElseThrow()).isEqualTo(
            enrollment2);

        assertThat(
            enrollmentRepository.findByMemberIdAndCourseId(member1.getId(), course2.getId()).isPresent()).isFalse();

    }
}