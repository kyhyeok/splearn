package kimspring.splearn.application.curriculum.required;

import org.springframework.data.repository.Repository;

import kimspring.splearn.domain.curriculum.Lesson;

public interface LessonRepository extends Repository<Lesson, Long> {
	void delete(Lesson lesson);
}
