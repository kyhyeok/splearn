package kimspring.splearn.application.enrollment;

import java.util.List;
import java.util.Optional;

import kimspring.splearn.application.enrollment.provided.EnrollmentFinder;
import kimspring.splearn.application.enrollment.required.EnrollmentRepository;
import kimspring.splearn.domain.enrollment.Enrollment;
import kimspring.splearn.support.stereotype.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class EnrollmentQueryService implements EnrollmentFinder {
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public Enrollment find(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                                   .orElseThrow(
                                       () -> new IllegalArgumentException("수강을 찾을 . 없습니다. ID: " + enrollmentId));
    }

    @Override
    public List<Enrollment> findByMember(Long memberId) {
        return enrollmentRepository.findByMemberId(memberId);
    }

    @Override
    public Optional<Enrollment> findByMemberAndCourse(Long memberId, Long courseId) {
        return enrollmentRepository.findByMemberIdAndCourseId(memberId, courseId);
    }
}
