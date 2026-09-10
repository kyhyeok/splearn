package kimspring.splearn.domain.curriculum;

import kimspring.splearn.domain.course.CourseFixture;

public class CurriculumFixture {
    public static Curriculum createCurriculum() {
        return new Curriculum(CourseFixture.createCourse());
    }
}
