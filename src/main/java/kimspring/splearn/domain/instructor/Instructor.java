package kimspring.splearn.domain.instructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import kimspring.splearn.domain.AbstractEntity;
import kimspring.splearn.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static kimspring.splearn.domain.instructor.InstructorStatus.ACTIVE;
import static kimspring.splearn.domain.instructor.InstructorStatus.PENDING;
import static kimspring.splearn.domain.instructor.InstructorStatus.REJECTED;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true, exclude = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Instructor extends AbstractEntity {
    @OneToOne(fetch = FetchType.LAZY)
    Member member;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    InstructorStatus status;

    public static Instructor apply(Member member) {
        state(member.isActive(), "등록 완료 상태가 아닌 회원은 강사 신청을 할 수 없습니다");

        Instructor instructor = new Instructor();
        instructor.member = member;
        instructor.status = PENDING;
        return instructor;
    }

    public void approve() {
        state(status == PENDING, "강사의 상태가 PENDING이 아닙니다");

        this.status = ACTIVE;
    }

    public void reject() {
        state(status == PENDING, "강사의 상태가 PENDING이 아닙니다");

        this.status = REJECTED;
    }

    public boolean isActive() {
        return status == ACTIVE;
    }

    public void ensureActive() {
        state(isActive(), "ACTIVE 상태가 아닙니다");
    }
}
