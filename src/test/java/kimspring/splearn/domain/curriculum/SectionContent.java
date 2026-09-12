package kimspring.splearn.domain.curriculum;

import java.util.List;

public record SectionContent(
	String title,
	List<LessonContent> lessons
) {
	public static List<SectionContent> from(Curriculum curriculum) {
		return curriculum.getSections().stream().map(
			section -> new SectionContent(
				section.getTitle(),
				section.getLessons().stream()
					.map(lesson -> new LessonContent(lesson.getTitle()))
					.toList()
			)
		).toList();
	}

	public static SectionContent section(String title, LessonContent... lessonContents) {
		return new SectionContent(title, List.of(lessonContents));
	}
}
