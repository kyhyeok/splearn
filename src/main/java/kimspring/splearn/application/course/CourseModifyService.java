package kimspring.splearn.application.course;

import kimspring.splearn.application.course.provided.CourseCreateRequest;
import kimspring.splearn.application.course.provided.CourseCreator;
import kimspring.splearn.application.course.provided.CourseFinder;
import kimspring.splearn.application.course.provided.CourseInfoUpdateRequest;
import kimspring.splearn.application.course.provided.CoursePublisher;
import kimspring.splearn.application.course.provided.CourseValidator;
import kimspring.splearn.application.course.required.CourseRepository;
import kimspring.splearn.application.instructor.provided.InstructorFinder;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.instructor.Instructor;
import kimspring.splearn.support.exception.ValidationException;
import kimspring.splearn.support.stereotype.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class CourseModifyService implements CourseCreator, CoursePublisher {
    private final CourseRepository courseRepository;
    private final CourseFinder courseFinder;
    private final CourseValidator courseValidator;
    private final InstructorFinder instructorFinder;

    @Override
    public Course create(CourseCreateRequest createRequest) throws ValidationException {
        Instructor instructor = instructorFinder.find(createRequest.instructorId());

        courseValidator.validateForCreate(instructor, createRequest);

        Course course = new Course(instructor, createRequest.title(), createRequest.description());

        return courseRepository.save(course);
    }

    @Override
    public Course updateInfo(Long courseId, CourseInfoUpdateRequest infoUpdateRequest) throws ValidationException {
        Course course = courseFinder.find(courseId);

        courseValidator.validateForUpdate(course, infoUpdateRequest);

        course.updateInfo(infoUpdateRequest.toInfo());

        return courseRepository.save(course);
    }

    @Override
    public Course submitForReview(Long courseId) {
        Course course = courseFinder.find(courseId);

        courseValidator.validateForReview(course);

        course.submitForReview();;

        return courseRepository.save(course);
    }

    @Override
    public Course publish(Long courseId) {
        Course course = courseFinder.find(courseId);

        courseValidator.validateForPublish(course);

        course.publish();;

        return courseRepository.save(course);
    }

    @Override
    public Course archive(Long courseId) {
        Course course = courseFinder.find(courseId);

        courseValidator.validateForArchive(course);

        course.archive();;

        return courseRepository.save(course);
    }
}
