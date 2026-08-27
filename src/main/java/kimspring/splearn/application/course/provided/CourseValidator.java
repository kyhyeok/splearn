package kimspring.splearn.application.course.provided;

import kimspring.splearn.domain.instructor.Instructor;
import kimspring.splearn.support.exception.ValidationException;

public interface CourseValidator {
    void validateForCreate(Instructor instructor, CourseCreateRequest createRequest) throws ValidationException;
}
