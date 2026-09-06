package kimspring.splearn.application.course.required;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.course.CourseFixture;
import kimspring.splearn.support.test.BaseRepositoryTest;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@RequiredArgsConstructor
class CourseRepositoryTest extends BaseRepositoryTest {
    final CourseRepository courseRepository;


    @BeforeEach
    void setUp() {
        member = prepareActiveMember();
        instructor = prepareActiveInstructor(member);
    }

    @Test
    void saveAndFindId() {
        Course course = CourseFixture.createCourse(instructor, null);
        course = courseRepository.save(course);

        assertThat(course.getId()).isNotNull();

        entityManager.flush();
        entityManager.clear();

        Course found = courseRepository.findById(course.getId()).orElseThrow();

        assertThat(found).isEqualTo(course);
    }

    @Test
    void findByTitleContaining() {
        List<Long> ids =
            Stream.of(prepareCourse(instructor, "Hello Spring"), prepareCourse(instructor, "Clean Spring 2"),
                prepareCourse(instructor, "Clean Code")).map(Course::getId).toList();

        assertThat(courseRepository.findByTitleContaining("Spring").stream().map(Course::getId)).isEqualTo(
            List.of(ids.get(0), ids.get(1)));

        assertThat(courseRepository.findByTitleContaining("Clean").stream().map(Course::getId)).isEqualTo(
            List.of(ids.get(1), ids.get(2)));

        assertThat(courseRepository.findByTitleContaining("Code").stream().map(Course::getId)).isEqualTo(
            List.of(ids.get(2)));

        assertThat(courseRepository.findByTitleContaining("JPA").stream().map(Course::getId)).isEqualTo(
            Collections.emptyList());
    }

    @Test
    void findByInstructor() {
        var instructor1 = prepareActiveInstructor();
        var instructor2 = prepareActiveInstructor();

        var course = prepareCourse(instructor1, null);
        var course2 = prepareCourse(instructor2, null);

        List<Course> courses = courseRepository.findByInstructorId(instructor1.getId());
        assertThat(courses).singleElement().isEqualTo(course);

        List<Course> courses2 = courseRepository.findByInstructorId(instructor2.getId());
        assertThat(courses2).singleElement().isEqualTo(course2);

        List<Course> courses2_1 = courseRepository.findByInstructor(instructor2);
        assertThat(courses2_1).singleElement().isEqualTo(course2);
    }

    @Test
    void uniqueTitleAndInstructor() {
        prepareCourse(instructor, "Title");

        assertThatThrownBy(() -> courseRepository.save(CourseFixture.createCourse(instructor, "Title"))).isInstanceOf(
            DataIntegrityViolationException.class);
    }
}