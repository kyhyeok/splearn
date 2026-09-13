package kimspring.splearn.domain.curriculum;

import static kimspring.splearn.domain.curriculum.LessonContent.*;
import static kimspring.splearn.domain.curriculum.SectionContent.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import kimspring.splearn.domain.course.CourseFixture;

class CurriculumTest {
	@Test
	void create() {
		var course = CourseFixture.createCourse();

		Curriculum curriculum = new Curriculum(course);

		assertThat(curriculum.getCourse()).isEqualTo(course);

		assertThatThrownBy(() -> new Curriculum(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void addSection() {
		Curriculum curriculum = CurriculumFixture.createCurriculum();

		Section section = curriculum.addSection("Section 1");

		assertThat(curriculum.getSections()).containsExactly(section);
		assertThat(curriculum.getSections()).extracting(Section::getTitle).containsExactly("Section 1");

		assertThatThrownBy(() -> curriculum.addSection(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void addSectionWithIndex() {
		Curriculum curriculum = CurriculumFixture.createCurriculum();

		Section s1 = curriculum.addSection("S1");
		Section s2 = curriculum.addSection("S2");

		assertThat(curriculum.getSections()).containsExactly(s1, s2);

		Section s1_1 = curriculum.addSection(1, "S1_1");

		assertThat(curriculum.getSections()).containsExactly(s1, s1_1, s2);

		Section s3 = curriculum.addSection(3, "S3");

		assertThat(curriculum.getSections()).containsExactly(s1, s1_1, s2, s3);

		assertThatThrownBy(() -> curriculum.addSection(5, "Fail")).isInstanceOf(IndexOutOfBoundsException.class);
	}

	@Test
	void addLesson() {
		Curriculum curriculum = CurriculumFixture.createCurriculum();
		Section s0 = curriculum.addSection("S0");
		Section s1 = curriculum.addSection("S1");

		Lesson l0 = curriculum.addLesson(0, "L0");
		Lesson l1 = curriculum.addLesson(0, "L1");

		assertThat(s0.getLessons()).containsExactly(l0, l1);

		Lesson l2 = curriculum.addLesson(1, "L2");

		assertThat(s1.getLessons()).containsExactly(l2);
	}

	@Test
	void updateSectionTitle() {
		Curriculum curriculum = CurriculumFixture.createCurriculum();
		Section s0 = curriculum.addSection("S0");
		Section s1 = curriculum.addSection("S1");

		curriculum.updateSectionTitle(0, "S0 Updated");

		assertThat(s0.getTitle()).isEqualTo("S0 Updated");

		curriculum.updateSectionTitle(1, "S1 Updated");

		assertThat(curriculum.getSections()).extracting(Section::getTitle)
			.containsExactly("S0 Updated", "S1 Updated");
	}

	@Test
	void updateLessonTitle() {
		Curriculum curriculum = CurriculumFixture.createCurriculum();
		Section s0 = curriculum.addSection("S0");
		Section s1 = curriculum.addSection("S1");
		Lesson l0_0 = curriculum.addLesson(0, "L0_0");
		Lesson l0_1 = curriculum.addLesson(0, "L0_1");
		Lesson l1 = curriculum.addLesson(1, "L1");

		curriculum.updateLessonTitle(0, 0, "L0_0 Updated");

		assertThat(l0_0.getTitle()).isEqualTo("L0_0 Updated");

		curriculum.updateLessonTitle(1, 0, "L1 Updated");

		assertThat(l1.getTitle()).isEqualTo("L1 Updated");

		var lessons = curriculum.allLessons();

		assertThat(lessons).extracting(Lesson::getTitle)
			.containsExactly("L0_0 Updated", "L0_1", "L1 Updated");
	}

	@Test
	void removeLesson() {
		Curriculum curriculum = CurriculumFixture.createCurriculum();
		Section s0 = curriculum.addSection("S0");
		Section s1 = curriculum.addSection("S1");
		Lesson l0_0 = curriculum.addLesson(0, "L0");
		Lesson l0_1 = curriculum.addLesson(0, "L1");
		Lesson l1_0 = curriculum.addLesson(1, "L2");
		Lesson l1_1 = curriculum.addLesson(1, "L3");

		assertThat(curriculum.allLessons()).extracting(Lesson::getTitle)
			.containsExactly("L0", "L1", "L2", "L3");

		curriculum.removeLesson(0, 0);

		assertThat(curriculum.allLessons()).extracting(Lesson::getTitle)
			.containsExactly("L1", "L2", "L3");

		curriculum.removeLesson(1, 1);

		assertThat(curriculum.allLessons()).extracting(Lesson::getTitle)
			.containsExactly("L1", "L2");
	}

	@Test
	void removeSection() {
		Curriculum curriculum = CurriculumFixture.createCurriculum();
		Section s0 = curriculum.addSection("S0");
		Section s1 = curriculum.addSection("S1");
		Section s2 = curriculum.addSection("S2");
		Lesson l0 = curriculum.addLesson(0, "L0");
		Lesson l1 = curriculum.addLesson(0, "L1");
		Lesson l2 = curriculum.addLesson(1, "L2");
		Lesson l3 = curriculum.addLesson(1, "L3");
		Lesson l4 = curriculum.addLesson(2, "L4");
		Lesson l5 = curriculum.addLesson(2, "L5");

		assertThat(SectionContent.from(curriculum)).containsExactly(
			section("S0", lesson("L0"), lesson("L1")),
			section("S1", lesson("L2"), lesson("L3")),
			section("S2", lesson("L4"), lesson("L5"))
		);

		// 섹션을 삭제하면 수업의 앞으로 섹션 수업의 뒤에 추가되나
		curriculum.removeSection(2);

		assertThat(curriculum.getSections()).containsExactly(s0, s1);
		assertThat(SectionContent.from(curriculum)).containsExactly(
			section("S0", lesson("L0"), lesson("L1")),
			section("S1", lesson("L2"), lesson("L3"), lesson("L4"), lesson("L5"))
		);

		// 단, 첫번째 섹션을 삭제하면 수업은 다음 섹션 앞부분으로 추가된다
		curriculum.removeSection(0);

		assertThat(curriculum.getSections()).containsExactly(s1);
		assertThat(SectionContent.from(curriculum)).containsExactly(
			section("S1", lesson("L0"), lesson("L1"), lesson("L2"), lesson("L3"),
				lesson("L4"), lesson("L5"))
		);

		// 하나 남은 섹션은 삭제할 수 없다
		assertThatThrownBy(() -> curriculum.removeSection(0))
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	void moveLesson() {
		Curriculum curriculum = CurriculumFixture.createCurriculum();
		Section s0 = curriculum.addSection("S0");
		Section s1 = curriculum.addSection("S1");
		Section s2 = curriculum.addSection("S2");
		Lesson l0 = curriculum.addLesson(0, "L0");
		Lesson l1 = curriculum.addLesson(0, "L1");
		Lesson l2 = curriculum.addLesson(0, "L2");
		Lesson l3 = curriculum.addLesson(1, "L3");
		Lesson l4 = curriculum.addLesson(1, "L4");
		Lesson l5 = curriculum.addLesson(2, "L5");
		Lesson l6 = curriculum.addLesson(2, "L6");

		assertThat(SectionContent.from(curriculum)).containsExactly(
			section("S0", lesson("L0"), lesson("L1"), lesson("L2")),
			section("S1", lesson("L3"), lesson("L4")),
			section("S2", lesson("L5"), lesson("L6"))
		);

		curriculum.moveLesson(0, 0, 0, 1);

		assertThat(SectionContent.from(curriculum)).containsExactly(
			section("S0", lesson("L1"), lesson("L0"), lesson("L2")),
			section("S1", lesson("L3"), lesson("L4")),
			section("S2", lesson("L5"), lesson("L6"))
		);

		curriculum.moveLesson(0, 2, 0, 0);

		assertThat(SectionContent.from(curriculum)).containsExactly(
			section("S0", lesson("L2"), lesson("L1"), lesson("L0")),
			section("S1", lesson("L3"), lesson("L4")),
			section("S2", lesson("L5"), lesson("L6"))
		);

		curriculum.moveLesson(0, 1, 1, 2);

		assertThat(SectionContent.from(curriculum)).containsExactly(
			section("S0", lesson("L2"), lesson("L0")),
			section("S1", lesson("L3"), lesson("L4"), lesson("L1")),
			section("S2", lesson("L5"), lesson("L6"))
		);

		assertThat(l1.getSection()).isEqualTo(s1);

		curriculum.moveLesson(2, 1, 0, 1);

		assertThat(SectionContent.from(curriculum)).containsExactly(
			section("S0", lesson("L2"), lesson("L6"), lesson("L0")),
			section("S1", lesson("L3"), lesson("L4"), lesson("L1")),
			section("S2", lesson("L5"))
		);

		assertThat(l6.getSection()).isEqualTo(s0);
	}

	@Test
	void validate() {
		Curriculum curriculum = CurriculumFixture.createCurriculum();

		// 최소한 하나의 섹션은 필요하다
		assertThatThrownBy(() -> curriculum.validate())
			.isInstanceOf(InvalidCurriculumException.class);

		curriculum.addSection("S0");

		// 레슨이 없는 섹션은 검증 실패
		assertThatThrownBy(() -> curriculum.validate())
			.isInstanceOf(InvalidCurriculumException.class);

		curriculum.addLesson(0, "L0");

		curriculum.validate();

		curriculum.addSection("S1");
		curriculum.addLesson(1, "L1");

		curriculum.validate();

		// 레슨이 없는 섹션은 검증 실패
		curriculum.removeLesson(1, 0);
		assertThatThrownBy(() -> curriculum.validate())
			.isInstanceOf(InvalidCurriculumException.class);
	}
}