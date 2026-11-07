package factory;
import java.time.LocalDate;
public class EssayAssignmentFactory extends AssignmentFactory {
    private final int minWords;
    public EssayAssignmentFactory(int minWords) {
        this.minWords = minWords;
    }
    @Override
    protected Assignment createAssignment(String id, String title, String courseId, LocalDate deadline) {
        return new EssayAssignment(id, title, courseId, deadline, minWords);
    }
    @Override
    protected int defaultDurationDays() {
        return 10; }
}
