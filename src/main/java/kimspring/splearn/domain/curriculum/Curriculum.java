package kimspring.splearn.domain.curriculum;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static org.springframework.util.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import kimspring.splearn.domain.AbstractEntity;
import kimspring.splearn.domain.course.Course;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true, exclude = {"course", "sections"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curriculum extends AbstractEntity {
	@OneToOne(optional = false, fetch = LAZY)
	private Course course;

	@OneToMany(mappedBy = "curriculum", cascade = ALL)
	@OrderColumn(name = "section_order")
	@Getter(AccessLevel.NONE)
	private List<Section> sections = new ArrayList<>();

	public List<Section> getSections() {
		return Collections.unmodifiableList(sections);
	}

	public Curriculum(Course course) {
		this.course = Objects.requireNonNull(course);
	}

	public Section addSection(String title) {
		Section section = new Section(this, title);

		this.sections.add(section);

		return section;
	}

	public Section addSection(int sectionIndex, String title) {
		Section section = new Section(this, title);

		this.sections.add(sectionIndex, section);

		return section;
	}

	public Lesson addLesson(int sectionIndex, String title) {
		return this.sections.get(sectionIndex).addLesson(title);
	}

	public Section updateSectionTitle(int sectionIndex, String title) {
		Section section = this.sections.get(sectionIndex);

		section.updateTitle(title);
		return section;
	}

	public void updateLessonTitle(int sectionIndex, int lessonIndex, String title) {
		this.sections.get(sectionIndex).updateLessonTitle(lessonIndex, title);
	}

	public Lesson removeLesson(int sectionIndex, int lessonIndex) {
		return this.sections.get(sectionIndex).removeLesson(lessonIndex);
	}

	public List<Lesson> allLessons() {
		return this.getSections().stream().flatMap(section -> section.getLessons().stream())
			.toList();
	}

	public Section removeSection(int sectionIndex) {
		state(this.sections.size() > 1, "마지막 남은 섹션은 삭제할 수 없습니다");

		Section removed = this.sections.remove(sectionIndex);

		if (sectionIndex == 0) {
			Section next = this.sections.get(0);
			removed.moveAllLessonsTo(next, 0);
		} else {
			Section previous = this.sections.get(sectionIndex - 1);
			removed.moveAllLessonsTo(previous, previous.getLessons().size());
		}

		return removed;
	}

	public void moveLesson(int fromSectionIndex, int fromLessonIndex, int toSectionIndex, int toLessonIndex) {
		Section from = this.sections.get(fromSectionIndex);
		Section to = this.sections.get(toSectionIndex);

		to.addLesson(toLessonIndex, from.removeLesson(fromLessonIndex));
	}

	public void validate() {
		if (this.sections.isEmpty())
			throw new InvalidCurriculumException("최소한 하나의 섹션이 필요합니다");

		this.sections.forEach(section -> {
			if (section.getLessons().isEmpty())
				throw new InvalidCurriculumException("수업이 없는 섹션은 허용되지 않습니다");
		});
	}

	public Optional<Lesson> firstLesson() {
		return this.allLessons().stream().findFirst();
	}

	public Optional<Lesson> nextLesson(Lesson lesson) {
		List<Lesson> lessons = allLessons();

		int index = lessons.indexOf(lesson);

		isTrue(index >= 0, "커리큘럼에 포함된 수업이 아닙니다");

		if (index + 1 >= lessons.size())
			return Optional.empty();

		return Optional.of(lessons.get(index + 1));
	}

	public Optional<Lesson> nextLesson(Long lessonId) {
		Lesson lesson = allLessons().stream().filter(
				candidate -> lessonId.equals(candidate.getId()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("레슨을 찾을 수 없습니다. ID: " + lessonId));

		return nextLesson(lesson);
	}
}
