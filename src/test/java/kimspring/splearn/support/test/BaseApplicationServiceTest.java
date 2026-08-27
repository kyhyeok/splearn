package kimspring.splearn.support.test;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import kimspring.splearn.application.course.provided.CourseCreator;
import kimspring.splearn.application.instructor.provided.InstructorApplication;
import kimspring.splearn.application.member.provided.MemberRegister;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.course.CourseFixture;
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

    protected Member member;

    protected Instructor instructor;

    protected Course course;

    @NonNull
    protected Instructor prepareInstructor() {
        prepareMember();

        this.instructor = instructorApplication.apply(InstructorFixture.createApplyRequest(member));
        this.instructor.approve();

        return this.instructor;
    }

    protected @NonNull Member prepareMember() {
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
}
