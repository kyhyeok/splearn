package kimspring.learningtest.java;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ListTest {
    @Test
    void init() {
        var list1 = List.of(1, 2, 3, 4);
        assertThatThrownBy(() -> list1.set(0, 0)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> list1.add(5)).isInstanceOf(UnsupportedOperationException.class);

        var list2 = Arrays.asList(1, 2, 3, 4);
        list2.set(0, 0);
        assertThatThrownBy(() -> list2.add(5)).isInstanceOf(UnsupportedOperationException.class);

        var list3 = new ArrayList<>(List.of(1, 2, 3, 4));
        list3.set(0, 0);
        list3.add(5);
    }
}
