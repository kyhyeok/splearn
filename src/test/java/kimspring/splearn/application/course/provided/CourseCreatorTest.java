package kimspring.splearn.application.course.provided;

import org.junit.jupiter.api.Test;

import kimspring.splearn.application.curriculum.provided.CurriculumFinder;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.course.CourseFixture;
import kimspring.splearn.domain.curriculum.Curriculum;
import kimspring.splearn.support.stereotype.ApplicationServiceTest;
import kimspring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationServiceTest
@RequiredArgsConstructor
class CourseCreatorTest extends BaseApplicationServiceTest {
    final CourseCreator courseCreator;
    final CurriculumFinder curriculumFinder;

    @Test
    void create() {
        prepareInstructor();

        Course course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor.getId(), null));
        Curriculum curriculum = curriculumFinder.findByCourse(course.getId());

        assertThat(course.getId()).isNotNull();
        assertThat(curriculum.getId()).isNotNull();
        assertThat(curriculum.getCourse()).isEqualTo(course);
    }

    @Test
    void updateInfo() {
        prepareInstructor();
        Course course = courseCreator.create(CourseFixture.createCourseCreateRequest(instructor.getId(), null));

        Course updated = courseCreator.updateInfo(course.getId(), CourseFixture.createCourseInfoUpdateRequest("Updated"));

        assertThat(updated.getTitle()).isEqualTo("Updated");
    }
}
