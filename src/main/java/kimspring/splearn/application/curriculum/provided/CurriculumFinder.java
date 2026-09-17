package kimspring.splearn.application.curriculum.provided;

import java.util.Optional;

import kimspring.splearn.domain.curriculum.Curriculum;
import kimspring.splearn.domain.curriculum.Lesson;

public interface CurriculumFinder {
	Curriculum find(Long curriculumId);

	Curriculum findWithSections(Long curriculumId);

	Curriculum findByCourse(Long courseId);

	Optional<Lesson> firstLesson(Long curriculumId);

	Optional<Lesson> nextLesson(Long curriculumId, Long lessonId);
}
