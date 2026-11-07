package factory;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
public abstract class AssignmentFactory {
    private static final AtomicInteger SEQ = new AtomicInteger(1);
    protected abstract Assignment createAssignment(String id, String title, String courseId, LocalDate deadline);
    protected abstract int defaultDurationDays();
    public final Assignment publish(String title, String courseId, LocalDate startDate) {
        String id = "A" + SEQ.getAndIncrement();
        LocalDate deadline = startDate.plusDays(defaultDurationDays());
        return createAssignment(id, title, courseId, deadline);
    }
    public static AssignmentFactory forType(String type) {
        return switch (type.trim().toLowerCase()) {
            case "quiz" -> new QuizAssignmentFactory(10);
            case "essay" -> new EssayAssignmentFactory(500);
            case "coding" -> new CodingAssignmentFactory("Java", 5);
            case "project" -> new ProjectAssignmentFactory(3);
            default -> throw new IllegalArgumentException("Unknown assignment type: " + type);
        };
    }
}
