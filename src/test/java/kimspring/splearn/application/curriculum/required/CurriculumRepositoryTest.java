package kimspring.splearn.application.curriculum.required;

import static kimspring.splearn.domain.curriculum.LessonContent.*;
import static kimspring.splearn.domain.curriculum.SectionContent.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kimspring.splearn.domain.curriculum.Curriculum;
import kimspring.splearn.domain.curriculum.Lesson;
import kimspring.splearn.domain.curriculum.LessonContent;
import kimspring.splearn.domain.curriculum.Section;
import kimspring.splearn.domain.curriculum.SectionContent;
import kimspring.splearn.support.test.BaseRepositoryTest;
import lombok.RequiredArgsConstructor;

@DataJpaTest
@RequiredArgsConstructor
class CurriculumRepositoryTest extends BaseRepositoryTest {
	final CurriculumRepository curriculumRepository;
	final LessonRepository lessonRepository;
	final SectionRepository sectionRepository;

	@Test
	void saveAndFindById() {
		Long curriculumId = saveCurriculum();

		Statistics statistics = prepareStatistics();

		Curriculum found = curriculumRepository.findById(curriculumId).orElseThrow();

		assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
		assertThat(found.getId()).isEqualTo(curriculumId);

		assertThat(SectionContent.from(found)).containsExactly(
			section("S1", lesson("L1"), lesson("L2")),
			section("S2", lesson("L3"))
		);

		assertThat(statistics.getPrepareStatementCount()).isEqualTo(4);
	}

	@Test
	void saveAndFindWithSectionsById() {
		Long curriculumId = saveCurriculum();

		Statistics statistics = prepareStatistics();

		Curriculum found = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();

		assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
		assertThat(found.getId()).isEqualTo(curriculumId);

		assertThat(SectionContent.from(found)).containsExactly(
			section("S1", lesson("L1"), lesson("L2")),
			section("S2", lesson("L3"))
		);

		assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
	}

	@Test
	void removeLesson() {
		Long curriculumId = saveCurriculum();
		Curriculum curriculum = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();

		Lesson lesson = curriculum.removeLesson(0, 0);
		lessonRepository.delete(lesson);

		curriculumRepository.save(curriculum);

		entityManager.flush();
		entityManager.clear();

		curriculum = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();
		assertThat(SectionContent.from(curriculum)).containsExactly(
			section("S1", lesson("L2")),
			section("S2", lesson("L3"))
		);
	}

	@Test
	void removeSection() {
		Long curriculumId = saveCurriculum();
		Curriculum curriculum = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();

		Section section = curriculum.removeSection(0);
		sectionRepository.delete(section);

		entityManager.flush();
		entityManager.clear();

		curriculum = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();
		assertThat(SectionContent.from(curriculum)).containsExactly(
			section("S2", lesson("L1"), lesson("L2"), lesson("L3"))
		);
	}

	@Test
	void moveLesson() {
		Long curriculumId = saveCurriculum();
		Curriculum curriculum = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();

		curriculum.moveLesson(0, 0, 1, 1);

		entityManager.flush();
		entityManager.clear();

		curriculum = curriculumRepository.findWithSectionsById(curriculumId).orElseThrow();
		assertThat(SectionContent.from(curriculum)).containsExactly(
			section("S1", lesson("L2")),
			section("S2", lesson("L3"), lesson("L1"))
		);
	}

	private Long saveCurriculum() {
		Curriculum curriculum = new Curriculum(prepareCourse());

		curriculum = curriculumRepository.save(curriculum);
		curriculum.addSection("S1");
		curriculum.addLesson(0, "L1");
		curriculum.addLesson(0, "L2");
		curriculum.addSection("S2");
		curriculum.addLesson(1, "L3");

		entityManager.flush();
		entityManager.clear();
		return curriculum.getId();
	}
}