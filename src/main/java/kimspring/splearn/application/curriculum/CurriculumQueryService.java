package kimspring.splearn.application.curriculum;

import java.util.Optional;

import kimspring.splearn.application.curriculum.provided.CurriculumFinder;
import kimspring.splearn.application.curriculum.required.CurriculumRepository;
import kimspring.splearn.domain.curriculum.Curriculum;
import kimspring.splearn.domain.curriculum.Lesson;
import kimspring.splearn.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CurriculumQueryService implements CurriculumFinder {
	private final CurriculumRepository curriculumRepository;

	@Override
	public Curriculum find(Long curriculumId) {
		return this.curriculumRepository.findById(curriculumId).orElseThrow(
			() -> new IllegalArgumentException("커리큘럼을 찾을 수 없습니다. ID: " + curriculumId)
		);
	}

	@Override
	public Curriculum findWithSections(Long curriculumId) {
		return curriculumRepository.findWithSectionsById(curriculumId).orElseThrow(
			() -> new IllegalArgumentException("커리큘럼을 찾을 수 없습니다. ID: " + curriculumId)
		);
	}

	@Override
	public Curriculum findByCourse(Long courseId) {
		return curriculumRepository.findByCourseId(courseId).orElseThrow(
			() -> new IllegalArgumentException("커리큘럼을 찾을 수 없습니다. CourseID: " + courseId)
		);
	}

	@Override
	public Optional<Lesson> firstLesson(Long curriculumId) {
		return this.findWithSections(curriculumId).firstLesson();
	}

	@Override
	public Optional<Lesson> nextLesson(Long curriculumId, Long lessonId) {
		return this.findWithSections(curriculumId).nextLesson(lessonId);
	}
}
