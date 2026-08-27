package kimspring.splearn.application.course.provided;

import org.junit.jupiter.api.Test;

import kimspring.splearn.application.course.required.CourseRepository;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.course.CourseFixture;
import kimspring.splearn.support.exception.ValidationException;
import kimspring.splearn.support.stereotype.ApplicationServiceTest;
import kimspring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ApplicationServiceTest
@RequiredArgsConstructor
class CourseValidatorTest extends BaseApplicationServiceTest {
    final CourseValidator courseValidator;
    final CourseRepository courseRepository;

    @Test
    void titleDuplication() {
        var instructor1 = prepareInstructor();
        var instructor2 = prepareInstructor();

        Course course1 = courseRepository.save(CourseFixture.createCourse(instructor1, "Clean Spring"));
        Course course2 = courseRepository.save(CourseFixture.createCourse(instructor2, "Clean Code"));

        // instructor1, 중복되지 않는 제목 - OK
        courseValidator.validateForCreate(instructor1, new CourseCreateRequest(instructor1.getId(), "Spring 7", null));

        // instructor1, 중복 제목 - FAIL
        assertThatThrownBy(() -> courseValidator.validateForCreate(instructor1,
            new CourseCreateRequest(instructor1.getId(), "Clean Spring", null))).isInstanceOfSatisfying(
            ValidationException.class, e -> assertThat(e.getErrors()).hasSize(1));

        // instructor2, 1과 중복되는 제목 - OK
        courseValidator.validateForCreate(instructor2,
            new CourseCreateRequest(instructor2.getId(), "Clean Spring", null));
    }

}