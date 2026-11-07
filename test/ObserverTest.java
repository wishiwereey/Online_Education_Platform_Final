import model.Level;
import model.Student;
import observer.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {
    private final Student student = new Student("S", "Ann", "ann@x.edu", Level.BEGINNER, List.of());

    @Test
    void observerReceivesOnlySubscribedTypes() {
        NotificationCenter center = new NotificationCenter();
        List<LearningEvent> got = new ArrayList<>();
        center.subscribe(got::add, EventType.ASSIGNMENT_GRADED);

        center.publish(new LearningEvent(EventType.ENROLLED, student, "C", "enrolled"));
        center.publish(new LearningEvent(EventType.ASSIGNMENT_GRADED, student, "C", "graded"));

        assertEquals(1, got.size());
        assertEquals("graded", got.get(0).message());
    }

    @Test
    void allObserversAreNotifiedAndCanUnsubscribe() {
        NotificationCenter center = new NotificationCenter();
        NotificationLog log = new NotificationLog();
        EmailNotifier email = new EmailNotifier(log);
        center.subscribe(email, EventType.ASSIGNMENT_GRADED);
        center.subscribe(new PushNotifier(log), EventType.ASSIGNMENT_GRADED);

        center.publish(new LearningEvent(EventType.ASSIGNMENT_GRADED, student, "C", "10/10"));
        assertEquals(2, log.all().size());

        center.unsubscribe(email);
        center.publish(new LearningEvent(EventType.ASSIGNMENT_GRADED, student, "C", "9/10"));
        assertEquals(3, log.all().size());
        assertEquals("PUSH", log.all().get(2).channel());
    }

    @Test
    void emailIgnoresBroadcastButPushDeliversIt() {
        NotificationCenter center = new NotificationCenter();
        NotificationLog log = new NotificationLog();
        center.subscribe(new EmailNotifier(log));
        center.subscribe(new PushNotifier(log));
        center.publish(new LearningEvent(EventType.COURSE_PUBLISHED, null, "C", "new course"));
        assertEquals(1, log.all().size());
        assertEquals("all students", log.all().get(0).recipient());
    }

    @Test
    void dashboardCountsEvents() {
        NotificationCenter center = new NotificationCenter();
        InstructorDashboard dash = new InstructorDashboard();
        center.subscribe(dash);
        center.publish(new LearningEvent(EventType.ENROLLED, student, "C", ""));
        center.publish(new LearningEvent(EventType.ENROLLED, student, "C", ""));
        assertEquals(2, dash.count(EventType.ENROLLED));
        assertEquals(0, dash.count(EventType.COURSE_COMPLETED));
    }
}
