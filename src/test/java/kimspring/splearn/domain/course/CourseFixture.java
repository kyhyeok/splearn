package kimspring.splearn.domain.course;

import org.instancio.Instancio;

import java.time.LocalDateTime;

import kimspring.splearn.domain.instructor.InstructorFixture;

import static org.instancio.Select.field;

public class CourseFixture {
    public static Course createCourse() {
        var instructor = InstructorFixture.createActiveInstructor();

        CourseDetail detail = Instancio.of(CourseDetail.class)
                                       .generate(field(CourseDetail::getDescription),
                                           gen -> gen.string().maxLength(100).nullable())
                                       .set(field(CourseDetail::getCreatedAt), LocalDateTime.now())
                                       .create();

        return Instancio.of(Course.class)
                        .ignore(field(Course::getId))
                        .set(field(Course::getInstructor), instructor)
                        .generate(field(Course::getTitle), gen -> gen.string().maxLength(100).minLength(2))
                        .set(field(Course::getStatus), CourseStatus.DRAFT)
                        .set(field(Course::getDetail), detail)
                        .create();
    }
}
