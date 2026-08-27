package kimspring.splearn.support.test;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import kimspring.splearn.application.instructor.provided.InstructorApplication;
import kimspring.splearn.application.member.provided.MemberRegister;
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

    protected Member member;
    protected Instructor instructor;

    @NonNull
    protected Instructor prepareInstructor() {
        this.member = prepareMember();

        this.instructor = instructorApplication.apply(InstructorFixture.createApplyRequest(member));
        this.instructor.approve();

        return this.instructor;
    }

    protected @NonNull Member prepareMember() {
        this.member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        this.member.activate();
        return this.member;
    }
}
