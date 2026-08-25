package kimspring.splearn.domain.course;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import kimspring.splearn.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true, exclude = {})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseDetail extends AbstractEntity {
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime publishedAt;

    private LocalDateTime archivedAt;

    CourseDetail(String description) {
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    void publish() {
        this.publishedAt = LocalDateTime.now();
    }

    void archive() {
        this.archivedAt = LocalDateTime.now();
    }

    void updateInfo(CourseUpdateInfo updateInfo) {
        this.description = updateInfo.description();
    }
}
