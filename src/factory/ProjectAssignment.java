package factory;
import java.time.LocalDate;
public class ProjectAssignment extends AbstractAssignment {
    private final int teamSize;
    ProjectAssignment(String id, String title, String courseId, LocalDate deadline, int teamSize) {
        super(id, title, courseId, deadline, 200);
        this.teamSize = teamSize;
    }
    public int getTeamSize() {
        return teamSize;
    }
    @Override public String getType() {
        return "Project";
    }
    @Override public String getInstructions() {
        return "Work in a team of up to " + teamSize + " people and defend the result.";
    }
}
