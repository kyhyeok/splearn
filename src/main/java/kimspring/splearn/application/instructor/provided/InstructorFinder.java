package kimspring.splearn.application.instructor.provided;

import java.util.Optional;

import kimspring.splearn.domain.instructor.Instructor;
import kimspring.splearn.domain.member.Member;

/**
 * 강사 조회
 */
public interface InstructorFinder {
    Instructor find(Long instructorId);

    Optional<Instructor> findByMember(Long memberId);

    default Optional<Instructor> findByMember(Member member) {
        return findByMember(member.getId());
    }
}
