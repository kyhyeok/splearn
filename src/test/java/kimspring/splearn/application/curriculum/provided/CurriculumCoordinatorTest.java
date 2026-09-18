package kimspring.splearn.application.curriculum.provided;

import static kimspring.splearn.domain.curriculum.LessonContent.*;
import static kimspring.splearn.domain.curriculum.SectionContent.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import kimspring.splearn.application.curriculum.required.CurriculumRepository;
import kimspring.splearn.domain.curriculum.Curriculum;
import kimspring.splearn.domain.curriculum.InvalidCurriculumException;
import kimspring.splearn.domain.curriculum.SectionContent;
import kimspring.splearn.support.stereotype.ApplicationServiceTest;
import kimspring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;

@ApplicationServiceTest
@RequiredArgsConstructor
class CurriculumCoordinatorTest extends BaseApplicationServiceTest {
	final CurriculumCoordinator curriculumCoordinator;
	final CurriculumFinder curriculumFinder;
	final CurriculumRepository curriculumRepository;
	final EntityManager entityManager;

	@Test
	void addSection() {
		Curriculum curriculum = saveCurriculum();

		curriculumCoordinator.addSection(curriculum.getId(), "S0");
		curriculumCoordinator.addSection(curriculum.getId(), "S1");

		assertThat(sectionContentsAfterReload(curriculum.getId())).containsExactly(
			section("S0"),
			section("S1")
		);
	}

	@Test
	void addSectionWithIndex() {
		Curriculum curriculum = saveCurriculum(
			section("S1"),
			section("S3")
		);

		curriculumCoordinator.addSection(curriculum.getId(), 1, "S2");
		curriculumCoordinator.addSection(curriculum.getId(), 0, "S0");
		curriculumCoordinator.addSection(curriculum.getId(), 4, "S4");

		assertThat(sectionContentsAfterReload(curriculum.getId())).containsExactly(
			section("S0"),
			section("S1"),
			section("S2"),
			section("S3"),
			section("S4")
		);
	}

