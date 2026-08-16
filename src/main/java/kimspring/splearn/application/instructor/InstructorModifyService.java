package kimspring.splearn.application.instructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kimspring.splearn.application.instructor.provided.DuplicationInstructorApplicationException;
import kimspring.splearn.application.instructor.provided.InstructorApplication;
import kimspring.splearn.application.instructor.provided.InstructorApplyRequest;
import kimspring.splearn.application.instructor.provided.InstructorFinder;
import kimspring.splearn.application.instructor.required.InstructorRepository;
import kimspring.splearn.application.member.provided.MemberFinder;
import kimspring.splearn.domain.instructor.Instructor;
import kimspring.splearn.domain.member.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class InstructorModifyService implements InstructorApplication {
    private final InstructorRepository instructorRepository;
    private final InstructorFinder instructorFinder;
    private final MemberFinder memberFinder;

    @Override
    public Instructor apply(InstructorApplyRequest applyRequest) {
        Member member = memberFinder.find(applyRequest.memberId());

        checkDuplicateApplication(member);

        Instructor instructor = Instructor.apply(member);

        return instructorRepository.save(instructor);
    }

    private void checkDuplicateApplication(Member member) {
        if (instructorRepository.findByMemberId(member.getId()).isPresent()) {
            throw new DuplicationInstructorApplicationException("회원은 중복해서 강사 신청을 할 수 없습니다");
        }
    }

    @Override
    public Instructor approve(Long instructorId) {
        Instructor instructor = instructorFinder.find(instructorId);

        instructor.approve();

        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor reject(Long instructorId) {
        Instructor instructor = instructorFinder.find(instructorId);

        instructor.reject();

        return instructorRepository.save(instructor);
    }
}
