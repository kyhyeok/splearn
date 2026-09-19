package kimspring.splearn.support.test;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import kimspring.splearn.application.course.provided.CourseCreator;
import kimspring.splearn.application.curriculum.provided.CurriculumFinder;
import kimspring.splearn.application.enrollment.provided.EnrollRequest;
import kimspring.splearn.application.enrollment.provided.Enroller;
import kimspring.splearn.application.instructor.provided.InstructorApplication;
import kimspring.splearn.application.member.provided.MemberRegister;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.course.CourseFixture;
import kimspring.splearn.domain.curriculum.Curriculum;
import kimspring.splearn.domain.enrollment.Enrollment;
import kimspring.splearn.domain.instructor.Instructor;
import kimspring.splearn.domain.instructor.InstructorFixture;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.domain.member.MemberFixture;
import kimspring.splearn.support.stereotype.ApplicationServiceTest;

@ApplicationServiceTest
public class BaseApplicationServiceTest {
    @Autowired
    MemberRegister memberRegister;

    @Autowired
    InstructorApplication instructorApplication;

    @Autowired
    CourseCreator courseCreator;

    @Autowired
    Enroller enroller;

	@Autowired
	CurriculumFinder curriculumFinder;

    protected Member member;

    protected Instructor instructor;

    protected Course course;

    protected Enrollment enrollment;

    protected Curriculum curriculum;


	protected Instructor prepareInstructor() {
        prepareActiveMember();

        this.instructor = instructorApplication.apply(InstructorFixture.createApplyRequest(member));
        this.instructor.approve();

        return this.instructor;
    }

    protected Member prepareActiveMember() {
        this.member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        this.member.activate();
        return this.member;
    }

    protected Course prepareCourse() {
        prepareInstructor();
        this.course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor.getId(), null));
        this.course.updateInfo(CourseFixture.createCourseInfoUpdateRequest(null).toInfo());

        return this.course;
    }

    protected Course preparePublishedCourse() {
        prepareCourse();

        this.course.submitForReview();
        this.course.publish();

        return this.course;
    }

    protected Enrollment prepareEnrollment() {
        Member member = prepareActiveMember();
        Course course = preparePublishedCourse();
        this.enrollment = enroller.enroll(new EnrollRequest(member.getId(), course.getId()));
        return this.enrollment;
    }

	protected Curriculum prepareCurriculumSectionsAndLessons(Course course) {
		Curriculum curriculum = curriculumFinder.findByCourse(course.getId());

		curriculum.addSection("S1");
		curriculum.addLesson(0, "L1");
		curriculum.addLesson(0, "L2");
		curriculum.addSection("S2");
		curriculum.addLesson(1, "L3");
		curriculum.addLesson(1, "L4");
		curriculum.addSection("S3");
		curriculum.addLesson(2, "L5");

		this.curriculum = curriculum;

		return this.curriculum;
	}

	protected Course prepareCourseWithCurriculum() {
		prepareCourse();
		prepareCurriculumSectionsAndLessons(course);

		return this.course;
	}
}
