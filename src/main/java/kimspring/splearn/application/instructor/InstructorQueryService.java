package kimspring.splearn.application.instructor;

import java.util.Optional;

import kimspring.splearn.application.instructor.provided.InstructorFinder;
import kimspring.splearn.application.instructor.required.InstructorRepository;
import kimspring.splearn.domain.instructor.Instructor;
import kimspring.splearn.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class InstructorQueryService implements InstructorFinder {
    private final InstructorRepository instructorRepository;

    @Override
    public Instructor find(Long instructorId) {
        return instructorRepository.findById(instructorId)
                                   .orElseThrow(
                                       () -> new IllegalArgumentException("강사를 찾을 수 없습니다. ID: " + instructorId));
    }

    @Override
    public Optional<Instructor> findByMember(Long memberId) {
        return instructorRepository.findByMemberId(memberId);
    }
}
