package kimspring.splearn.domain.instructor;

import jakarta.validation.Valid;
import kimspring.splearn.application.instructor.provided.InstructorApplyRequest;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.domain.member.MemberFixture;

public class InstructorFixture {
    public static Instructor createInstructor(Member member) {
        return Instructor.apply(member);
    }

    public static Instructor createInstructor() {
        return createInstructor(MemberFixture.createActiveMember());
    }

    public static Instructor createActiveInstructor() {
        Instructor instructor = createInstructor();
        instructor.approve();
        return instructor;
    }

    public static @Valid InstructorApplyRequest createApplyRequest(Member member) {
        return new InstructorApplyRequest(member.getId());
    }
}
