package kimspring.splearn.application.course.required;

import kimspring.splearn.domain.course.Course;

public interface CurriculumCreator {
	Long createCurriculum(Course course);
}
