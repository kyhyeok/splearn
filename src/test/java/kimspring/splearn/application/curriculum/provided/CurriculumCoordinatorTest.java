package kimspring.splearn.application.curriculum.provided;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import kimspring.splearn.domain.curriculum.Curriculum;
import kimspring.splearn.support.stereotype.ApplicationServiceTest;
import kimspring.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;

@ApplicationServiceTest
@RequiredArgsConstructor
class CurriculumCoordinatorTest extends BaseApplicationServiceTest {
	final CurriculumCoordinator curriculumCoordinator;

	@Test
	void create() {
		Curriculum curriculum = curriculumCoordinator.create(prepareCourse().getId());

		assertThat(curriculum.getId()).isNotNull();
	}
}