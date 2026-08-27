package kimspring.splearn.application.course;

import java.util.ArrayList;
import java.util.List;

import kimspring.splearn.application.course.provided.CourseCreateRequest;
import kimspring.splearn.application.course.provided.CourseInfoUpdateRequest;
import kimspring.splearn.application.course.provided.CourseValidator;
import kimspring.splearn.application.course.required.CourseRepository;
import kimspring.splearn.domain.course.Course;
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

        checkTitleDuplicationForCreate(instructor, createRequest.title(), errors);
        checkBannedWords(createRequest.title(), errors);
        checkBannedWords(createRequest.description(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public void validateForUpdate(Course course, CourseInfoUpdateRequest infoUpdateRequest) throws ValidationException {
        List<String> errors = new ArrayList<>();

        checkTitleDuplicationForUpdate(course, infoUpdateRequest.title(), errors);
        checkBannedWords(infoUpdateRequest.title(), errors);
        checkBannedWords(infoUpdateRequest.description(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    @Override
    public void validateForReview(Course course) throws ValidationException {
        // TODO
    }

    @Override
    public void validateForPublish(Course course) throws ValidationException {
        // TODO
    }

    @Override
    public void validateForArchive(Course course) throws ValidationException {
        // TODO
    }


    private void checkBannedWords(String text, List<String> errors) {
        // TODO

    }

    private void checkTitleDuplicationForCreate(Instructor instructor, String title, List<String> errors) {
        if (courseRepository.findByInstructorAndTitle(instructor, title).isPresent()) {
            errors.add("이미 사용중인 강의 제목입니다. " + title);
        }
    }

    private void checkTitleDuplicationForUpdate(Course course, String title, List<String> errors) {
        courseRepository.findByInstructorAndTitle(course.getInstructor(), title).ifPresent(found -> {
            if (!found.equals(course)) {
                errors.add("이미 사용중인 강의 제목입니다. " + title);
            }
        });
    }
}
