package kimspring.splearn.application.course.provided;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.course.CourseFixture;
import kimspring.splearn.support.stereotype.ApplicationServiceTest;
import kimspring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationServiceTest
@RequiredArgsConstructor
class CourseCreatorTest extends BaseApplicationServiceTest {
    final CourseCreator courseCreator;

    @Test
    void create() {
        prepareInstructor();

        Course course = courseCreator.course(CourseFixture.createCourseCreateRequest(instructor.getId(), null));

        assertThat(course.getId()).isNotNull();
    }
    @Test
    void updateInfo() {
        prepareInstructor();
        Course course = courseCreator.course(CourseFixture.createCourseCreateRequest(instructor.getId(), null));

        Course updated = courseCreator.updateInfo(course.getId(), CourseFixture.createCourseInfoUpdateRequest("Updated"));

        assertThat(updated.getTitle()).isEqualTo("Updated");
    }
}