package kimspring.splearn.domain.curriculum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import kimspring.splearn.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@ToString(callSuper = true, exclude = {})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section extends AbstractEntity {
	@ManyToOne(optional = false, fetch = LAZY)
	private Curriculum curriculum;

	@Column(length = 256)
	private String title;

	@OneToMany(mappedBy = "section", cascade = ALL, orphanRemoval = true)
	private List<Lesson> lessons = new ArrayList<>();

	Section(Curriculum curriculum, String title) {
		this.curriculum = curriculum;
		this.title = Objects.requireNonNull(title);
	}

	Lesson addLesson(String title) {
		Lesson lesson = new Lesson(this, title);

		this.lessons.add(lesson);

		return lesson;
	}

	void updateTitle(String title) {
		this.title = Objects.requireNonNull(title);
	}

	void updateLessonTitle(int lessonIndex, String title) {
		this.lessons.get(lessonIndex).updateTitle(title);
	}

	Lesson removeLesson(int lessonIndex) {
		return this.lessons.remove(lessonIndex);
	}

	void moveAllLessonsTo(Section target, int insertIndex) {
		while(!this.lessons.isEmpty()) {
			target.addLesson(insertIndex++, this.lessons.getFirst());
			this.lessons.removeFirst();
		}
	}

	void addLesson(int insertIndex, Lesson lesson) {
		lesson.moveTo(this);
		this.lessons.add(insertIndex, lesson);
	}
}
