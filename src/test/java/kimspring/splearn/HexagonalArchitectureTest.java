package kimspring.splearn;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethodCall;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AnalyzeClasses(packages = "kimspring.splearn", importOptions = ImportOption.DoNotIncludeTests.class)
public class HexagonalArchitectureTest {
    @ArchTest
    void hexagonalArchitecture(JavaClasses classes) {
        Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .layer("domain").definedBy("kimspring.splearn.domain..")
                .layer("application").definedBy("kimspring.splearn.application..")
                .layer("adapter").definedBy("kimspring.splearn.adapter..")
                .whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapter")
                .whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
                .whereLayer("adapter").mayNotBeAccessedByAnyLayer()
                .check(classes);
    }

    @ArchTest
    void aggregateFreeOfCycles(JavaClasses classes) {
        SlicesRuleDefinition.slices()
            .matching("kimspring.splearn.domain.(*)..")
            .should().beFreeOfCycles()
            .check(classes);
    }

    @ArchTest
    void applicationServiceFreeOfCycles(JavaClasses classes) {
        SlicesRuleDefinition.slices()
            .matching("kimspring.splearn.application.(*)..")
            .should().beFreeOfCycles()
            .check(classes);
    }

    @ArchTest
    void aggregateDependencies(JavaClasses classes) {
        SlicesRuleDefinition.slices()
            .matching("kimspring.splearn.domain.(*)..")
            .should(onlyCallGettersOrRecordMethodsOfOtherSlices())
            .check(classes);
    }

    private <SLICE extends Set<JavaClass>> ArchCondition<SLICE> onlyCallGettersOrRecordMethodsOfOtherSlices() {
        return new ArchCondition<>("다른 슬라이스의 getter 또는 레코드, Enum 메소드만 호출할 수 있다") {
            private final Set<JavaClass> classesInAnySlice = new HashSet<>();

            @Override
            public void init(Collection<SLICE> allSlice) {
                allSlice.forEach(classesInAnySlice::addAll);
            }

            @Override
            public void check(SLICE slice, ConditionEvents events) {
                for (JavaClass javaClass : slice) {
                    for (JavaMethodCall call : javaClass.getMethodCallsFromSelf()) {
                        JavaClass targetOwner = call.getTargetOwner();
                        if (slice.contains(targetOwner)) continue;
                        if (!classesInAnySlice.contains(targetOwner)) continue;
                        if (targetOwner.isRecord()) continue;
                        if (targetOwner.isEnum()) continue;

                        String methodName = call.getTarget().getName();
                        if (methodName.startsWith("get") || methodName.startsWith("is")
                            || methodName.startsWith("ensure")) continue;

                        events.add(SimpleConditionEvent.violated(call, call.getDescription()));
                    }
                }
            }
        };
    }
}
