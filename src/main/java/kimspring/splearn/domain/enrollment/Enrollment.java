package kimspring.splearn.domain.enrollment;

import org.hibernate.annotations.NaturalId;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import kimspring.splearn.domain.AbstractEntity;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true, exclude = {"member", "course"})
@NoArgsConstructor
public class Enrollment extends AbstractEntity {
    @NaturalId
    @ManyToOne
    private Member member;

    @NaturalId
    @ManyToOne
    private Course course;

    private EnrollmentStatus status;

    private LocalDateTime enrolledAt;

    private LocalDateTime completedAt;

    public static Enrollment enroll(Member member, Course course) {
        member.ensureActive();
        course.ensurePublished();

        Enrollment enrollment = new Enrollment();
        enrollment.member = member;
        enrollment.course = course;
        enrollment.status = EnrollmentStatus.ENROLLED;
        enrollment.enrolledAt = LocalDateTime.now();

        return enrollment;
    }

    public void startStudying() {
        state(status == EnrollmentStatus.ENROLLED, "수강 상태가 ENROLLED가 아닙니다");

        this.status = EnrollmentStatus.STUDYING;
    }

    public void complete() {
        state(status == EnrollmentStatus.STUDYING, "수강 상태가 STUDYING이 아닙니다");

        this.status = EnrollmentStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
}
