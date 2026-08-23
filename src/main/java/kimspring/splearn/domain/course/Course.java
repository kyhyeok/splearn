package kimspring.splearn.domain.course;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import kimspring.splearn.domain.AbstractEntity;
import kimspring.splearn.domain.instructor.Instructor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true, exclude = {})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends AbstractEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    Instructor instructor;

    @Column(nullable = false, length = 100)
    String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    CourseStatus status;

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    CourseDetail detail;

    public Course(Instructor instructor, String title, @Nullable String description) {
        instructor.ensureActive();

        this.instructor = requireNonNull(instructor);
        this.title = requireNonNull(title);
        this.status = CourseStatus.DRAFT;

        this.detail = new CourseDetail(description);
    }

    public void submitForReview() {
        state(status == CourseStatus.DRAFT, "DRAFT 상태가 아닙니다.");

        this.status = CourseStatus.IN_REVIEW;
    }

    public void publish() {
        state(status == CourseStatus.IN_REVIEW, "IN_REVIEW 상태가 아닙니다.");

        this.status = CourseStatus.PUBLISHED;
        this.detail.publish();
    }

    public void archive() {
        state(status == CourseStatus.PUBLISHED, "PUBLISHED 상태가 아닙니다.");

        this.status = CourseStatus.ARCHIVED;
        this.detail.archive();
    }

    public boolean isPublished() {
        return status == CourseStatus.PUBLISHED;
    }

    public void ensurePublished() {
        state(status == CourseStatus.PUBLISHED, "PUBLISHED 상태가 아닙니다.");
    }
}
