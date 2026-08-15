package kimspring.splearn.application.instructor.required;


import org.springframework.data.repository.Repository;

import java.util.Optional;

import kimspring.splearn.domain.instructor.Instructor;

public interface InstructorRepository extends Repository<Instructor, Long> {
    Instructor save(Instructor instructor);

    Optional<Instructor> findById(Long instructorId);

    Optional<Instructor> findByMemberId(Long memberId);
}

