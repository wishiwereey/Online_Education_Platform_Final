import builder.Course;
import model.Level;
import model.Student;
import strategy.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StrategyTest {
    private final Course popularCheap = course("A", Level.BEGINNER, 10, 500, "art");
    private final Course javaMid = course("B", Level.INTERMEDIATE, 60, 100, "java", "oop");
    private final Course javaAdv = course("C", Level.ADVANCED, 90, 50, "java");
    private final List<Course> catalog = List.of(popularCheap, javaMid, javaAdv);
    private final Student student = new Student("S", "Ann", "a@x", Level.BEGINNER, List.of("java", "oop"));

    private static Course course(String id, Level level, double price, int popularity, String... tags) {
        return Course.builder(id, "Course " + id).level(level).price(price).popularity(popularity)
                .addModule("M", "L").tags(tags).build();
    }

    private List<String> ids(List<Course> list) {
        return list.stream().map(Course::getId).toList();
    }

    @Test
    void popularityOrdersByEnrollments() {
        assertEquals(List.of("A", "B", "C"), ids(new PopularityStrategy().recommend(student, catalog, 5)));
    }

    @Test
    void interestsPreferMoreMatchingTags() {
        assertEquals(List.of("B", "C"), ids(new InterestBasedStrategy().recommend(student, catalog, 5)));
    }

    @Test
    void levelBasedSuggestsCurrentThenNextLevel() {
        assertEquals(List.of("A", "B"), ids(new LevelBasedStrategy().recommend(student, catalog, 5)));
    }

    @Test
    void budgetFiltersByPrice() {
        assertEquals(List.of("A", "B"), ids(new BudgetStrategy(60).recommend(student, catalog, 5)));
    }

    @Test
    void engineSwitchesStrategyAtRuntimeAndSkipsTakenCourses() {
        RecommendationEngine engine = new RecommendationEngine(new PopularityStrategy());
        assertEquals(List.of("B", "C"), ids(engine.recommend(student, catalog, Set.of("A"), 5)));
        engine.setStrategy(new BudgetStrategy(20));
        assertEquals(List.of(), ids(engine.recommend(student, catalog, Set.of("A"), 5)));
    }
}
