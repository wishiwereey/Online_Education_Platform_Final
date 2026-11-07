package factory;
import java.time.LocalDate;
public class QuizAssignmentFactory extends AssignmentFactory {
    private final int questions;
    public QuizAssignmentFactory(int questions) {
        this.questions = questions;
    }
    @Override
    protected Assignment createAssignment(String id, String title, String courseId, LocalDate deadline) {
        return new QuizAssignment(id, title, courseId, deadline, questions);
    }
    @Override
    protected int defaultDurationDays() {
        return 7;
    }
}
