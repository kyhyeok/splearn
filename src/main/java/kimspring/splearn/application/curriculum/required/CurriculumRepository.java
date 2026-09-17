package kimspring.splearn.application.curriculum.required;


import java.util.Optional;

import org.springframework.data.repository.Repository;

import kimspring.splearn.domain.curriculum.Curriculum;

public interface CurriculumRepository extends Repository<Curriculum, Long> {
	Curriculum save(Curriculum curriculum);

	Optional<Curriculum> findById(Long curriculumId);
}
