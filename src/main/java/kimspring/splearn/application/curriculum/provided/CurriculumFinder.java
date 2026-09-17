package kimspring.splearn.application.curriculum.provided;

import kimspring.splearn.domain.curriculum.Curriculum;
import kimspring.splearn.domain.curriculum.Lesson;

public interface CurriculumFinder {
	Curriculum find(Long curriculumId);

	Curriculum findByCourse(Long courseId);

	Lesson firstLesson(Long curriculumId);

	Lesson nextLesson(Long curriculumId, Long lessonId);
}
