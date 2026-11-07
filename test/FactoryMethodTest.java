import factory.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FactoryMethodTest {
    private static final LocalDate START = LocalDate.of(2026, 1, 1);

    @Test
    void eachCreatorProducesItsOwnProduct() {
        assertInstanceOf(QuizAssignment.class, new QuizAssignmentFactory(5).publish("q", "C", START));
        assertInstanceOf(EssayAssignment.class, new EssayAssignmentFactory(300).publish("e", "C", START));
        assertInstanceOf(CodingAssignment.class, new CodingAssignmentFactory("Go", 3).publish("c", "C", START));
        assertInstanceOf(ProjectAssignment.class, new ProjectAssignmentFactory(2).publish("p", "C", START));
    }

    @Test
    void creatorSetsDeadlineAndScore() {
        Assignment quiz = new QuizAssignmentFactory(5).publish("q", "C", START);
        assertEquals(START.plusDays(7), quiz.getDeadline());
        assertEquals(10, quiz.getMaxScore());

        Assignment coding = new CodingAssignmentFactory("Java", 4).publish("c", "C", START);
        assertEquals(START.plusDays(14), coding.getDeadline());
        assertEquals(40, coding.getMaxScore());
    }

    @Test
    void idsAreUnique() {
        AssignmentFactory f = new EssayAssignmentFactory(100);
        assertNotEquals(f.publish("a", "C", START).getId(), f.publish("b", "C", START).getId());
    }

    @Test
    void forTypeSelectsCreatorByName() {
        assertEquals("Project", AssignmentFactory.forType("Project").publish("p", "C", START).getType());
        assertThrows(IllegalArgumentException.class, () -> AssignmentFactory.forType("exam"));
    }
}
