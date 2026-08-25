package kimspring.splearn.application.course.provided;

import jakarta.validation.Valid;
import kimspring.splearn.domain.course.Course;

/**
 * 강의를 준비하는 작업
 */
public interface CourseCreator {
    Course course(@Valid CourseCreateRequest createRequest);

    Course updateInfo(Long courseId, @Valid CourseInfoUpdateRequest infoUpdateRequest);
}
