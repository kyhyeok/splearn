package kimspring.splearn.domain.enrollment;

import org.hibernate.annotations.NaturalId;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kimspring.splearn.domain.AbstractEntity;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.FetchType.LAZY;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true, exclude = {"member", "course"})
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_ENROLLMENT_MEMBER_COURSE", columnNames = {"member_id", "course_id"}))
public class Enrollment extends AbstractEntity {
    @NaturalId
    @ManyToOne(optional = false, fetch = LAZY)
    private Member member;

    @NaturalId
    @ManyToOne(optional = false, fetch = LAZY)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status;

    @Column(nullable = false)
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
