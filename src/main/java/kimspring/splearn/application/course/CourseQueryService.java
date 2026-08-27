package kimspring.splearn.application.course;

import java.util.List;

import kimspring.splearn.application.course.provided.CourseFinder;
import kimspring.splearn.application.course.required.CourseRepository;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CourseQueryService implements CourseFinder {
    final CourseRepository courseRepository;

    @Override
    public Course find(Long courseId) {
        return courseRepository.findById(courseId)
                               .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다. ID: " + courseId));
    }

    @Override
    public List<Course> findByTitle(String keyword) {
        return courseRepository.findByTitleContaining(keyword);
    }

    @Override
    public List<Course> findByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }
}
