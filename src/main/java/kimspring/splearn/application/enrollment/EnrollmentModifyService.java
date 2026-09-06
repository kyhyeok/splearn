package kimspring.splearn.application.enrollment;

import kimspring.splearn.application.course.provided.CourseFinder;
import kimspring.splearn.application.enrollment.provided.EnrollRequest;
import kimspring.splearn.application.enrollment.provided.Enroller;
import kimspring.splearn.application.enrollment.provided.EnrollmentFinder;
import kimspring.splearn.application.enrollment.required.EnrollmentRepository;
import kimspring.splearn.application.member.provided.MemberFinder;
import kimspring.splearn.domain.course.Course;
import kimspring.splearn.domain.enrollment.Enrollment;
import kimspring.splearn.domain.member.Member;
import kimspring.splearn.support.stereotype.ValidatedApplicationService;
import lombok.RequiredArgsConstructor;

@ValidatedApplicationService
@RequiredArgsConstructor
public class EnrollmentModifyService implements Enroller {
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentFinder enrollmentFinder;
    private final MemberFinder memberFinder;
    private final CourseFinder courseFinder;

    @Override
    public Enrollment enroll(EnrollRequest enrollRequest) {
        Member member = memberFinder.find(enrollRequest.memberId());
        Course course = courseFinder.find(enrollRequest.courseId());

        checkDuplication(member, course);

        Enrollment enrollment = Enrollment.enroll(member, course);

        return enrollmentRepository.save(enrollment);
    }

    private void checkDuplication(Member member, Course course) {
        if (enrollmentRepository.findByMemberIdAndCourseId(member.getId(), course.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 수강중인 강의입니다");
        }
    }

    @Override
    public Enrollment startStudying(Long enrollmentId) {
        Enrollment enrollment = enrollmentFinder.find(enrollmentId);

        enrollment.startStudying();

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment complete(Long enrollmentId) {
        Enrollment enrollment = enrollmentFinder.find(enrollmentId);

        enrollment.complete();

        return enrollmentRepository.save(enrollment);
    }
}
