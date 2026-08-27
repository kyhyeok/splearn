package kimspring.splearn.application.course;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Size;
import kimspring.splearn.application.course.provided.CourseCreateRequest;
import kimspring.splearn.application.course.provided.CourseValidator;
import kimspring.splearn.application.course.required.CourseRepository;
import kimspring.splearn.domain.instructor.Instructor;
import kimspring.splearn.support.exception.ValidationException;
import kimspring.splearn.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CourseValidationService implements CourseValidator {
    private final CourseRepository courseRepository;

    @Override
    public void validateForCreate(Instructor instructor, CourseCreateRequest createRequest) throws ValidationException {
        instructor.ensureActive();

        List<String> errors = new ArrayList<>();

        checkTitleDuplication(instructor, createRequest.title(), errors);
        checkBannedWords(createRequest.title(), errors);
        checkBannedWords(createRequest.description(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void checkBannedWords(String text, List<String> errors) {
        // TODO

    }

    private void checkTitleDuplication(Instructor instructor, String title, List<String> errors) {
        if (courseRepository.findByInstructorAndTitle(instructor, title).isPresent()) {
            errors.add("이미 사용중인 강의 제목입니다. " + title);
        }
    }
}
