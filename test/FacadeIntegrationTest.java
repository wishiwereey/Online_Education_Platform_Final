import builder.Course;
import decorator.AddOn;
import factory.Assignment;
import facade.LearningPlatformFacade;
import model.Level;
import model.Submission;
import observer.EventType;
import service.Enrollment;
import strategy.InterestBasedStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
class FacadeIntegrationTest {
    private LearningPlatformFacade platform;

    @BeforeEach
    void setUp() {
        platform = new LearningPlatformFacade();
        platform.setToday(LocalDate.of(2026, 11, 2));
        platform.publishCourse(Course.builder("C1", "Java").price(40).level(Level.BEGINNER)
                .addModule("M", "L").tags("java").build());
        platform.publishCourse(Course.builder("C2", "Patterns").price(60).level(Level.INTERMEDIATE)
                .addModule("M", "L").tags("java", "design-patterns").build());
        platform.registerStudent("S1", "Ann", "ann@x.edu", Level.BEGINNER, "design-patterns");
    }

    @Test
    void enrollChargesDecoratedPriceAndNotifies() {
        Enrollment e = platform.enroll("S1", "C1", AddOn.CERTIFICATE, AddOn.MENTOR);
        assertEquals(40 + 19.99 + 49.00, e.amountPaid(), 1e-9);
        assertEquals(e.amountPaid(), platform.totalRevenue(), 1e-9);
        assertEquals(1, platform.dashboard().count(EventType.ENROLLED));
        assertEquals(1, platform.notifications().forRecipient("ann@x.edu").size());
    }

    @Test
    void cannotEnrollTwice() {
        platform.enroll("S1", "C1");
        assertThrows(IllegalStateException.class, () -> platform.enroll("S1", "C1"));
        assertEquals(40.0, platform.totalRevenue(), 1e-9); // second attempt was not charged
    }

    @Test
    void gradingAllAssignmentsCompletesCourseAndLevelsUp() {
        platform.enroll("S1", "C1");
        Assignment a = platform.createAssignment("coding", "C1", "Task");
        Submission s = platform.submit("S1", a.getId(), "code");
        platform.grade(s, a.getMaxScore(), "perfect");

        assertTrue(platform.student("S1").getCompletedCourseIds().contains("C1"));
        assertEquals(Level.INTERMEDIATE, platform.student("S1").getLevel());
        assertEquals(1, platform.dashboard().count(EventType.COURSE_COMPLETED));
        assertEquals(1, platform.gradedAssignments("S1"));
    }

    @Test
    void cannotSubmitWithoutEnrollment() {
        Assignment a = platform.createAssignment("quiz", "C1", "Quiz");
        assertThrows(IllegalStateException.class, () -> platform.submit("S1", a.getId(), "x"));
    }

    @Test
    void remindersOnlyForUnsubmittedWork() {
        platform.enroll("S1", "C1");
        Assignment done = platform.createAssignment("quiz", "C1", "Done");
        platform.createAssignment("quiz", "C1", "Todo");
        platform.submit("S1", done.getId(), "answers");
        assertEquals(1, platform.sendDeadlineReminders(10));
    }

    @Test
    void recommendationsUseChosenStrategyAndSkipEnrolled() {
        platform.setRecommendationStrategy(new InterestBasedStrategy());
        assertEquals("C2", platform.recommend("S1", 3).get(0).getId());
        platform.enroll("S1", "C2");
        assertTrue(platform.recommend("S1", 3).isEmpty());
    }
}
