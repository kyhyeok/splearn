package kimspring.splearn.application.curriculum;

import kimspring.splearn.application.course.required.CurriculumCreator;
import kimspring.splearn.application.curriculum.provided.CurriculumCoordinator;
import kimspring.splearn.application.curriculum.provided.CurriculumFinder;
import kimspring.splearn.application.curriculum.required.CurriculumRepository;
import kimspring.splearn.application.curriculum.required.LessonRepository;
import kimspring.splearn.application.curriculum.required.SectionRepository;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.curriculum.Curriculum;
import kimspring.splearn.domain.curriculum.InvalidCurriculumException;
import kimspring.splearn.domain.curriculum.Lesson;
import kimspring.splearn.domain.curriculum.Section;
import kimspring.splearn.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CurriculumModifyService implements CurriculumCoordinator, CurriculumCreator {
	private final CurriculumRepository curriculumRepository;
	private final SectionRepository sectionRepository;
	private final LessonRepository lessonRepository;
	private final CurriculumFinder curriculumFinder;

	@Override
	public Long createCurriculum(Course course) {
		Curriculum curriculum = new Curriculum(course);

		return curriculumRepository.save(curriculum).getId();
	}

	@Override
	public Curriculum addSection(Long curriculumId, String title) {
		Curriculum curriculum = curriculumFinder.find(curriculumId);

		curriculum.addSection(title);

		return curriculumRepository.save(curriculum);
	}

	@Override
	public Curriculum addSection(Long curriculumId, int sectionIndex, String title) {
		Curriculum curriculum = curriculumFinder.find(curriculumId);

		curriculum.addSection(sectionIndex, title);

		return curriculumRepository.save(curriculum);
	}

	@Override
	public Curriculum addLesson(Long curriculumId, int sectionIndex, String title) {
		Curriculum curriculum = curriculumFinder.find(curriculumId);

		curriculum.addLesson(sectionIndex, title);

		return curriculumRepository.save(curriculum);
	}

	@Override
	public Curriculum updateSectionTitle(Long curriculumId, int sectionIndex, String title) {
		Curriculum curriculum = curriculumFinder.find(curriculumId);

		curriculum.updateSectionTitle(sectionIndex, title);

		return curriculumRepository.save(curriculum);
	}

	@Override
	public Curriculum updateLessonTitle(Long curriculumId, int sectionIndex, int lessonIndex, String title) {
		Curriculum curriculum = curriculumFinder.find(curriculumId);

		curriculum.updateLessonTitle(sectionIndex, lessonIndex, title);

		return curriculumRepository.save(curriculum);
	}

	@Override
	public Curriculum removeSection(Long curriculumId, int sectionIndex) {
		Curriculum curriculum = curriculumFinder.find(curriculumId);

		Section removed = curriculum.removeSection(sectionIndex);
		sectionRepository.delete(removed);

		return curriculumRepository.save(curriculum);
	}

	@Override
	public Curriculum removeLesson(Long curriculumId, int sectionIndex, int lessonIndex) {
		Curriculum curriculum = curriculumFinder.find(curriculumId);

		Lesson removed = curriculum.removeLesson(sectionIndex, lessonIndex);
		lessonRepository.delete(removed);

		return curriculumRepository.save(curriculum);
	}

	@Override
	public Curriculum moveLesson(Long curriculumId, int fromSectionIndex, int fromLessonIndex, int toSectionIndex,
		int toLessonIndex) {
		Curriculum curriculum = curriculumFinder.find(curriculumId);

		curriculum.moveLesson(fromSectionIndex, fromLessonIndex, toSectionIndex, toLessonIndex);

		return curriculumRepository.save(curriculum);
	}

	@Override
	public void validate(Long courseId) throws InvalidCurriculumException {
		Curriculum curriculum = curriculumFinder.findByCourse(courseId);

		curriculum.validate();
	}
}
