package factory;
import java.time.LocalDate;
public class ProjectAssignmentFactory extends AssignmentFactory {
    private final int teamSize;
    public ProjectAssignmentFactory(int teamSize) {
        this.teamSize = teamSize;
    }
    @Override
    protected Assignment createAssignment(String id, String title, String courseId, LocalDate deadline) {
        return new ProjectAssignment(id, title, courseId, deadline, teamSize);
    }
    @Override
    protected int defaultDurationDays() {
        return 30;
    }
}
