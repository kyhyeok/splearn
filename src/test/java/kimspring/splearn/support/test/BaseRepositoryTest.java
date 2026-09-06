package kimspring.splearn.support.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import kimspring.splearn.application.course.required.CourseRepository;
import kimspring.splearn.application.enrollment.required.EnrollmentRepository;
import kimspring.splearn.application.instructor.required.InstructorRepository;
import kimspring.splearn.application.member.required.MemberRepository;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.course.CourseFixture;
import kimspring.splearn.domain.enrollment.Enrollment;
import kimspring.splearn.domain.enrollment.EnrollmentFixture;
import kimspring.splearn.domain.instructor.Instructor;
import kimspring.splearn.domain.instructor.InstructorFixture;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.domain.member.MemberFixture;

@DataJpaTest
public class BaseRepositoryTest {
    @Autowired
    protected EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    protected Member member;

    protected Instructor instructor;

    protected Course course;

    protected Enrollment enrollment;

    protected Course preparePublishedCourse() {
        prepareActiveInstructor();

        this.course = courseRepository.save(CourseFixture.createCourse(this.instructor, null));
        this.course.updateInfo(CourseFixture.createCourseInfoUpdateRequest(null).toInfo());
        this.course.submitForReview();
        this.course.publish();

        return this.course;
    }

    protected Instructor prepareActiveInstructor() {
        prepareActiveMember();

        this.instructor = instructorRepository.save(InstructorFixture.createActiveInstructor(member));

        return this.instructor;
    }

    protected Member prepareActiveMember() {
        this.member = memberRepository.save(MemberFixture.createActiveMember());

        return this.member;
    }

    protected Enrollment prepareEnrollment(Member member, Course course) {
        this.enrollment = enrollmentRepository.save(EnrollmentFixture.createEnrollment(member, course));

        return this.enrollment;
    }
}
