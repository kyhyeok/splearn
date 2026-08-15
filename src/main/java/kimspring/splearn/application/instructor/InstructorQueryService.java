package kimspring.splearn.application.instructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import kimspring.splearn.application.instructor.provided.InstructorFinder;
import kimspring.splearn.application.instructor.required.InstructorRepository;
import kimspring.splearn.domain.instructor.Instructor;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
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
