package kimspring.splearn.application.course.provided;

import jakarta.validation.Valid;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.support.exception.ValidationException;

/**
 * 강의를 준비하는 작업
 */
public interface CourseCreator {
    Course create(@Valid CourseCreateRequest createRequest) throws ValidationException;

    Course updateInfo(Long courseId, @Valid CourseInfoUpdateRequest infoUpdateRequest) throws ValidationException;
}
