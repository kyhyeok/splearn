package kimspring.splearn.domain.course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kimspring.splearn.domain.instructor.InstructorFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CourseTest {
    Course course;

    @BeforeEach
    void setUp() {
        this.course = CourseFixture.createCourse();
    }


    @Test
    void create() {
        var instructor = InstructorFixture.createActiveInstructor();

        Course course = new Course(instructor, "Clean Spring 2", "Description");

        assertThat(course.getInstructor()).isEqualTo(instructor);
        assertThat(course.getTitle()).isEqualTo("Clean Spring 2");
        assertThat(course.getStatus()).isEqualTo(CourseStatus.DRAFT);
        assertThat(course.getDetail().getDescription()).isEqualTo("Description");
        assertThat(course.getDetail().getCreatedAt()).isNotNull();
    }

    @Test
    void createFailNotActiveInstructor() {
        var instructor = InstructorFixture.createInstructor();

        assertThatThrownBy(() -> new Course(instructor, "title", null)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void submitForReview() {
        course.submitForReview();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.IN_REVIEW);

        assertThatThrownBy(() -> course.submitForReview()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void publish() {
        course.submitForReview();

        course.publish();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.PUBLISHED);
        assertThat(course.getDetail().getPublishedAt()).isNotNull();

        assertThatThrownBy(() -> course.publish()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void archive() {
        course.submitForReview();
        course.publish();

        course.archive();

        assertThat(course.getStatus()).isEqualTo(CourseStatus.ARCHIVED);
        assertThat(course.getDetail().getArchivedAt()).isNotNull();

        assertThatThrownBy(() -> course.archive()).isInstanceOf(IllegalStateException.class);
    }
}