	@Test
	void addSectionFail() {
		Curriculum curriculum = saveCurriculum(section("S0"));

		assertThatThrownBy(() -> curriculumCoordinator.addSection(curriculum.getId(), -1, "Fail"))
			.isInstanceOf(IndexOutOfBoundsException.class);
		assertThatThrownBy(() -> curriculumCoordinator.addSection(curriculum.getId(), 2, "Fail"))
			.isInstanceOf(IndexOutOfBoundsException.class);
		assertThatThrownBy(() -> curriculumCoordinator.addSection(curriculum.getId(), null))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void addLesson() {
		Curriculum curriculum = saveCurriculum(
			section("S0"),
			section("S1")
		);

		curriculumCoordinator.addLesson(curriculum.getId(), 0, "L0");
		curriculumCoordinator.addLesson(curriculum.getId(), 1, "L1");

		assertThat(sectionContentsAfterReload(curriculum.getId())).containsExactly(
			section("S0", lesson("L0")),
			section("S1", lesson("L1"))
		);
	}

	@Test
	void addLessonFail() {
		Curriculum curriculum = saveCurriculum(section("S0"));

		assertThatThrownBy(() -> curriculumCoordinator.addLesson(curriculum.getId(), -1, "Fail"))
			.isInstanceOf(IndexOutOfBoundsException.class);
		assertThatThrownBy(() -> curriculumCoordinator.addLesson(curriculum.getId(), 1, "Fail"))
			.isInstanceOf(IndexOutOfBoundsException.class);
		assertThatThrownBy(() -> curriculumCoordinator.addLesson(curriculum.getId(), 0, null))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void updateSectionTitle() {
		Curriculum curriculum = saveCurriculum(
			section("S0"),
			section("S1")
		);

		curriculumCoordinator.updateSectionTitle(curriculum.getId(), 0, "S0 Updated");
		curriculumCoordinator.updateSectionTitle(curriculum.getId(), 1, "S1 Updated");

		assertThat(sectionContentsAfterReload(curriculum.getId())).containsExactly(
			section("S0 Updated"),
			section("S1 Updated")
		);
	}

	@Test
	void updateSectionTitleFail() {
		Curriculum curriculum = saveCurriculum(section("S0"));

		assertThatThrownBy(() -> curriculumCoordinator.updateSectionTitle(curriculum.getId(), -1, "Fail"))
			.isInstanceOf(IndexOutOfBoundsException.class);
		assertThatThrownBy(() -> curriculumCoordinator.updateSectionTitle(curriculum.getId(), 1, "Fail"))
			.isInstanceOf(IndexOutOfBoundsException.class);
		assertThatThrownBy(() -> curriculumCoordinator.updateSectionTitle(curriculum.getId(), 0, null))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void updateLessonTitle() {
		Curriculum curriculum = saveCurriculum(
			section("S0", lesson("L0"), lesson("L1")),
			section("S1", lesson("L2"), lesson("L3"))
		);

		curriculumCoordinator.updateLessonTitle(curriculum.getId(), 0, 0, "L0 Updated");
		curriculumCoordinator.updateLessonTitle(curriculum.getId(), 1, 1, "L3 Updated");

		assertThat(sectionContentsAfterReload(curriculum.getId())).containsExactly(
			section("S0", lesson("L0 Updated"), lesson("L1")),
			section("S1", lesson("L2"), lesson("L3 Updated"))
		);
	}

	@Test
	void updateLessonTitleFail() {
		Curriculum curriculum = saveCurriculum(section("S0", lesson("L0")));

		assertThatThrownBy(() -> curriculumCoordinator.updateLessonTitle(curriculum.getId(), -1, 0, "Fail"))
			.isInstanceOf(IndexOutOfBoundsException.class);
		assertThatThrownBy(() -> curriculumCoordinator.updateLessonTitle(curriculum.getId(), 0, 1, "Fail"))
			.isInstanceOf(IndexOutOfBoundsException.class);
		assertThatThrownBy(() -> curriculumCoordinator.updateLessonTitle(curriculum.getId(), 0, 0, null))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void removeLesson() {
		Curriculum curriculum = saveCurriculum(
			section("S0", lesson("L0"), lesson("L1")),
			section("S1", lesson("L2"), lesson("L3"))
		);

		curriculumCoordinator.removeLesson(curriculum.getId(), 0, 0);
		curriculumCoordinator.removeLesson(curriculum.getId(), 1, 1);

		assertThat(sectionContentsAfterReload(curriculum.getId())).containsExactly(
			section("S0", lesson("L1")),
			section("S1", lesson("L2"))
		);
	}

	@Test
	void removeLessonFail() {
		Curriculum curriculum = saveCurriculum(section("S0", lesson("L0")));

		assertThatThrownBy(() -> curriculumCoordinator.removeLesson(curriculum.getId(), -1, 0))
			.isInstanceOf(IndexOutOfBoundsException.class);
		assertThatThrownBy(() -> curriculumCoordinator.removeLesson(curriculum.getId(), 0, 1))
			.isInstanceOf(IndexOutOfBoundsException.class);
	}

	@Test
	void removeFirstSection() {
		Curriculum curriculum = saveCurriculum(
			section("S0", lesson("L0"), lesson("L1")),
			section("S1", lesson("L2"))
		);

		curriculumCoordinator.removeSection(curriculum.getId(), 0);

		assertThat(sectionContentsAfterReload(curriculum.getId())).containsExactly(
			section("S1", lesson("L0"), lesson("L1"), lesson("L2"))
		);
	}

	@Test
	void removeLastSection() {
		Curriculum curriculum = saveCurriculum(
			section("S0", lesson("L0")),
			section("S1", lesson("L1"), lesson("L2"))
		);

		curriculumCoordinator.removeSection(curriculum.getId(), 1);

		assertThat(sectionContentsAfterReload(curriculum.getId())).containsExactly(
			section("S0", lesson("L0"), lesson("L1"), lesson("L2"))
		);
	}

	@Test
	void removeSectionFail() {
		Curriculum curriculumWithOneSection = saveCurriculum(section("S0", lesson("L0")));

		assertThatThrownBy(() -> curriculumCoordinator.removeSection(curriculumWithOneSection.getId(), 0))
			.isInstanceOf(IllegalStateException.class);

		Curriculum curriculum = saveCurriculum(section("S0"), section("S1"));

		assertThatThrownBy(() -> curriculumCoordinator.removeSection(curriculum.getId(), -1))
			.isInstanceOf(IndexOutOfBoundsException.class);
		assertThatThrownBy(() -> curriculumCoordinator.removeSection(curriculum.getId(), 2))
			.isInstanceOf(IndexOutOfBoundsException.class);
	}

	@Test
	void moveLessonInSameSection() {
		Curriculum curriculum = saveCurriculum(
			section("S0", lesson("L0"), lesson("L1"), lesson("L2"))
		);

		curriculumCoordinator.moveLesson(curriculum.getId(), 0, 0, 0, 1);

		assertThat(sectionContentsAfterReload(curriculum.getId())).containsExactly(
			section("S0", lesson("L1"), lesson("L0"), lesson("L2"))
		);
	}

	@Test
	void moveLessonBetweenSections() {
		Curriculum curriculum = saveCurriculum(
			section("S0", lesson("L0"), lesson("L1")),
			section("S1", lesson("L2"))
		);

		curriculumCoordinator.moveLesson(curriculum.getId(), 0, 1, 1, 0);

		assertThat(sectionContentsAfterReload(curriculum.getId())).containsExactly(
			section("S0", lesson("L0")),
			section("S1", lesson("L1"), lesson("L2"))
		);
	}

	@Test
	void moveLessonFail() {
		Curriculum curriculum = saveCurriculum(
			section("S0", lesson("L0")),
			section("S1")
		);

		assertThatThrownBy(() -> curriculumCoordinator.moveLesson(curriculum.getId(), -1, 0, 1, 0))
			.isInstanceOf(IndexOutOfBoundsException.class);
		assertThatThrownBy(() -> curriculumCoordinator.moveLesson(curriculum.getId(), 0, 1, 1, 0))
			.isInstanceOf(IndexOutOfBoundsException.class);
		assertThatThrownBy(() -> curriculumCoordinator.moveLesson(curriculum.getId(), 0, 0, 2, 0))
			.isInstanceOf(IndexOutOfBoundsException.class);
		assertThatThrownBy(() -> curriculumCoordinator.moveLesson(curriculum.getId(), 0, 0, 1, 1))
			.isInstanceOf(IndexOutOfBoundsException.class);
	}

	@Test
	void validate() {
		Curriculum curriculum = saveCurriculum(
			section("S0", lesson("L0")),
			section("S1", lesson("L1"))
		);

		Curriculum validated = curriculumCoordinator.validate(curriculum.getId());

		assertThat(validated.getId()).isEqualTo(curriculum.getId());
	}

	@Test
	void validateFail() {
		Curriculum withoutSection = saveCurriculum();
		Curriculum withEmptySection = saveCurriculum(section("S0", lesson("L0")), section("S1"));

		assertThatThrownBy(() -> curriculumCoordinator.validate(withoutSection.getId()))
			.isInstanceOf(InvalidCurriculumException.class);
		assertThatThrownBy(() -> curriculumCoordinator.validate(withEmptySection.getId()))
			.isInstanceOf(InvalidCurriculumException.class);
	}

	@Test
	void modifyFailWhenCurriculumDoesNotExist() {
		assertThatThrownBy(() -> curriculumCoordinator.addSection(Long.MAX_VALUE, "Fail"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	private Curriculum saveCurriculum(SectionContent... sectionContents) {
		var course = prepareCourse();
		Curriculum curriculum = curriculumFinder.findByCourse(course.getId());

		for (SectionContent sectionContent : sectionContents) {
			curriculum.addSection(sectionContent.title());
			sectionContent.lessons().forEach(
				lessonContent -> curriculum.addLesson(curriculum.getSections().size() - 1, lessonContent.title())
			);
		}

		return curriculumRepository.save(curriculum);
	}

	private List<SectionContent> sectionContentsAfterReload(Long curriculumId) {
		entityManager.flush();
		entityManager.clear();
		return SectionContent.from(curriculumFinder.findWithSections(curriculumId));
	}
}
