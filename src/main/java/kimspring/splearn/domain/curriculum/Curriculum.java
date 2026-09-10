package kimspring.splearn.domain.curriculum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import kimspring.splearn.domain.AbstractEntity;
import kimspring.splearn.domain.course.Course;
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
public class Curriculum extends AbstractEntity {
    @OneToOne(optional = false, fetch = LAZY)
    private Course course;

    @OneToMany(mappedBy = "curriculum", cascade = ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

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
}
