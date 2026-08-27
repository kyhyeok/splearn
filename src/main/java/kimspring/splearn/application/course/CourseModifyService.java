package kimspring.splearn.application.course;

import kimspring.splearn.application.course.provided.CourseCreateRequest;
import kimspring.splearn.application.course.provided.CourseCreator;
import kimspring.splearn.application.course.provided.CourseFinder;
import kimspring.splearn.application.course.provided.CourseInfoUpdateRequest;
import kimspring.splearn.application.course.required.CourseRepository;
import kimspring.splearn.application.instructor.provided.InstructorFinder;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.instructor.Instructor;
import kimspring.splearn.support.stereotype.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class CourseModifyService implements CourseCreator {
    private final CourseRepository courseRepository;
    private final CourseFinder courseFinder;
    private final InstructorFinder instructorFinder;

    @Override
    public Course course(CourseCreateRequest createRequest) {
        Instructor instructor = instructorFinder.find(createRequest.instructorId());
        // 2. validate
        // 3. save

        return null;
    }

    @Override
    public Course updateInfo(Long courseId, CourseInfoUpdateRequest infoUpdateRequest) {
        return null;
    }
}
