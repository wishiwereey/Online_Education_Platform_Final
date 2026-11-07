package factory;
import java.time.LocalDate;
public class CodingAssignmentFactory extends AssignmentFactory {
    private final String language;
    private final int testCount;
    public CodingAssignmentFactory(String language, int testCount) {
        this.language = language;
        this.testCount = testCount;
    }
    @Override
    protected Assignment createAssignment(String id, String title, String courseId, LocalDate deadline) {
        return new CodingAssignment(id, title, courseId, deadline, language, testCount);
    }
    @Override
    protected int defaultDurationDays() {
        return 14;
    }
}
