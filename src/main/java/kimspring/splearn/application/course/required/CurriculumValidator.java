package kimspring.splearn.application.course.required;

import kimspring.splearn.domain.curriculum.InvalidCurriculumException;

public interface CurriculumValidator {
	void validate(Long courseId) throws InvalidCurriculumException;
}
