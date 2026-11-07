import builder.Course;
import builder.CourseDirector;
import model.Level;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuilderTest {

    @Test
    void buildsCourseWithAllParts() {
        Course c = Course.builder("T-1", "Testing")
                .instructor("Teacher").level(Level.ADVANCED).price(10)
                .addModule("M1", "L1", "L2").addModule("M2", "L3")
                .addQuiz("Q", 5, 50).tags("a", "b")
                .build();
        assertEquals("Testing", c.getTitle());
        assertEquals(Level.ADVANCED, c.getLevel());
        assertEquals(2, c.getModules().size());
        assertEquals(3, c.getLessonCount());
        assertEquals(1, c.getQuizzes().size());
        assertTrue(c.getTags().contains("b"));
    }

    @Test
    void usesDefaultsForOptionalParts() {
        Course c = Course.builder("T-2", "Defaults").addModule("Only", "L").build();
        assertEquals(Level.BEGINNER, c.getLevel());
        assertEquals(0.0, c.getBasePrice());
        assertEquals("TBA", c.getInstructor());
    }

    @Test
    void rejectsCourseWithoutModules() {
        assertThrows(IllegalStateException.class, () -> Course.builder("T-3", "Empty").build());
    }

    @Test
    void rejectsNegativePrice() {
        assertThrows(IllegalStateException.class,
                () -> Course.builder("T-4", "Bad").addModule("M", "L").price(-1).build());
    }

    @Test
    void builtCourseIsImmutable() {
        Course c = Course.builder("T-5", "Immutable").addModule("M", "L").build();
        assertThrows(UnsupportedOperationException.class, () -> c.getModules().clear());
    }

    @Test
    void directorBuildsBootcampRecipe() {
        Course c = new CourseDirector().buildBootcamp("B-1", "Spring Boot", "X", 99);
        assertEquals("Spring Boot Bootcamp", c.getTitle());
        assertEquals(3, c.getModules().size());
        assertEquals(2, c.getQuizzes().size());
        assertTrue(c.getTags().contains("spring-boot"));
    }
}
