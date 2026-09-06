package kimspring.splearn.domain.enrollment;

import jakarta.annotation.Nullable;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.member.Member;

import static kimspring.splearn.domain.course.CourseFixture.createPublishedCourse;
import static kimspring.splearn.domain.member.MemberFixture.createActiveMember;

public class EnrollmentFixture {
    public static Enrollment createEnrollment(@Nullable Member member, @Nullable Course course) {
        return Enrollment.enroll(member == null ? createActiveMember() : member,
            course == null ? createPublishedCourse() : course);
    }

    public static Enrollment createEnrollment() {
        return createEnrollment(null, null);
    }
}
