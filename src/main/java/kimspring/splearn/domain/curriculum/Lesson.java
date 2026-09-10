package kimspring.splearn.domain.curriculum;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import kimspring.splearn.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@ToString(callSuper = true, exclude = {})
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
}
