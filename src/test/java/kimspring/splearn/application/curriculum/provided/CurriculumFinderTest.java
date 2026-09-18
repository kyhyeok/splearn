package kimspring.splearn.application.curriculum.provided;

import static kimspring.splearn.domain.curriculum.LessonContent.*;
import static kimspring.splearn.domain.curriculum.SectionContent.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import kimspring.splearn.application.curriculum.required.CurriculumRepository;
import kimspring.splearn.domain.curriculum.Curriculum;
import kimspring.splearn.domain.curriculum.Lesson;
import kimspring.splearn.domain.curriculum.SectionContent;
import kimspring.splearn.support.stereotype.ApplicationServiceTest;
import kimspring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;

@ApplicationServiceTest
@RequiredArgsConstructor
class CurriculumFinderTest extends BaseApplicationServiceTest {
	final CurriculumFinder curriculumFinder;
	final CurriculumRepository curriculumRepository;
	final EntityManager entityManager;

	@Test
	void find() {
		Curriculum curriculum = saveCurriculum();
		flushAndClear();

		Curriculum found = curriculumFinder.find(curriculum.getId());

		assertThat(found.getId()).isEqualTo(curriculum.getId());
	}

	@Test
	void findFail() {
		assertThatThrownBy(() -> curriculumFinder.find(Long.MAX_VALUE))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void findWithSections() {
		Curriculum curriculum = saveCurriculum(
			section("S0", lesson("L0"), lesson("L1")),
			section("S1", lesson("L2"))
		);
		flushAndClear();

		Curriculum found = curriculumFinder.findWithSections(curriculum.getId());

		assertThat(SectionContent.from(found)).containsExactly(
			section("S0", lesson("L0"), lesson("L1")),
			section("S1", lesson("L2"))
		);
	}

	@Test
	void findWithSectionsFail() {
		assertThatThrownBy(() -> curriculumFinder.findWithSections(Long.MAX_VALUE))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void findByCourse() {
		Curriculum curriculum = saveCurriculum();
		Long courseId = curriculum.getCourse().getId();
		flushAndClear();

		Curriculum found = curriculumFinder.findByCourse(courseId);

		assertThat(found.getId()).isEqualTo(curriculum.getId());
	}

	@Test
	void findByCourseFail() {
		assertThatThrownBy(() -> curriculumFinder.findByCourse(Long.MAX_VALUE))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void firstLesson() {
		Curriculum curriculum = saveCurriculum(
			section("S0"),
			section("S1", lesson("L0"), lesson("L1"))
		);
		Long firstLessonId = curriculum.getSections().get(1).getLessons().getFirst().getId();
		flushAndClear();

		Lesson firstLesson = curriculumFinder.firstLesson(curriculum.getId()).orElseThrow();

		assertThat(firstLesson.getId()).isEqualTo(firstLessonId);
		assertThat(firstLesson.getTitle()).isEqualTo("L0");
	}

	@Test
	void firstLessonEmpty() {
		Curriculum curriculum = saveCurriculum(section("S0"), section("S1"));
		flushAndClear();

		assertThat(curriculumFinder.firstLesson(curriculum.getId())).isEmpty();
	}

	@Test
	void firstLessonFailWhenCurriculumDoesNotExist() {
		assertThatThrownBy(() -> curriculumFinder.firstLesson(Long.MAX_VALUE))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void nextLesson() {
		Curriculum curriculum = saveCurriculum(
			section("S0", lesson("L0"), lesson("L1")),
			section("S1", lesson("L2"), lesson("L3"))
		);
		var lessons = curriculum.allLessons();
		Long l0Id = lessons.get(0).getId();
		Long l1Id = lessons.get(1).getId();
		Long l2Id = lessons.get(2).getId();
		Long l3Id = lessons.get(3).getId();
		flushAndClear();

		assertThat(curriculumFinder.nextLesson(curriculum.getId(), l0Id).orElseThrow().getId())
			.isEqualTo(l1Id);
		assertThat(curriculumFinder.nextLesson(curriculum.getId(), l1Id).orElseThrow().getId())
			.isEqualTo(l2Id);
		assertThat(curriculumFinder.nextLesson(curriculum.getId(), l2Id).orElseThrow().getId())
			.isEqualTo(l3Id);
		assertThat(curriculumFinder.nextLesson(curriculum.getId(), l3Id)).isEmpty();
	}

	@Test
	void nextLessonFailWhenLessonDoesNotExist() {
		Curriculum curriculum = saveCurriculum(section("S0", lesson("L0")));
		flushAndClear();

		assertThatThrownBy(() -> curriculumFinder.nextLesson(curriculum.getId(), Long.MAX_VALUE))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void nextLessonFailWhenCurriculumDoesNotExist() {
		assertThatThrownBy(() -> curriculumFinder.nextLesson(Long.MAX_VALUE, Long.MAX_VALUE))
			.isInstanceOf(IllegalArgumentException.class);
	}

	private Curriculum saveCurriculum(SectionContent... sectionContents) {
		var course = prepareCourse();
		Curriculum curriculum = curriculumRepository.findByCourseId(course.getId()).orElseThrow();

		for (SectionContent sectionContent : sectionContents) {
			curriculum.addSection(sectionContent.title());
			sectionContent.lessons().forEach(
				lessonContent -> curriculum.addLesson(curriculum.getSections().size() - 1, lessonContent.title())
			);
		}

		Curriculum saved = curriculumRepository.save(curriculum);
		entityManager.flush();
		return saved;
	}

	private void flushAndClear() {
		entityManager.flush();
		entityManager.clear();
	}
}
