package kimspring.splearn.application.course.provided;

import jakarta.validation.constraints.Size;
import kimspring.splearn.domain.course.CourseUpdateInfo;

public record CourseInfoUpdateRequest(
    @Size(min = 2, max = 100) String title,
    @Size(max = 500) String description
) {
    public CourseUpdateInfo toInfo() {
        return new CourseUpdateInfo(title, description);
    }
}
