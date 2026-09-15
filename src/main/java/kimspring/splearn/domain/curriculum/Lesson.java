package kimspring.splearn.domain.curriculum;

import static jakarta.persistence.FetchType.*;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import kimspring.splearn.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true, exclude = {"section"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lesson extends AbstractEntity {
    @ManyToOne(optional = false, fetch = LAZY)
    Section section;

    @Column(length = 256)
    String title;

    Lesson(Section section, String title) {
        this.section = section;
        this.title = Objects.requireNonNull(title);
    }

    void updateTitle(String title) {
        this.title = Objects.requireNonNull(title);
    }

	public void moveTo(Section section) {
		this.section = section;
	}
}
